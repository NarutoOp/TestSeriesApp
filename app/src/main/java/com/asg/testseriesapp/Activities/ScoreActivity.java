package com.asg.testseriesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.asg.testseriesapp.Fragments.LeaderboardsFragment;
import com.asg.testseriesapp.Helpers.DBQuery;
import com.asg.testseriesapp.Helpers.MyCompleteListener;
import com.asg.testseriesapp.Models.QuestionModel;
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

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Score");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        init();

        loadData();

        setBookmarks();

        binding.viewAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ScoreActivity.this, AnswersActivity.class);
                startActivity(intent);

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

    private void setBookmarks(){
        for(int i=0; i<DBQuery.g_questionList.size(); i++){

            QuestionModel question = DBQuery.g_questionList.get(i);

            if(question.isBookmarked()){
                if( ! DBQuery.g_bookmarkIdList.contains(question.getqID())){
                    DBQuery.g_bookmarkIdList.add(question.getqID());
                    DBQuery.myProfile.setBookmarksCount(DBQuery.g_bookmarkIdList.size());
                }
            }
            else{
                if(DBQuery.g_bookmarkIdList.contains(question.getqID())){
                    DBQuery.g_bookmarkIdList.remove(question.getqID());
                    DBQuery.myProfile.setBookmarksCount(DBQuery.g_bookmarkIdList.size());
                }
            }
        }
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