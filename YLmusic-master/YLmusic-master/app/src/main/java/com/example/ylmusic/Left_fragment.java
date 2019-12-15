package com.example.ylmusic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Left_fragment extends Fragment {
    ListView listView;
    Mylist_DBhelper mylist_dBhelper;
    SQLiteDatabase music_db;
    Cursor cursor;
    String del_title;
    TextView refresh;

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case 0:
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("友情提示");
                builder.setMessage("您确定要删除吗？");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mylist_dBhelper=new Mylist_DBhelper(getActivity());
                        music_db=mylist_dBhelper.getWritableDatabase();
                        music_db.execSQL("delete from musics where title=?",new String[]{del_title});
                        final List<allSongs_json.ResultBean.SongsBean> my_list=new LinkedList<>();


                        mylist_dBhelper=new Mylist_DBhelper(getActivity());
                        music_db=mylist_dBhelper.getReadableDatabase();
                        cursor=music_db.rawQuery("select * from musics",null);


                        for(int j=0;j<cursor.getCount();j++){
                            allSongs_json.ResultBean.SongsBean songsBean=new allSongs_json.ResultBean.SongsBean();
                            allSongs_json.ResultBean.SongsBean.ArtistsBean artistsBean=new allSongs_json.ResultBean.SongsBean.ArtistsBean();
                            List<allSongs_json.ResultBean.SongsBean.ArtistsBean> artistsBeanList=new LinkedList<>();
                            allSongs_json.ResultBean.SongsBean.AlbumBean albumBean=new allSongs_json.ResultBean.SongsBean.AlbumBean();
                            allSongs_json.ResultBean.SongsBean.AlbumBean.ArtistBean artistBean=new allSongs_json.ResultBean.SongsBean.AlbumBean.ArtistBean();
                            cursor.moveToNext();
                            songsBean.setName(cursor.getString(cursor.getColumnIndex("title")));
                            songsBean.setId(Integer.parseInt((cursor.getString(cursor.getColumnIndex("music_id")))));
                            artistsBean.setName(cursor.getString(cursor.getColumnIndex("artist")));
                            artistBean.setImg1v1Url(cursor.getString(cursor.getColumnIndex("img")));
                            albumBean.setArtist(artistBean);

                            artistsBeanList.add(artistsBean);
                            my_list.add(songsBean);
                            my_list.get(j).setArtists(artistsBeanList);
                            my_list.get(j).setAlbum(albumBean);

                        }
                        listView.setAdapter(new MusicAdapter((List<allSongs_json.ResultBean.SongsBean>) my_list,getActivity()));
                        Toast.makeText(getActivity(),"已删除",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();

                return true;
                default:
                    return super.onContextItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final List<allSongs_json.ResultBean.SongsBean> my_list=new LinkedList<>();

        View view=inflater.inflate(R.layout.left_fragment,container,false);
        mylist_dBhelper=new Mylist_DBhelper(getActivity());
        music_db=mylist_dBhelper.getReadableDatabase();
        cursor=music_db.rawQuery("select * from musics",null);
        listView=view.findViewById(R.id.my_list);
        refresh=view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<allSongs_json.ResultBean.SongsBean> my_list=new LinkedList<>();


                mylist_dBhelper=new Mylist_DBhelper(getActivity());
                music_db=mylist_dBhelper.getReadableDatabase();
                cursor=music_db.rawQuery("select * from musics",null);


                for(int j=0;j<cursor.getCount();j++){
                    allSongs_json.ResultBean.SongsBean songsBean=new allSongs_json.ResultBean.SongsBean();
                    allSongs_json.ResultBean.SongsBean.ArtistsBean artistsBean=new allSongs_json.ResultBean.SongsBean.ArtistsBean();
                    List<allSongs_json.ResultBean.SongsBean.ArtistsBean> artistsBeanList=new LinkedList<>();
                    allSongs_json.ResultBean.SongsBean.AlbumBean albumBean=new allSongs_json.ResultBean.SongsBean.AlbumBean();
                    allSongs_json.ResultBean.SongsBean.AlbumBean.ArtistBean artistBean=new allSongs_json.ResultBean.SongsBean.AlbumBean.ArtistBean();
                    cursor.moveToNext();
                    songsBean.setName(cursor.getString(cursor.getColumnIndex("title")));
                    songsBean.setId(Integer.parseInt((cursor.getString(cursor.getColumnIndex("music_id")))));
                    artistsBean.setName(cursor.getString(cursor.getColumnIndex("artist")));
                    artistBean.setImg1v1Url(cursor.getString(cursor.getColumnIndex("img")));
                    albumBean.setArtist(artistBean);

                    artistsBeanList.add(artistsBean);
                    my_list.add(songsBean);
                    my_list.get(j).setArtists(artistsBeanList);
                    my_list.get(j).setAlbum(albumBean);

                }
                listView.setAdapter(new MusicAdapter((List<allSongs_json.ResultBean.SongsBean>) my_list,getActivity()));
            }
        });
        for(int i=0;i<cursor.getCount();i++){
            allSongs_json.ResultBean.SongsBean songsBean=new allSongs_json.ResultBean.SongsBean();
            allSongs_json.ResultBean.SongsBean.ArtistsBean artistsBean=new allSongs_json.ResultBean.SongsBean.ArtistsBean();
            List<allSongs_json.ResultBean.SongsBean.ArtistsBean> artistsBeanList=new LinkedList<>();
            allSongs_json.ResultBean.SongsBean.AlbumBean albumBean=new allSongs_json.ResultBean.SongsBean.AlbumBean();
            allSongs_json.ResultBean.SongsBean.AlbumBean.ArtistBean artistBean=new allSongs_json.ResultBean.SongsBean.AlbumBean.ArtistBean();
            cursor.moveToNext();
            songsBean.setName(cursor.getString(cursor.getColumnIndex("title")));
            songsBean.setId(Integer.parseInt((cursor.getString(cursor.getColumnIndex("music_id")))));
            artistsBean.setName(cursor.getString(cursor.getColumnIndex("artist")));
            artistBean.setImg1v1Url(cursor.getString(cursor.getColumnIndex("img")));
            albumBean.setArtist(artistBean);

            artistsBeanList.add(artistsBean);
            my_list.add(songsBean);
            my_list.get(i).setArtists(artistsBeanList);
            my_list.get(i).setAlbum(albumBean);

        }
        listView.setAdapter(new MusicAdapter((List<allSongs_json.ResultBean.SongsBean>) my_list,getActivity()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                del_title=my_list.get(i).getName();
                Intent intent=new Intent(getActivity(),Playing_music.class);
                intent.putExtra("position",i);
                intent.putExtra("music_list",(Serializable)my_list);
                startActivity(intent);
            }
        });
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.setHeaderTitle("选择操作");
                contextMenu.add(0,0,0,"删除该条");
            }
        });

        return view;
    }


}
