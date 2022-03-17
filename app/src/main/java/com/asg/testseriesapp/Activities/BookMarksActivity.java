package com.asg.testseriesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.asg.testseriesapp.Adapters.AnswersAdapter;
import com.asg.testseriesapp.Adapters.BookmarksAdapter;
import com.asg.testseriesapp.Helpers.DBQuery;
import com.asg.testseriesapp.Helpers.MyCompleteListener;
import com.asg.testseriesapp.databinding.ActivityBookMarksBinding;

public class BookMarksActivity extends AppCompatActivity {

    ActivityBookMarksBinding binding;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookMarksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.bookmarkToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Saved Questions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading ...");
        dialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.bookmarkRecyclerView.setLayoutManager(layoutManager);

        DBQuery.loadBookmarks(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                BookmarksAdapter adapter = new BookmarksAdapter(DBQuery.g_bookmarksList);
                binding.bookmarkRecyclerView.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(BookMarksActivity.this, "Something went wrong ! Please try again later.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            BookMarksActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}