package com.chentir.imageloader.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.chentir.imageloader.R;

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
            "https://images.pexels.com/photos/754587/pexels-photo-754587.jpeg?w=940&h=650&dpr=2&auto=compress&cs=tinysrgb",
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
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
            "https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAqgAAAAJDUwMDc3YmJmLTExOTgtNDNkYS05YTljLWNmNDRkMDUwMWEwMg.jpg",
        };

        adapter = new ImageAdapter(this, dataSet);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
