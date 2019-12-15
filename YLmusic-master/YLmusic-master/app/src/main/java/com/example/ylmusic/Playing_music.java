package com.example.ylmusic;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.nfc.cardemulation.OffHostApduService;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Playing_music extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {
    String my_title;
    String my_artist;
    String my_image;
    String my_url;


    Boolean ischange=false;
    ImageView m_background;
    ImageView close_music;
    TextView  music_name;
    TextView music_artist;
    ImageView disc;
    ImageView needle;
    TextView m_current;
    SeekBar m_seekBar;//时间轴
    TextView m_total;
    ImageView m_inturn;
    ImageView m_before;
    ImageView m_pause;
    ImageView m_next;
    ImageView m_close;
    ImageView getSongs;
    int i=0;
    int position;
    int music_id;
    String my_id;
    int playAgagin=0;
    int btmode=0;
    String mp3_url;
    String name;
    String artist;
    MediaPlayer mediaPlayer;
    musciUrl musicUrlBean;
    boolean isStop;
    private  ObjectAnimator objectAnimator=null;
    private RotateAnimation rotateAnimation = null;
    private RotateAnimation rotateAnimation2 = null;

    Mylist_DBhelper mylist_dBhelper;
    SQLiteDatabase music_db;

    private Thread thread;

    List<allSongs_json.ResultBean.SongsBean> music_list;//歌曲存放链表
//    private Handler handler=new Handler(){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            m_seekBar.setProgress((int)msg.what);
//            m_current.setText(formatTime(msg.what));
//        }
//    };


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        m_current.setText(formatTime(i));
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {//拖动seekbar改变歌曲进度
        m_current.setText(formatTime(seekBar.getProgress()));
        mediaPlayer.seekTo(seekBar.getProgress());
        ischange=false;
        thread=new Thread(new SeekBarThread());
        thread.start();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        ischange=true;
    }

    class SeekBarThread implements Runnable{//seekbar随歌曲移动
        @Override
        public void run() {
            while(!isStop&&music_list.get(position)!=null){
                m_seekBar.setProgress(mediaPlayer.getCurrentPosition());
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playing_music);
        m_background=findViewById(R.id.m_background);
        close_music=findViewById(R.id.close_music);
        music_name=findViewById(R.id.music_name);
        music_artist=findViewById(R.id.music_artist);
        disc=findViewById(R.id.disc);
        needle=findViewById(R.id.needle);
        m_current=findViewById(R.id.m_current);
        m_total=findViewById(R.id.m_total);
        m_close=findViewById(R.id.close_music);
        m_pause=findViewById(R.id.m_pause);
        m_next=findViewById(R.id.m_next);
        m_seekBar=findViewById(R.id.m_seekbar);
        m_inturn=findViewById(R.id.m_inturn);
        m_before=findViewById(R.id.m_before);
        getSongs=findViewById(R.id.getSongs);
        getSongs.setOnClickListener(this);
        m_before.setOnClickListener(this);
        m_pause.setOnClickListener(this);
        m_next.setOnClickListener(this);
        m_close.setOnClickListener(this);
        m_inturn.setOnClickListener(this);
        m_seekBar.setOnSeekBarChangeListener(this);
        Intent intent=getIntent();
        music_list=(List<allSongs_json.ResultBean.SongsBean>) intent.getSerializableExtra("music_list");
        position=intent.getIntExtra("position",0);
        name=music_list.get(position).getName();
        artist=music_list.get(position).getArtists().get(0).getName();
        music_id=music_list.get(position).getId();
       // Log.d("#artist",artist);
        my_id=String.valueOf(music_id);
        mediaPlayer=new MediaPlayer();
        music_name.setText(name);
        my_title=name;
        music_artist.setText(artist);
        my_artist=artist;
        String music_url="http://bzpnb.xyz:3000/song/url?id="+music_id;
        my_image=music_list.get(position).getAlbum().getArtist().getImg1v1Url();
        HttpUtil.sendOkHttpRequest(music_url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(Playing_music.this,"获取数据失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        musicUrlBean=getM_all(result);
                        prePlaying(musicUrlBean.getData().get(0).getUrl());
                    }
                });
            }
        });

//        mp3_url=musicUrlBean.getData().get(0).getUrl();




    }
    private void prePlaying(String url){
        my_url=url;
        isStop=false;
        mediaPlayer.reset();
        m_pause.setImageResource(R.drawable.pause);
        Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.background)).getBitmap();
        Bitmap background=BlurUtil.doBlur(bitmap,10,5);
        m_background.setImageBitmap(background);
        Bitmap desc_img= BitmapFactory.decodeResource(getResources(),R.drawable.disc);
        Bitmap bm=MergeImage.mergeThumbnailBitmap(desc_img,bitmap);
        disc.setImageBitmap(bm);
        try{
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @RequiresApi(api= Build.VERSION_CODES.KITKAT)
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if(!mediaPlayer.isPlaying()){
                        setPlayMode();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        m_total.setText(formatTime(mediaPlayer.getDuration()));
        m_seekBar.setMax(mediaPlayer.getDuration());
        //m_Thread m_thread=new m_Thread();

        //new Thread(m_thread).start();
        thread=new Thread(new SeekBarThread());
        thread.start();
        objectAnimator=ObjectAnimator.ofFloat(disc, "rotation",0f,360f);
        objectAnimator.setDuration(8000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.start();

        rotateAnimation=new RotateAnimation(-25,0f, Animation.RELATIVE_TO_SELF,0.3f,Animation.RELATIVE_TO_SELF,0.1f);
        rotateAnimation.setDuration(500);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setStartOffset(500);
        needle.setAnimation(rotateAnimation);
        rotateAnimation.cancel();

        rotateAnimation2 = new RotateAnimation(0f, -25f, Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF, 0.1f);
        rotateAnimation2.setDuration(500);
        rotateAnimation2.setInterpolator(new LinearInterpolator());
        rotateAnimation2.setRepeatCount(0);
        rotateAnimation2.setFillAfter(true);
        needle.setAnimation(rotateAnimation2);
        rotateAnimation2.cancel();

    }
    @RequiresApi(api= Build.VERSION_CODES.KITKAT)
    private void setPlayMode(){
        if(playAgagin==0){
            //列表循环
            if(position==music_list.size()-1){
                position=0;
                mediaPlayer.reset();
                objectAnimator.pause();
                needle.setAnimation(rotateAnimation2);
                String music_url="http://bzpnb.xyz:3000/song/url?id="+music_list.get(position).getId();
                HttpUtil.sendOkHttpRequest(music_url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(Playing_music.this,"获取数据失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result=response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                musicUrlBean=getM_all(result);
                                prePlaying(musicUrlBean.getData().get(0).getUrl());

                            }
                        });
                    }
                });

            }
        }
        else if(playAgagin==1){
            //单曲循环
            mediaPlayer.reset();
            objectAnimator.pause();
            needle.setAnimation(rotateAnimation2);
            String music_url="http://bzpnb.xyz:3000/song/url?id="+music_list.get(position).getId();
            HttpUtil.sendOkHttpRequest(music_url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(Playing_music.this,"获取数据失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result=response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            musicUrlBean=getM_all(result);
                            prePlaying(musicUrlBean.getData().get(0).getUrl());
                        }
                    });
                }
            });

        }
        else if(playAgagin==2){
            position=(int)(Math.random()*music_list.size());//随机播放
            mediaPlayer.reset();
            objectAnimator.pause();
            needle.setAnimation(rotateAnimation2);
            String music_url="http://bzpnb.xyz:3000/song/url?id="+music_list.get(position).getId();
            HttpUtil.sendOkHttpRequest(music_url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(Playing_music.this,"获取数据失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result=response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            musicUrlBean=getM_all(result);
                            prePlaying(musicUrlBean.getData().get(0).getUrl());
                        }
                    });
                }
            });
        }

    }

    private String formatTime(int length) {
        Date date = new Date(length);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");    //规定固定的格式
        String totaltime = simpleDateFormat.format(date);
        return totaltime;
    }
    public static musciUrl getM_all(String res) {
        Gson gson = new Gson();
        musciUrl musciUrl = gson.fromJson(res, musciUrl.class);
        return musciUrl;
    }

    @Override
    protected void onPause() {
        super.onPause();
        for(allSongs_json.ResultBean.SongsBean songsBean:music_list){
            songsBean.playing=false;
        }
        music_list.get(position).playing=true;

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setButtonMode(){
        if(playAgagin==0){
            if(position==music_list.size()-1){
                if(btmode==1){
                    position--;
                    mediaPlayer.reset();
                    objectAnimator.pause();
                    needle.startAnimation(rotateAnimation2);
                    music_name.setText(music_list.get(position).getName());
                    music_artist.setText(music_list.get(position).getArtists().get(0).getName());
                    String music_url="http://bzpnb.xyz:3000/song/url?id="+music_list.get(position).getId();
                    HttpUtil.sendOkHttpRequest(music_url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Toast.makeText(Playing_music.this,"获取数据失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String result=response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    musicUrlBean=getM_all(result);
                                    prePlaying(musicUrlBean.getData().get(0).getUrl());
                                }
                            });
                        }
                    });
                }
                else if(btmode==2){
                    position=0;
                    mediaPlayer.reset();
                    objectAnimator.pause();
                    needle.startAnimation(rotateAnimation2);
                    music_name.setText(music_list.get(position).getName());
                    music_artist.setText(music_list.get(position).getArtists().get(0).getName());
                    String music_url="http://bzpnb.xyz:3000/song/url?id="+music_list.get(position).getId();
                    HttpUtil.sendOkHttpRequest(music_url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Toast.makeText(Playing_music.this,"获取数据失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String result=response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    musicUrlBean=getM_all(result);
                                    prePlaying(musicUrlBean.getData().get(0).getUrl());
                                }
                            });
                        }
                    });
                }

            }
            else if(position==0){
                if(btmode==1){
                    position=music_list.size()-1;
                    mediaPlayer.reset();
                    objectAnimator.pause();
                    needle.startAnimation(rotateAnimation2);
                    music_name.setText(music_list.get(position).getName());
                    music_artist.setText(music_list.get(position).getArtists().get(0).getName());
                    String music_url="http://bzpnb.xyz:3000/song/url?id="+music_list.get(position).getId();
                    HttpUtil.sendOkHttpRequest(music_url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Toast.makeText(Playing_music.this,"获取数据失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String result=response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    musicUrlBean=getM_all(result);
                                    prePlaying(musicUrlBean.getData().get(0).getUrl());
                                }
                            });
                        }
                    });
                }
                else if(btmode==2){
                    position++;
                    mediaPlayer.reset();
                    objectAnimator.pause();
                    needle.startAnimation(rotateAnimation2);
                    music_name.setText(music_list.get(position).getName());
                    music_artist.setText(music_list.get(position).getArtists().get(0).getName());
                    String music_url="http://bzpnb.xyz:3000/song/url?id="+music_list.get(position).getId();
                    HttpUtil.sendOkHttpRequest(music_url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Toast.makeText(Playing_music.this,"获取数据失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String result=response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    musicUrlBean=getM_all(result);
                                    prePlaying(musicUrlBean.getData().get(0).getUrl());
                                }
                            });
                        }
                    });
                }
            }
            else{
                if(btmode==1){
                    position--;
                    mediaPlayer.reset();
                    objectAnimator.pause();
                    needle.startAnimation(rotateAnimation2);
                    music_name.setText(music_list.get(position).getName());
                    music_artist.setText(music_list.get(position).getArtists().get(0).getName());
                    String music_url="http://bzpnb.xyz:3000/song/url?id="+music_list.get(position).getId();
                    HttpUtil.sendOkHttpRequest(music_url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Toast.makeText(Playing_music.this,"获取数据失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String result=response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    musicUrlBean=getM_all(result);
                                    prePlaying(musicUrlBean.getData().get(0).getUrl());
                                }
                            });
                        }
                    });
                }
                else if(btmode==2){
                    position++;
                    mediaPlayer.reset();
                    objectAnimator.pause();
                    needle.startAnimation(rotateAnimation2);
                    music_name.setText(music_list.get(position).getName());
                    music_artist.setText(music_list.get(position).getArtists().get(0).getName());
                    String music_url="http://bzpnb.xyz:3000/song/url?id="+music_list.get(position).getId();
                    HttpUtil.sendOkHttpRequest(music_url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Toast.makeText(Playing_music.this,"获取数据失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String result=response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    musicUrlBean=getM_all(result);
                                    prePlaying(musicUrlBean.getData().get(0).getUrl());
                                }
                            });
                        }
                    });
                }
            }
        }
        else if(playAgagin==1){
            mediaPlayer.reset();
            objectAnimator.pause();
            needle.startAnimation(rotateAnimation2);
            music_name.setText(music_list.get(position).getName());
            music_artist.setText(music_list.get(position).getArtists().get(0).getName());
            String music_url="http://bzpnb.xyz:3000/song/url?id="+music_list.get(position).getId();
            HttpUtil.sendOkHttpRequest(music_url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(Playing_music.this,"获取数据失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result=response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            musicUrlBean=getM_all(result);
                            prePlaying(musicUrlBean.getData().get(0).getUrl());
                        }
                    });
                }
            });
        }
        else if(playAgagin==2){
            position=(int)(Math.random()*music_list.size());
            mediaPlayer.reset();
            objectAnimator.pause();
            needle.startAnimation(rotateAnimation2);
            music_name.setText(music_list.get(position).getName());
            music_artist.setText(music_list.get(position).getArtists().get(0).getName());
            String music_url="http://bzpnb.xyz:3000/song/url?id="+music_list.get(position).getId();
            HttpUtil.sendOkHttpRequest(music_url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(Playing_music.this,"获取数据失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result=response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            musicUrlBean=getM_all(result);
                            prePlaying(musicUrlBean.getData().get(0).getUrl());
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        i=0;
        isStop=false;
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        if(mediaPlayer!=null){
            mediaPlayer.stop();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.m_before:
                btmode=1;
                setButtonMode();
                break;
            case R.id.m_pause:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    objectAnimator.cancel();
                    needle.startAnimation(rotateAnimation2);
                    m_pause.setImageResource(R.drawable.play);
                }else{
                    mediaPlayer.start();
                    objectAnimator.start();
                    needle.startAnimation(rotateAnimation);
                    m_pause.setImageResource(R.drawable.pause);
                }
                break;
            case R.id.m_next:
                btmode=2;
                setButtonMode();
                break;
            case R.id.m_inturn:
                i++;
                if (i % 3 == 1) {
                    Toast.makeText(Playing_music.this, "单曲", Toast.LENGTH_SHORT).show();
                    playAgagin = 1;
                    m_inturn.setImageResource(R.drawable.one_again);
                }
                if (i % 3 == 2) {
                    Toast.makeText(Playing_music.this, "随机", Toast.LENGTH_SHORT).show();
                    playAgagin = 2;
                    m_inturn.setImageResource(R.drawable.all_shuff);
                }
                if (i % 3 == 0) {
                    Toast.makeText(Playing_music.this, "顺序", Toast.LENGTH_SHORT).show();
                    playAgagin= 0;
                    m_inturn.setImageResource(R.drawable.in_turn);
                }
                break;
            case R.id.close_music:

                this.finish();
               // moveTaskToBack(true);
                break;
            case R.id.getSongs://收藏，对数据库进行插入操作
                mylist_dBhelper=new Mylist_DBhelper(this);
                music_db=mylist_dBhelper.getWritableDatabase();
                music_db.execSQL("insert into musics values(null,?,?,?,?)",new String[]{my_title,my_artist,my_image,my_id});
                music_db.close();
                getSongs.setImageDrawable(getResources().getDrawable(R.drawable.get));
                Toast.makeText(this,"已收藏^-^",Toast.LENGTH_SHORT).show();
                default:
                    break;

        }
    }

//    class m_Thread implements Runnable{
//        @Override
//        public void run() {
//            while(!isStop&&music_list.get(position)!=null){
//                try{
//                    Thread.sleep(2000);
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//
//               handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());
//            }
//
//        }
//    }
}

