package com.asg.testseriesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.asg.testseriesapp.Adapters.AnswersAdapter;
import com.asg.testseriesapp.Helpers.DBQuery;
import com.asg.testseriesapp.databinding.ActivityAnswersBinding;

public class AnswersActivity extends AppCompatActivity {

    ActivityAnswersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnswersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.answersToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setTitle(DBQuery.g_categoryList.get(DBQuery.g_selected_cat_index).getCategoryName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.answerRecyclerView.setLayoutManager(layoutManager);

        AnswersAdapter adapter = new AnswersAdapter(DBQuery.g_questionList);
        binding.answerRecyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            AnswersActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}