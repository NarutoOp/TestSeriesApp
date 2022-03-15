package com.asg.testseriesapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asg.testseriesapp.Helpers.DBQuery;
import com.asg.testseriesapp.Models.TestModel;
import com.asg.testseriesapp.R;
import com.asg.testseriesapp.Activities.StartTestActivity;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private List<TestModel> testList;
    Context context;

    public TestAdapter(Context context, List<TestModel> testList) {
        this.context = context;
        this.testList = testList;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.test_item_layout,parent,false);

        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        TestModel model = testList.get(position);

        holder.testNo.setText("Test No : "+ String.valueOf(position+1));
        holder.topScore.setText(String.valueOf(model.getTopScore())+" %");
        holder.progressBar.setProgress(model.getTopScore());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBQuery.g_selected_test_index = holder.getAdapterPosition();

                Intent intent = new Intent(context, StartTestActivity.class);
//                intent.putExtra("catId", "1");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;
        private TextView testNo,topScore;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            testNo = itemView.findViewById(R.id.testNo);
            progressBar = itemView.findViewById(R.id.testProgressBar);
            topScore = itemView.findViewById(R.id.scoreText);
        }

    }
}
