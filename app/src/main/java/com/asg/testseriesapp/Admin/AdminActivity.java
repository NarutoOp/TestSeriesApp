package com.asg.testseriesapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.asg.testseriesapp.Activities.LoginActivity;
import com.asg.testseriesapp.Activities.SignupActivity;
import com.asg.testseriesapp.Helpers.MyCompleteListener;
import com.asg.testseriesapp.R;
import com.asg.testseriesapp.databinding.ActivityAdminBinding;
import com.asg.testseriesapp.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminActivity extends AppCompatActivity {

    ActivityAdminBinding binding;
    FirebaseAuth auth;

    //    FirebaseFirestore database;

    ArrayAdapter<String> semAdapter;
    ProgressDialog dialog;
    int numberOfTest;
    String subjectName, imageLink, selectedYear, selectedBranch, selectedSem;
    String[] sem, years, branch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DBAdmin.g_admin_firestore = FirebaseFirestore.getInstance();

        years = getResources().getStringArray(R.array.years);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(
                this,
                R.layout.dropdown_item,
                years);
        binding.yearId.setAdapter(yearAdapter);
        binding.yearId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedYear = binding.yearId.getSelectedItem().toString();

                if(i == 1){
                    sem = getResources().getStringArray(R.array.semesterFirst);
                    semAdapter = new ArrayAdapter<String>(
                            adapterView.getContext(),
                            R.layout.dropdown_item,
                            sem);
                    binding.semesterId.setAdapter(semAdapter);
                }
                else if(i == 2) {
                    sem = getResources().getStringArray(R.array.semesterSecond);
                    semAdapter = new ArrayAdapter<String>(
                            adapterView.getContext(),
                            R.layout.dropdown_item,
                            sem);
                    binding.semesterId.setAdapter(semAdapter);
                }
                else if(i==3){
                    sem = getResources().getStringArray(R.array.semesterFinal);
                    semAdapter = new ArrayAdapter<String>(
                            adapterView.getContext(),
                            R.layout.dropdown_item,
                            sem);
                    binding.semesterId.setAdapter(semAdapter);
                }
                else{
                    sem = new String[]{"Select Semester"};
                    semAdapter = new ArrayAdapter<String>(
                            adapterView.getContext(),
                            R.layout.dropdown_item,
                            sem);
                    binding.semesterId.setAdapter(semAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        branch = getResources().getStringArray(R.array.branches);
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(
                this,
                R.layout.dropdown_item,
                branch);
        binding.branchId.setAdapter(branchAdapter);
        binding.branchId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedBranch = binding.branchId.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.semesterId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSem = binding.semesterId.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        binding.submitAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subjectName = binding.subjectName.getText().toString();
                imageLink = binding.imageLink.getText().toString();
                numberOfTest = Integer.parseInt(binding.numOfTest.getText().toString());

                if(validate()){
                    save(subjectName,imageLink, numberOfTest, selectedYear, selectedBranch, selectedSem);
                    Toast.makeText(AdminActivity.this, selectedYear+selectedSem+selectedBranch, Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminActivity.this.finish();
            }
        });
    }

    private boolean validate(){
        return true;
    }

    private void save(String subName,String imageLink,int numOfTest, String year, String branch, String sem){

        DBAdmin.SaveAdminData(subName, imageLink, numOfTest, year, branch, sem, new MyCompleteListener() {
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