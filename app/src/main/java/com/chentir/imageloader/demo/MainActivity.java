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
            "https://i1.wp.com/thebarbellspin.com/wp-content/uploads/2017/05/matfraser-day1-e1495470544390.jpg?resize=696%2C464",
            "https://cdn-s3.si.com/images/mat-fraser-crossfit-training-with-cleans.jpg",
            "https://images.pexels.com/photos/754587/pexels-photo-754587.jpeg?w=940&h=650&dpr=2&auto=compress&cs=tinysrgb",
            "https://pbs.twimg.com/profile_images/912183500031139840/Bvc1VCqy.jpg",
            "https://pbs.twimg.com/profile_images/966036392689176577/ylh8ce9F_400x400.jpg",
            "https://media-exp2.licdn.com/mpr/mpr/shrinknp_200_200/p/2/000/0a3/3da/32bbad3.jpg",
            "https://avatars0.githubusercontent.com/u/593564?s=100&v=4",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
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
