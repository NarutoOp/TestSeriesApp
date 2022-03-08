package com.asg.testseriesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asg.testseriesapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    FragmentHomeBinding binding;

//    public static ArrayList<CategoryModel> categories = new ArrayList<>();

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

//        loadCategories();

        CategoryAdapter adapter = new CategoryAdapter(getContext(), DBQuery.g_categoryList);

        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.categoryList.setAdapter(adapter);

        return binding.getRoot();
    }

//    private void loadCategories() {
//        categories.clear();
//
//        categories.add(new CategoryModel("","Mathematics","https://www.inventicons.com/uploads/iconset/1298/wm/512/Math-52.png",2));
//        categories.add(new CategoryModel("","Science","https://www.inventicons.com/uploads/iconset/1298/wm/512/Math-52.png", 3));
//        categories.add(new CategoryModel("","History", "https://www.inventicons.com/uploads/iconset/1298/wm/512/Math-52.png",4));
//        categories.add(new CategoryModel("","Language", "",1));
//
//    }
}