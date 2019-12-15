package com.example.contentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ProviderTestActivity";
    private String key = "ssbz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_test);

        Button button_add = findViewById(R.id.provider_button_add);
        Button button_update = findViewById(R.id.provider_button_update);
        Button button_delete = findViewById(R.id.provider_button_delete);
        Button button_query = findViewById(R.id.provider_button_query);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("content://com.hwhhhh.wordbook.provider/word");
                ContentValues values = new ContentValues();
                values.put("word", "ssbz");
                values.put("pronunciation", "subozhong");
                values.put("meaning", "SBZ");
                Uri newUri = getContentResolver().insert(uri, values);
                if (newUri != null) {
                    key = newUri.getPathSegments().get(1);
                    Toast.makeText(MainActivity.this, "add successfully!", Toast.LENGTH_LONG).show();
                }
            }
        });

        button_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("content://com.hwhhhh.wordbook.provider/word/" + key);
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String word = cursor.getString(cursor.getColumnIndex("word"));
                        String pronunciation = cursor.getString(cursor.getColumnIndex("pronunciation"));
                        String meaning = cursor.getString(cursor.getColumnIndex("meaning"));

                        Log.d(TAG, "onClick: word: " + word + " pronunciation: " + pronunciation + " meaning: " + meaning);
                    }
                    cursor.close();
                }
            }
        });

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("content://com.hwhhhh.wordbook.provider/word/" + key);
                ContentValues values = new ContentValues();
                values.put("word", "ssbz1");
                values.put("pronunciation", "ssbz1");
                values.put("meaning", "ssu1");
                key = "ssbz1";
                if (getContentResolver().update(uri, values, null, null) > 0) {
                    Toast.makeText(MainActivity.this, "update successfully!", Toast.LENGTH_LONG).show();
                }
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("content://com.hwhhhh.wordbook.provider/word/" + key);
                if (getContentResolver().delete(uri, null, null) > 0) {
                    Toast.makeText(MainActivity.this, "delete successfully!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
