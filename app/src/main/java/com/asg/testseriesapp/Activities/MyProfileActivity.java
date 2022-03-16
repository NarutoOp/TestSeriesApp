package com.asg.testseriesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.asg.testseriesapp.Helpers.DBQuery;
import com.asg.testseriesapp.Helpers.MyCompleteListener;
import com.asg.testseriesapp.R;
import com.asg.testseriesapp.databinding.ActivityMyProfileBinding;

import java.util.Locale;

public class MyProfileActivity extends AppCompatActivity {

    ActivityMyProfileBinding binding;
    ProgressDialog dialog;
    private String nameStr, phoneStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProfileBinding.inflate(getLayoutInflater());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating Data...");
        
        disableEditing();

        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enableEditing();

            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                disableEditing();

            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    saveData();
                }
            }
        });

        setContentView(binding.getRoot());
    }

    private void saveData(){
        dialog.show();
        if(phoneStr.isEmpty()){
            phoneStr = null;
        }

        DBQuery.saveProfileData(nameStr, phoneStr, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MyProfileActivity.this, "Profile updated Succesfully.", Toast.LENGTH_SHORT).show();
                disableEditing();
                dialog.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(MyProfileActivity.this, "Something went wrong ! Please try again later.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private boolean validate(){
        nameStr = binding.myProfileName.getText().toString();
        phoneStr = binding.myProfilePhone.getText().toString();

        if(nameStr.isEmpty()){
            binding.myProfileName.setError("Name cannot be empty !");
            return false;
        }

        if (!phoneStr.isEmpty()){
            if( ! ((phoneStr.length() == 10) && (TextUtils.isDigitsOnly(phoneStr))) ){
                binding.myProfilePhone.setError("Enter valid phone number !");
                return false;
            }
        }
        return true;
    }

    private void  enableEditing(){
        binding.myProfileName.setEnabled(true);
        binding.myProfilePhone.setEnabled(true);

        binding.btnLayout.setVisibility(View.VISIBLE);
    }

    private void disableEditing() {
        binding.myProfileName.setEnabled(false);
        binding.myProfileEmail.setEnabled(false);
        binding.myProfilePhone.setEnabled(false);

        binding.btnLayout.setVisibility(View.GONE);

        binding.myProfileName.setText(DBQuery.myProfile.getName());
        binding.myProfileEmail.setText(DBQuery.myProfile.getEmail());

        if(DBQuery.myProfile.getPhone() != null){
            binding.myProfilePhone.setText(DBQuery.myProfile.getPhone());
        }

        String profileName = DBQuery.myProfile.getName();

        binding.profileText.setText(profileName.toUpperCase().substring(0,1));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            MyProfileActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}