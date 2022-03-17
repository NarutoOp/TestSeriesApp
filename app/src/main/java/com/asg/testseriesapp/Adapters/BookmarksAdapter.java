package com.asg.testseriesapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asg.testseriesapp.Models.QuestionModel;
import com.asg.testseriesapp.R;

import java.util.List;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.BookmarksViewHolder> {

    private List<QuestionModel> questionList;

    public BookmarksAdapter(List<QuestionModel> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public BookmarksAdapter.BookmarksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item_layout,parent,false);

        return new BookmarksAdapter.BookmarksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarksAdapter.BookmarksViewHolder holder, int position) {

        String ques = questionList.get(position).getQuestion();
        String a = questionList.get(position).getOption1();
        String b = questionList.get(position).getOption2();
        String c = questionList.get(position).getOption3();
        String d = questionList.get(position).getOption4();
        int answer = questionList.get(position).getAnswer();

        holder.setData(position, ques, a, b, c, d, answer);

    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class BookmarksViewHolder extends RecyclerView.ViewHolder {

        private TextView quesNo, question, optionA, optionB, optionC, optionD, result;

        public BookmarksViewHolder(@NonNull View itemView) {
            super(itemView);

            quesNo = itemView.findViewById(R.id.quesNo);
            question = itemView.findViewById(R.id.question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            result = itemView.findViewById(R.id.result);

        }

        private void setData(int pos, String ques, String a, String b, String c, String d, int correctAns){

            quesNo.setText("Question No. " + String.valueOf(pos+1));
            question.setText(ques);
            optionA.setText("A. "+a);
            optionB.setText("B. "+b);
            optionC.setText("C. "+c);
            optionD.setText("D. "+d);

            if(correctAns == 1){
                result.setText("ANSWER : "+a);
            }
            else if(correctAns == 2){
                result.setText("ANSWER : "+b);
            }
            else if(correctAns == 3){
                result.setText("ANSWER : "+c);
            }
            else{
                result.setText("ANSWER : "+d);
            }

        }

    }
}
