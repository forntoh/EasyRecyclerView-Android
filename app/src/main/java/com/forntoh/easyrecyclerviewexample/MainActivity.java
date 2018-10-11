package com.forntoh.easyrecyclerviewexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.forntoh.EasyRecyclerView.EasyRecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new EasyRecyclerView()
                .setType(EasyRecyclerView.Type.GRID)
                .setAdapter(new ListAdapter())
                .setRecyclerView(findViewById(R.id.rv_list))
                .setItemSpacing(16, null)
                .setSpan(4)
                .enableDividers(false)
                .initialize();
    }
}
