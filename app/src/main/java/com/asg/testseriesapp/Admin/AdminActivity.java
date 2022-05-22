package com.asg.testseriesapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.asg.testseriesapp.Activities.LoginActivity;
import com.asg.testseriesapp.Helpers.MyCompleteListener;
import com.asg.testseriesapp.databinding.ActivityAdminBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminActivity extends AppCompatActivity {

    ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DBAdmin.g_admin_firestore = FirebaseFirestore.getInstance();

        binding.submitAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = binding.emailBox.getText().toString();
                String b = binding.passwordBox.getText().toString();

                save(a,b);
            }
        });
    }

    private void save(String a,String b){
        DBAdmin.SaveAdminData(a, b, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(AdminActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {
                Toast.makeText(AdminActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}