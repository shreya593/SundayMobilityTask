package com.mj.shreyajaiswal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.mj.shreyajaiswal.MainActivity.captain;
import static com.mj.shreyajaiswal.MainActivity.playername;


public class MainActivity2 extends AppCompatActivity {
ListView listView;
Button fnbtn,lnbtn;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView = findViewById(R.id.listView1);
        fnbtn = findViewById(R.id.fn);
        lnbtn = findViewById(R.id.ln);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,playername);
        // listView.setAdapter(adapter);
        listView.setAdapter(adapter);
      fnbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Collections.sort(playername);
              adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,playername);
              listView.setAdapter(adapter);
          }
      });
      lnbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              sortLast();
              adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,playername);
              listView.setAdapter(adapter);
          }
      });
    }
    public void sortLast() {
        Collections.sort(playername, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] split1 = o1.split(" ");
                String[] split2 = o2.split(" ");
                String lastName1 = split1[1];
                String lastName2 = split2[1];
                if (lastName1.compareTo(lastName2) > 0) {
                    return 1;
                } else {
                    return -1;
                }

            }

        });
        //System.out.println(al);
        return;
    }
}