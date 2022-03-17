package com.asg.testseriesapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asg.testseriesapp.Models.RankModel;
import com.asg.testseriesapp.R;

import java.util.List;
import java.util.Locale;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {

    private List<RankModel> userList;

    public RankAdapter(List<RankModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public RankAdapter.RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item_layout, parent, false);

        return new RankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankAdapter.RankViewHolder holder, int position) {

        String name = userList.get(position).getName();
        int score = userList.get(position).getScore();
        int rank = userList.get(position).getRank();

        holder.setData(name, score, rank);

    }

    @Override
    public int getItemCount() {
        if(userList.size() > 10){
            return 10;
        }
        else{
            return userList.size();
        }
    }

    public class RankViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV, rankTV, scoreTV, imgTV;

        public RankViewHolder(@NonNull View itemView){
            super(itemView);

            nameTV = itemView.findViewById(R.id.name);
            rankTV = itemView.findViewById(R.id.rank);
            scoreTV = itemView.findViewById(R.id.score);
            imgTV = itemView.findViewById(R.id.imgText);
        }

        private void setData(String name, int score, int rank){
            nameTV.setText(name);
            scoreTV.setText("Score : " + score);
            rankTV.setText("Rank - " + rank);
            imgTV.setText(name.toUpperCase().substring(0,1));
        }
    }
}
