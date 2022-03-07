package com.asg.testseriesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.asg.testseriesapp.databinding.ActivityTestBinding;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private RecyclerView tesView;
    private Toolbar toolbar;
//    private List<TestModel> testList;
    private TestAdapter adapter;

    ActivityTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setTitle(DBQuery.g_categoryList.get(DBQuery.g_selected_cat_index).getCategoryName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.testRecyclerView.setLayoutManager(layoutManager);
        
//        loadTestData();

        DBQuery.loadTestData(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                TestAdapter adapter = new TestAdapter(TestActivity.this,DBQuery.g_testList);
                binding.testRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure() {
                Toast.makeText(TestActivity.this, "Something went wrong please try again later ||", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private void loadTestData() {
//        testList = new ArrayList<>();
//
//        testList.add(new TestModel("1",50,20));
//        testList.add(new TestModel("2",70,30));
//        testList.add(new TestModel("3",80,40));
//        testList.add(new TestModel("4",60,50));
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            TestActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}