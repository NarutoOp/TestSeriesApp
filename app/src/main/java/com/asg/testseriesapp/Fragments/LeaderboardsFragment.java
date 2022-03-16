package com.asg.testseriesapp.Fragments;

import static com.asg.testseriesapp.Helpers.DBQuery.g_usersCount;
import static com.asg.testseriesapp.Helpers.DBQuery.g_usersList;
import static com.asg.testseriesapp.Helpers.DBQuery.myPerformance;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.asg.testseriesapp.Activities.MainActivity;
import com.asg.testseriesapp.Adapters.RankAdapter;
import com.asg.testseriesapp.Helpers.DBQuery;
import com.asg.testseriesapp.Helpers.MyCompleteListener;
import com.asg.testseriesapp.databinding.FragmentLeaderboardsBinding;

public class LeaderboardsFragment extends Fragment {

    public LeaderboardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentLeaderboardsBinding binding;
    private RankAdapter adapter;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLeaderboardsBinding.inflate(inflater, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("LeaderBoard");

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.usersView.setLayoutManager(layoutManager);

        adapter = new RankAdapter(g_usersList);

        binding.usersView.setAdapter(adapter);

        DBQuery.getTopUsers(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();

                if(DBQuery.myPerformance.getScore() != 0){
                    if( ! DBQuery.isMeOnTopList){
                        calculateRank();
                    }

                    binding.totalScore.setText("Score : " + DBQuery.myPerformance.getScore());
                    binding.rank.setText("Rank - " + DBQuery.myPerformance.getRank());
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "Something went wrong Please try again later !", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        binding.totalUsers.setText("Total Users : " + DBQuery.g_usersCount);

        binding.imgText.setText(myPerformance.getName().toUpperCase().substring(0, 1));

        return binding.getRoot();
    }

    private void calculateRank(){

        int lowTopScore = g_usersList.get(g_usersList.size() - 1).getScore();

        int remainingSlots = g_usersCount - 20;

        int myslot = (myPerformance.getScore()*remainingSlots) / lowTopScore;

        int rank;

        if(lowTopScore != myPerformance.getScore()){
            rank = g_usersCount - myslot;
        }
        else {
           rank = 21;
        }

        myPerformance.setRank(rank);

    }
}