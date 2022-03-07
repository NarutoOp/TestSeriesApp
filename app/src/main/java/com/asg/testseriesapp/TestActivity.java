package com.asg.testseriesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.asg.testseriesapp.databinding.ActivityTestBinding;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private RecyclerView tesView;
    private Toolbar toolbar;
    private List<TestModel> testList;

    ActivityTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        int cat_index = getIntent().getIntExtra("CAT_INDEX",0);

        getSupportActionBar().setTitle(DBQuery.g_categoryList.get(cat_index).getCategoryName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.testRecyclerView.setLayoutManager(layoutManager);
        
        loadTestData();

        TestAdapter adapter = new TestAdapter(this,testList);
        binding.testRecyclerView.setAdapter(adapter);

    }

    private void loadTestData() {
        testList = new ArrayList<>();

        testList.add(new TestModel("1",50,20));
        testList.add(new TestModel("2",70,30));
        testList.add(new TestModel("3",80,40));
        testList.add(new TestModel("4",60,50));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            TestActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}