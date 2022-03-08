package com.asg.testseriesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionsViewHolder> {

    private List<Question> questionList;

    public QuestionsAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionsAdapter.QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout,parent,false);
        return new QuestionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsAdapter.QuestionsViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class QuestionsViewHolder extends RecyclerView.ViewHolder {

        private TextView ques, optionA, optionB, optionC, optionD;

        public QuestionsViewHolder(@NonNull View itemView) {
            super(itemView);

            ques = itemView.findViewById(R.id.question);
            optionA = itemView.findViewById(R.id.option_1);
            optionB = itemView.findViewById(R.id.option_2);
            optionC = itemView.findViewById(R.id.option_3);
            optionD = itemView.findViewById(R.id.option_4);

        }

        private void setData(final int pos){
            ques.setText(questionList.get(pos).getQuestion());
            optionA.setText((questionList.get(pos).getOption1()));
            optionB.setText((questionList.get(pos).getOption2()));
            optionC.setText((questionList.get(pos).getOption3()));
            optionD.setText((questionList.get(pos).getOption4()));
        }
    }
}
