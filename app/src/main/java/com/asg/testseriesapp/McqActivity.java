package com.asg.testseriesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;

import com.asg.testseriesapp.databinding.ActivityMcqBinding;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class McqActivity extends AppCompatActivity {

    ActivityMcqBinding binding;

    List<Question> questions = DBQuery.g_questionList;
    private int questionNum;
    QuestionsAdapter quesAdapter;
    private DrawerLayout drawerLayout;
    private ImageButton drawerCloseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMcqBinding.inflate(getLayoutInflater());
        setContentView(R.layout.questions_list_layout);

        init();

        quesAdapter = new QuestionsAdapter(DBQuery.g_questionList);
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

        binding.clearSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBQuery.g_questionList.get(questionNum).setSelectedAns(-1);

                quesAdapter.notifyDataSetChanged();
            }
        });

        binding.quesListGridBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        drawerCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });
    }

    private void init(){
        questionNum = 0;
        binding.questionCounter.setText(String.format("%d/%d", 1, questions.size()));
        binding.mcqCatName.setText(DBQuery.g_categoryList.get(DBQuery.g_selected_cat_index).getCategoryName());
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerCloseBtn = findViewById(R.id.drawerClose);

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