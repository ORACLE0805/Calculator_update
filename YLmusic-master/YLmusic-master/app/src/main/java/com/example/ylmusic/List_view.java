package com.example.ylmusic;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class List_view extends ListView {
    public List_view(Context context){
        super(context);
    }
    public List_view(Context context, AttributeSet attr){
        super(context,attr);
    }
    public List_view(Context context, AttributeSet attr,int def){
        super(context,attr,def);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
