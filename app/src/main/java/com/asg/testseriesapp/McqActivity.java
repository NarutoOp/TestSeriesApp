package com.asg.testseriesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewDebug;
import android.widget.TextView;
import android.widget.Toast;

import com.asg.testseriesapp.databinding.ActivityMcqBinding;

import java.util.ArrayList;
import java.util.Random;

public class McqActivity extends AppCompatActivity {

    ActivityMcqBinding binding;

    ArrayList<Question> questions;
    int index = 0;
    Question question;
    CountDownTimer timer;
    int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMcqBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        questions = new ArrayList<>();

        questions.add(new Question("What is math ?","Sub","Algorithm","Formula","Song","Sub"));
        questions.add(new Question("What is a+b ?","Sub","Algorithm","Formula","Song","Formula"));
        questions.add(new Question("What is History ?","Sub","Algorithm","Formula","Song","Sub"));
        questions.add(new Question("What is Bellman Ford?","Sub","Algorithm","Formula","Song","Algorithm"));
        questions.add(new Question("What is ice cream ?","Sub","Algorithm","Formula","Song","Song"));

        resetTimer();
        setNextQuestion();

//        Random random = new Random();

    }

    void resetTimer() {
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {

            }
        };
    }

    void setNextQuestion(){
        if(timer != null)
            timer.cancel();

        timer.start();
        if(index < questions.size()){
            binding.questionCounter.setText(String.format("%d/%d", (index+1), questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());

        }
    }

    void showAnswer() {
        if(question.getAnswer().equals(binding.option1.getText().toString()))
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option2.getText().toString()))
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option3.getText().toString()))
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option4.getText().toString()))
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
    }

    void checkAnswer(TextView textView) {
        String selectedAnswer = textView.getText().toString();
        if(selectedAnswer.equals(question.getAnswer())) {
            correctAnswers++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        } else {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
    }

    void reset() {
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                if (timer != null)
                    timer.cancel();
                TextView selected = (TextView) view;
                checkAnswer(selected);

                break;
            case R.id.nextBtn:
                reset();
                if (index >= questions.size()-1) {
                    Intent intent = new Intent(McqActivity.this, ResultActivity.class);
                    intent.putExtra("correct", correctAnswers);
                    intent.putExtra("total", questions.size());
                    startActivity(intent);
//                    Toast.makeText(this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
                } else {
                    index++;
                    String s = Integer.toString(index);
                    Toast.makeText(this,s, Toast.LENGTH_SHORT).show();
                    setNextQuestion();

                }
                break;
        }

    }
}