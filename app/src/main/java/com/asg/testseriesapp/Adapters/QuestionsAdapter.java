package com.asg.testseriesapp.Adapters;

import static com.asg.testseriesapp.Helpers.DBQuery.ANSWERED;
import static com.asg.testseriesapp.Helpers.DBQuery.REVIEW;
import static com.asg.testseriesapp.Helpers.DBQuery.UNANSWERED;
import static com.asg.testseriesapp.Helpers.DBQuery.g_questionList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asg.testseriesapp.Helpers.DBQuery;
import com.asg.testseriesapp.Models.QuestionModel;
import com.asg.testseriesapp.R;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionsViewHolder> {

    private List<QuestionModel> questionList;

    public QuestionsAdapter(List<QuestionModel> questionList) {
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

        private TextView ques, optionA, optionB, optionC, optionD, prevSelectedB;

        public QuestionsViewHolder(@NonNull View itemView) {
            super(itemView);

            ques = itemView.findViewById(R.id.question);
            optionA = itemView.findViewById(R.id.option_1);
            optionB = itemView.findViewById(R.id.option_2);
            optionC = itemView.findViewById(R.id.option_3);
            optionD = itemView.findViewById(R.id.option_4);

            prevSelectedB = null;

        }

        private void setData(final int pos){
            ques.setText(questionList.get(pos).getQuestion());
            optionA.setText((questionList.get(pos).getOption1()));
            optionB.setText((questionList.get(pos).getOption2()));
            optionC.setText((questionList.get(pos).getOption3()));
            optionD.setText((questionList.get(pos).getOption4()));

            setOption(optionA, 1, pos);
            setOption(optionB, 2, pos);
            setOption(optionC, 3, pos);
            setOption(optionD, 4, pos);

            optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selectOption(optionA, 1, pos);

                }
            });

            optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionB, 2, pos);
                }
            });

            optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionC, 3, pos);
                }
            });

            optionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionD, 4, pos);
                }
            });
        }

        private void selectOption(TextView btn, int optionNum, int quesID){

            if(prevSelectedB == null){
                btn.setBackgroundResource(R.drawable.option_selected);
                DBQuery.g_questionList.get(quesID).setSelectedAns(optionNum);

                changeStatus(quesID, ANSWERED);
                prevSelectedB = btn;
            }
            else{
                if(prevSelectedB.getId() == btn.getId()){
                    btn.setBackgroundResource(R.drawable.option_unselected);
                    DBQuery.g_questionList.get(quesID).setSelectedAns(-1);
                    changeStatus(quesID, UNANSWERED);
                    prevSelectedB = null;

                }else {
                    prevSelectedB.setBackgroundResource(R.drawable.option_unselected);
                    btn.setBackgroundResource(R.drawable.option_selected);
                    DBQuery.g_questionList.get(quesID).setSelectedAns(optionNum);
                    changeStatus(quesID, ANSWERED);
                    prevSelectedB = btn;
                }

            }

        }

        private void changeStatus(int id, int status){
            if(g_questionList.get(id).getStatus() != REVIEW){
                g_questionList.get(id).setStatus(status);
            }
        }


        private void setOption(TextView btn, int optionNum, int quesID){
            if( DBQuery.g_questionList.get(quesID).getSelectedAns() == optionNum){
                btn.setBackgroundResource(R.drawable.option_selected);
            }else{
                btn.setBackgroundResource(R.drawable.option_unselected);
            }
        }

    }
}
