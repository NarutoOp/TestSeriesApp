package com.asg.testseriesapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.asg.testseriesapp.Helpers.DBQuery;
import com.asg.testseriesapp.Helpers.MyCompleteListener;
import com.asg.testseriesapp.databinding.ActivityStartTestBinding;

public class StartTestActivity extends AppCompatActivity {

    ActivityStartTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        DBQuery.loadQuestions(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                setData();
            }

            @Override
            public void onFailure() {
                Toast.makeText(StartTestActivity.this, "Something went wrong please try again. ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setData(){
        binding.stCategoryName.setText(DBQuery.g_categoryList.get(DBQuery.g_selected_cat_index).getCategoryName());
        binding.stTestNo.setText("Test No. " + String.valueOf(DBQuery.g_selected_test_index + 1));
        binding.stTotalQues.setText(String.valueOf(DBQuery.g_questionList.size()));
        binding.stBestScore.setText(String.valueOf(DBQuery.g_testList.get(DBQuery.g_selected_test_index).getTopScore()));
        binding.stTime.setText(String.valueOf(DBQuery.g_testList.get(DBQuery.g_selected_test_index).getTime()));
    }

    private void init(){
        binding.stBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTestActivity.this.finish();
            }
        });

        binding.startTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartTestActivity.this, McqActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}