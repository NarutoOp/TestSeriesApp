package com.asg.testseriesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asg.testseriesapp.Activities.TestActivity;
import com.asg.testseriesapp.Helpers.DBQuery;
import com.asg.testseriesapp.Models.CategoryModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<CategoryModel> categoryModels;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category,null);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final CategoryModel model = categoryModels.get(position);

        holder.textView1.setText(model.getCategoryName());
        holder.textView2.setText(String.valueOf(model.getNoOfTests()+" Tests"));
        Glide.with(context)
                .load(model.getCategoryImage())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, McqActivity.class);
//                intent.putExtra("catId", model.getCategoryId());
//                context.startActivity(intent);
                DBQuery.g_selected_cat_index = holder.getAdapterPosition();
                Intent intent = new Intent(context, TestActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1,textView2;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView1 = itemView.findViewById(R.id.categoryName);
            textView2 = itemView.findViewById(R.id.noOfTests);
        }
    }
}

