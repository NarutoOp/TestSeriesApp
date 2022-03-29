package com.asg.testseriesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.asg.testseriesapp.Helpers.DBQuery;
import com.asg.testseriesapp.Helpers.MyCompleteListener;
import com.asg.testseriesapp.R;
import com.asg.testseriesapp.databinding.ActivityMyProfileBinding;

import java.util.Locale;

public class MyProfileActivity extends AppCompatActivity {

    ActivityMyProfileBinding binding;
    ProgressDialog dialog;
    private String nameStr, yearStr, branchStr, semStr;
    String[] sem, years, branch;
    ArrayAdapter<String> semAdapter;

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

        ////

        years = getResources().getStringArray(R.array.years);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                years);
        binding.myProfileYear.setAdapter(yearAdapter);
        binding.myProfileYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                yearStr = binding.myProfileYear.getSelectedItem().toString();

                if(i == 1){
                    sem = getResources().getStringArray(R.array.semesterFirst);
                    semAdapter = new ArrayAdapter<String>(
                            adapterView.getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            sem);
                    binding.myProfileSemester.setAdapter(semAdapter);
                }
                else if(i == 2) {
                    sem = getResources().getStringArray(R.array.semesterSecond);
                    semAdapter = new ArrayAdapter<String>(
                            adapterView.getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            sem);
                    binding.myProfileSemester.setAdapter(semAdapter);
                }
                else if(i==3){
                    sem = getResources().getStringArray(R.array.semesterFinal);
                    semAdapter = new ArrayAdapter<String>(
                            adapterView.getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            sem);
                    binding.myProfileSemester.setAdapter(semAdapter);
                }
                else{
                    sem = new String[]{"Select Semester"};
                    semAdapter = new ArrayAdapter<String>(
                            adapterView.getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            sem);
                    binding.myProfileSemester.setAdapter(semAdapter);
                }

//                semAdapter = new ArrayAdapter<String>(
//                        adapterView.getContext(),
//                        R.layout.dropdown_item,
//                        sem);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        branch = getResources().getStringArray(R.array.branches);
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                branch);
        binding.myProfileBranch.setAdapter(branchAdapter);
        binding.myProfileBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                branchStr = binding.myProfileBranch.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.myProfileSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                semStr = binding.myProfileSemester.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
//        if(phoneStr.isEmpty()){
//            phoneStr = null;
//        }

        DBQuery.saveProfileData(nameStr, yearStr, branchStr, semStr, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                DBQuery.loadData(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MyProfileActivity.this, "Profile updated Succesfully.", Toast.LENGTH_SHORT).show();
                        disableEditing();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(MyProfileActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    }
                });
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
//        phoneStr = binding.myProfilePhone.getText().toString();

        if(nameStr.isEmpty()){
            binding.myProfileName.setError("Name cannot be empty !");
            return false;
        }

//        if (!phoneStr.isEmpty()){
//            if( ! ((phoneStr.length() == 10) && (TextUtils.isDigitsOnly(phoneStr))) ){
//                binding.myProfilePhone.setError("Enter valid phone number !");
//                return false;
//            }
//        }
        return true;
    }

    private void  enableEditing(){
//        binding.myProfileName.setEnabled(true);
//        binding.myProfilePhone.setEnabled(true);
        binding.myProfileYear.setEnabled(true);
        binding.myProfileBranch.setEnabled(true);
        binding.myProfileSemester.setEnabled(true);

        binding.btnLayout.setVisibility(View.VISIBLE);
    }

    private void disableEditing() {
        binding.myProfileName.setEnabled(false);
        binding.myProfileEmail.setEnabled(false);
//        binding.myProfilePhone.setEnabled(false);
        binding.myProfileYear.setEnabled(false);
        binding.myProfileBranch.setEnabled(false);
        binding.myProfileSemester.setEnabled(false);

        binding.btnLayout.setVisibility(View.GONE);

        binding.myProfileName.setText(DBQuery.myProfile.getName());
        binding.myProfileEmail.setText(DBQuery.myProfile.getEmail());


//        binding.myProfileYear.set(DBQuery.myProfile.getYear());
//        binding.myProfileBranch.setText(DBQuery.myProfile.getBranch());
//        binding.myProfileSemester.setText(DBQuery.myProfile.getSemester());

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