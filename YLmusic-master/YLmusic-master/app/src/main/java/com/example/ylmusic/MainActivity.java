package com.example.ylmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView my_music;
    private TextView search_music;
    private ViewPager viewPager;
    private List<Fragment> fragmentList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getId();
        setOnClick();

        Left_fragment left_fragment=new Left_fragment();
        Right_fragment right_fragment=new Right_fragment();
        fragmentList.add(left_fragment);
        fragmentList.add(right_fragment);
        Music_pager music_pager=new Music_pager(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(music_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        my_music.setTextColor(Color.WHITE);
                        search_music.setTextColor(Color.rgb(204,204,204));

                        break;
                    case 1:
                        my_music.setTextColor(Color.rgb(204,204,204));
                        search_music.setTextColor(Color.WHITE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void getId(){
        my_music=findViewById(R.id.main_mymusic);
        search_music=findViewById(R.id.main_online);
        viewPager=findViewById(R.id.main_fragment);
    }
    private void setOnClick(){
        my_music.setOnClickListener(this);
        search_music.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_mymusic:
                viewPager.setCurrentItem(0);

                break;
            case R.id.main_online:
                viewPager.setCurrentItem(1);
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
