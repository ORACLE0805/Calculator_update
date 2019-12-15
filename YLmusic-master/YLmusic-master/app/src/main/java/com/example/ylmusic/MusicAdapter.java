package com.example.ylmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.net.URL;
import java.util.List;
import java.util.logging.LogRecord;


public class MusicAdapter extends BaseAdapter {
    ViewHolder holder=null;
    Context context;
    List<allSongs_json.ResultBean.SongsBean> list;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            holder.album.setImageBitmap( (Bitmap) msg.obj);
        }
    };


    int layout;
    public MusicAdapter(List<allSongs_json.ResultBean.SongsBean> list,Context context){
        this.context=context;

        this.list=list;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertview, ViewGroup parent) {

        if(convertview==null){
            convertview=LayoutInflater.from(context).inflate(R.layout.music_item,parent,false);
//
            holder=new ViewHolder();
            holder.title=convertview.findViewById(R.id.musicitem_title);
            holder.artist=convertview.findViewById(R.id.musicitem_artist);
            holder.album=convertview.findViewById(R.id.musicitem_album);
            holder.playing=convertview.findViewById(R.id.m_playing);
            convertview.setTag(holder);
        }else{
            holder=(ViewHolder) convertview.getTag();
        }
        holder.title.setText(list.get(position).getName());
        holder.artist.setText(list.get(position).getArtists().get(0).getName());


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //URL picurl = new URL("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1571849637833&di=bd2e59e2b4d62195704f5434edd33bfc&imgtype=0&src=http%3A%2F%2Fgss0.baidu.com%2F-vo3dSag_xI4khGko9WTAnF6hhy%2Fzhidao%2Fpic%2Fitem%2F09fa513d269759eef0b9305cb3fb43166c22dfb8.jpg");
                    URL picurl = new URL(list.get(position).getAlbum().getArtist().getImg1v1Url());
                    Bitmap bitmap= BitmapFactory.decodeStream(picurl.openStream());
                    if(bitmap != null){
                        Log.d("#BITMAP","ok");
                    }else{
                        Log.d("#BITMAP","failed");
                    }

                    Message message = new Message();
                    message.obj=bitmap;
                    handler.sendMessage(message);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        return convertview;
    }
    static class ViewHolder{
        TextView title;
        TextView artist;
        ImageView album;
        View playing;
    }
}
