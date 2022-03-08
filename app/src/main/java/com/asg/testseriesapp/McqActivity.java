package com.asg.testseriesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.asg.testseriesapp.databinding.ActivityMcqBinding;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class McqActivity extends AppCompatActivity {

    ActivityMcqBinding binding;

    List<Question> questions = DBQuery.g_questionList;
    private int questionNum;
    Question question;
//    CountDownTimer timer;
    int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMcqBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        QuestionsAdapter quesAdapter = new QuestionsAdapter(DBQuery.g_questionList);
        binding.questionsView.setAdapter(quesAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.questionsView.setLayoutManager(layoutManager);

        setSnapHelper();

        setClickListeners();

        startTimer();

    }

    private void startTimer() {
        long totalTime = DBQuery.g_testList.get(DBQuery.g_selected_test_index).getTime()*60*1000;

        CountDownTimer timer = new CountDownTimer(totalTime + 1000, 1000) {
            @Override
            public void onTick(long remainingTime) {

                String time = String.format("%02d:%02d min",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))
                );

                binding.timer.setText(time);

            }

            @Override
            public void onFinish() {



            }
        };

        timer.start();
    }

    private void setClickListeners() {
        binding.prevQuesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(questionNum > 0){
                    binding.questionsView.smoothScrollToPosition(questionNum-1);
                }
            }
        });

        binding.nextQuesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(questionNum < DBQuery.g_questionList.size() - 1){
                    binding.questionsView.smoothScrollToPosition(questionNum+1);
                }
            }
        });
    }

    private void init(){
        questionNum = 0;
        binding.questionCounter.setText(String.format("%d/%d", 1, questions.size()));
        binding.mcqCatName.setText(DBQuery.g_categoryList.get(DBQuery.g_selected_cat_index).getCategoryName());
    }

    private void setSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.questionsView);

        binding.questionsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                questionNum = recyclerView.getLayoutManager().getPosition(view);
                binding.questionCounter.setText(String.format("%d/%d", (questionNum+1), questions.size()));

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}