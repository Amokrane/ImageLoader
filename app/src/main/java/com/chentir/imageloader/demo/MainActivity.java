package com.chentir.imageloader.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.chentir.imageloader.R;
import com.chentir.imageloader.library.ImageLoader;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String[] dataSet = new String[] {
            "https://cdn.pixabay.com/photo/2018/03/10/08/37/swan-3213538_960_720.jpg",
            "https://cdn.pixabay.com/photo/2018/03/12/01/31/bridge-3218539_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/10/17/40/prague-3010407_960_720.jpg",
            "https://cdn.pixabay.com/photo/2018/01/21/19/57/tree-3097419_960_720.jpg",
            "https://cdn.pixabay.com/photo/2018/01/21/19/57/tree-3097419_960_720.jpg",
            "https://cdn.pixabay.com/photo/2018/02/08/22/27/flower-3140492_960_720.jpg",
            "https://cdn.pixabay.com/photo/2018/02/08/22/27/flower-3140492_960_720.jpg",
        };

        adapter = new ImageAdapter(this, dataSet);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        ImageLoader.getInstance(this).cancelAllRequests();
        super.onStop();
    }
}
