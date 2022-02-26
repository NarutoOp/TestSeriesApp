package com.asg.testseriesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asg.testseriesapp.databinding.ActivityMainBinding;
import com.asg.testseriesapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    FragmentHomeBinding binding;

    public HomeFragment() {
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
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        ArrayList<CategoryModel> categories = new ArrayList<>();
        categories.add(new CategoryModel("","Mathematics","https://www.inventicons.com/uploads/iconset/1298/wm/512/Math-52.png"));
        categories.add(new CategoryModel("","Science",""));
        categories.add(new CategoryModel("","History",""));
        categories.add(new CategoryModel("","Language",""));

        CategoryAdapter adapter = new CategoryAdapter(getContext(),categories);

        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.categoryList.setAdapter(adapter);

        return binding.getRoot();
    }
}