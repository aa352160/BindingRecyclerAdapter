package com.example.administrator.bindingrecycleradapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this,LinearLayoutManager.VERTICAL,false));
        List<TestModel> datas=new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add(new TestModel("test"+i));
        }
        NewAdapter newAdapter = new NewAdapter(this,datas);
        recyclerView.setAdapter(newAdapter);
    }
}
