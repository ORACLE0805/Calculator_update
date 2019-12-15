package com.example.ylmusic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Right_fragment extends Fragment {
    EditText music_name;
    TextView find_music;
    ListView songList;

    private List<allSongs_json.ResultBean.SongsBean> music_jsonList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.right_fragment,container,false);
        songList=view.findViewById(R.id.search_music);
        music_name=view.findViewById(R.id.music);//寻找的歌曲名称
        find_music=view.findViewById(R.id.find);//搜索按钮

//        songsList=view.findViewById(R.id.search_music);
        find_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {//搜索歌曲
                String search_name=music_name.getText().toString();
                String songs_url="";
                //music_name.setText("ja  ");
                    songs_url="http://bzpnb.xyz:3000/search?keywords="+search_name;
                HttpUtil.sendOkHttpRequest(songs_url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(getActivity(),"获取数据失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result=response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               final  allSongs_json allSongs_json;

                                allSongs_json=getall(result);

                                music_jsonList=allSongs_json.getResult().getSongs();
                                songList.setAdapter(new MusicAdapter((List<allSongs_json.ResultBean.SongsBean>) music_jsonList,getActivity()));
                                //Toast.makeText(getActivity(),"lall"+music_jsonList.get(0).getArtists().get(0).getName(),Toast.LENGTH_SHORT).show();


//                         n       songsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                       for(int j=0;i<music_jsonList.size();i++){
//                                           allSongs_json.getResult().getSongs().get(j).playing=false;
//                                       }
//                                       Common.music_jsonList.get(i).playing=true;
//                                       adapter.notifyDataSetChanged();
//                                    }
//                                });
//                                adapter=new Adapter(getActivity(),music_jsonList);
//                                songsList.setAdapter(adapter);




                            }
                        });
                    }
                });
            }
        });
        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),Playing_music.class);
                intent.putExtra("position",i);
                intent.putExtra("m_name",music_jsonList.get(i).getName());
                intent.putExtra("m_artist",music_jsonList.get(i).getArtists().get(0).getName());
                intent.putExtra("music_id",music_jsonList.get(i).getId());
                intent.putExtra("music_list",(Serializable)music_jsonList);
                startActivity(intent);

               // Toast.makeText(getActivity(),music_jsonList.get(i).getName(),Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }



    public static allSongs_json getall(String res){
        Gson gson=new Gson();//android解析工具gson
        allSongs_json tbean=gson.fromJson(res, allSongs_json.class);
        return tbean;
    }

}
