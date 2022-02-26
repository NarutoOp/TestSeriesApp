package com.asg.testseriesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;

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
        setContentView(R.layout.activity_mcq);

        questions = new ArrayList<>();
        Random random = new Random();

    }
}