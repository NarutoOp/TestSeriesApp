package com.asg.testseriesapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.asg.testseriesapp.Activities.LoginActivity;
import com.asg.testseriesapp.Activities.MainActivity;
import com.asg.testseriesapp.Helpers.DBQuery;
import com.asg.testseriesapp.R;
import com.asg.testseriesapp.databinding.FragmentAccountBinding;
import com.asg.testseriesapp.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import androidx.fragment.app.FragmentTransaction;

public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;

    private me.ibrahimsn.lib.SmoothBottomBar bottomNavigationView;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        bottomNavigationView = getActivity().findViewById(R.id.bottomBar);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("My Account");

        String userName = DBQuery.myProfile.getName();
        binding.profileImageText.setText(userName.toUpperCase().substring(0,1));

        binding.name.setText(userName);

        binding.totalScore.setText(String.valueOf(DBQuery.myPerformance.getScore()));

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                getActivity().finish();
            }
        });

        binding.bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.leaderboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bottomNavigationView.setSelectedItemId(R.id.rank);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content, new LeaderboardsFragment());
                transaction.commit();
                bottomNavigationView.setItemActiveIndex(1);
            }
        });

        return binding.getRoot();
    }
}