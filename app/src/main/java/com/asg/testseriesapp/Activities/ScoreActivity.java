package com.asg.testseriesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.asg.testseriesapp.Helpers.DBQuery;
import com.asg.testseriesapp.Helpers.MyCompleteListener;
import com.asg.testseriesapp.databinding.ActivityScoreBinding;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {

    ActivityScoreBinding binding;
    private long timeTaken;
    private int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        init();

        loadData();

        binding.viewAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.reAttemptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reAttempt();
            }
        });

        saveResult();
    }

    private void loadData(){
        int correctQ = 0, wrongQ = 0, unAttemptQ = 0;

        for(int i = 0; i< DBQuery.g_questionList.size(); i++){
            if(DBQuery.g_questionList.get(i).getSelectedAns() == -1)
                unAttemptQ++;
            else{
                if(DBQuery.g_questionList.get(i).getSelectedAns() == DBQuery.g_questionList.get(i).getAnswer())
                    correctQ++;
                else
                    wrongQ++;
            }
        }

        binding.correctQuestion.setText(String.valueOf(correctQ));
        binding.wrongQuestion.setText(String.valueOf(wrongQ));
        binding.unAttempted.setText(String.valueOf(unAttemptQ));

        binding.totalQuestions.setText(String.valueOf(DBQuery.g_questionList.size()));
        finalScore = (correctQ*100)/DBQuery.g_questionList.size();
        binding.score.setText(String.valueOf(finalScore));

        timeTaken = getIntent().getLongExtra("TIME_TAKEN", 0);

        String time = String.format("%02d:%02d min",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken))
        );

        binding.timeTaken.setText(time);
    }

    private void reAttempt(){
        for(int i = 0; i<DBQuery.g_questionList.size(); i++){
            DBQuery.g_questionList.get(i).setSelectedAns(-1);
            DBQuery.g_questionList.get(i).setStatus(DBQuery.NOT_VISITED);
        }

        Intent intent = new Intent(ScoreActivity.this, StartTestActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveResult(){
        DBQuery.saveResult(finalScore, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                
            }

            @Override
            public void onFailure() {
                Toast.makeText(ScoreActivity.this, "Something went wrong ! Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            ScoreActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}