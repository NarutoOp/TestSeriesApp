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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.asg.testseriesapp.databinding.ActivityMcqBinding;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class McqActivity extends AppCompatActivity {


    List<Question> questions = DBQuery.g_questionList;
    private int questionNum;
    QuestionsAdapter quesAdapter;
    private DrawerLayout drawerLayout;
    private ImageButton drawerCloseBtn;
    private RecyclerView questionsView;
    private TextView timerTV, mcqCatName, questionCounter;
    private ImageView qa_bookmark, quesListGridBtn, prevQuesBtn, nextQuesBtn;
    private Button markBtn, clearSelection, submitTestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_list_layout);

        init();

        quesAdapter = new QuestionsAdapter(DBQuery.g_questionList);
        questionsView.setAdapter(quesAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionsView.setLayoutManager(layoutManager);

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

                timerTV.setText(time);

            }

            @Override
            public void onFinish() {



            }
        };

        timer.start();
    }

    private void setClickListeners() {
        prevQuesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(questionNum > 0){
                    questionsView.smoothScrollToPosition(questionNum-1);
                }
            }
        });

        nextQuesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(questionNum < DBQuery.g_questionList.size() - 1){
                    questionsView.smoothScrollToPosition(questionNum+1);
                }
            }
        });

        clearSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBQuery.g_questionList.get(questionNum).setSelectedAns(-1);

                quesAdapter.notifyDataSetChanged();
            }
        });

        quesListGridBtn.setOnClickListener(new View.OnClickListener() {
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

        drawerLayout = findViewById(R.id.drawerLayout);
        drawerCloseBtn = findViewById(R.id.drawerClose);
        questionsView = findViewById(R.id.questionsView);
        timerTV = findViewById(R.id.timer);
        mcqCatName = findViewById(R.id.mcqCatName);
        questionCounter = findViewById(R.id.questionCounter);
        qa_bookmark = findViewById(R.id.qa_bookmark);
        quesListGridBtn = findViewById(R.id.quesListGridBtn);
        prevQuesBtn = findViewById(R.id.prevQuesBtn);
        markBtn = findViewById(R.id.markBtn);
        clearSelection = findViewById(R.id.clearSelection);
        submitTestBtn = findViewById(R.id.submitTestBtn);
        nextQuesBtn = findViewById(R.id.nextQuesBtn);


        questionNum = 0;
        questionCounter.setText(String.format("%d/%d", 1, questions.size()));
        mcqCatName.setText(DBQuery.g_categoryList.get(DBQuery.g_selected_cat_index).getCategoryName());


    }

    private void setSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionsView);

        questionsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                questionNum = recyclerView.getLayoutManager().getPosition(view);
                questionCounter.setText(String.format("%d/%d", (questionNum+1), questions.size()));

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}