package com.asg.testseriesapp;

import static com.asg.testseriesapp.DBQuery.ANSWERED;
import static com.asg.testseriesapp.DBQuery.NOT_VISITED;
import static com.asg.testseriesapp.DBQuery.REVIEW;
import static com.asg.testseriesapp.DBQuery.UNANSWERED;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class QuestionGridAdapter extends BaseAdapter {

    private int noOfQues;

    public QuestionGridAdapter(int noOfQues) {
        this.noOfQues = noOfQues;
    }

    @Override
    public int getCount() {
        return noOfQues;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myview;

        if(view == null)
            myview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ques_grid_item,viewGroup,false);
        else
            myview = view;

        myview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

        TextView quesTV = myview.findViewById(R.id.quesNum);
        quesTV.setText(String.valueOf(i+1));

        switch (DBQuery.g_questionList.get(i).getStatus()){
            case NOT_VISITED :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.grey)));
                break;
            case UNANSWERED :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.red)));
                break;
            case ANSWERED :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.green)));
                break;
            case REVIEW :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.pink)));
                break;
        }

        return myview;
    }
}
