package com.asg.testseriesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.asg.testseriesapp.Helpers.DBQuery;
import com.asg.testseriesapp.Helpers.MyCompleteListener;
import com.asg.testseriesapp.R;
import com.asg.testseriesapp.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth auth;
//    FirebaseFirestore database;
    ArrayAdapter<String> semAdapter;
    ProgressDialog dialog;
    String email, pass, name, selectedYear, selectedBranch, selectedSem;
    String[] sem, years, branch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("We're creating new account...");

        years = getResources().getStringArray(R.array.years);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(
                this,
                R.layout.dropdown_item,
                years);
        binding.year.setAdapter(yearAdapter);
        binding.year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedYear = binding.year.getSelectedItem().toString();

                if(i == 1){
                    sem = getResources().getStringArray(R.array.semesterFirst);
                    semAdapter = new ArrayAdapter<String>(
                            adapterView.getContext(),
                            R.layout.dropdown_item,
                            sem);
                    binding.semester.setAdapter(semAdapter);
                }
                else if(i == 2) {
                    sem = getResources().getStringArray(R.array.semesterSecond);
                    semAdapter = new ArrayAdapter<String>(
                            adapterView.getContext(),
                            R.layout.dropdown_item,
                            sem);
                    binding.semester.setAdapter(semAdapter);
                }
                else if(i==3){
                    sem = getResources().getStringArray(R.array.semesterFinal);
                    semAdapter = new ArrayAdapter<String>(
                            adapterView.getContext(),
                            R.layout.dropdown_item,
                            sem);
                    binding.semester.setAdapter(semAdapter);
                }
                else{
                    sem = new String[]{"Select Semester"};
                    semAdapter = new ArrayAdapter<String>(
                            adapterView.getContext(),
                            R.layout.dropdown_item,
                            sem);
                    binding.semester.setAdapter(semAdapter);
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
                R.layout.dropdown_item,
                branch);
        binding.branch.setAdapter(branchAdapter);
        binding.branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedBranch = binding.branch.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        ArrayAdapter<String> semAdapter = new ArrayAdapter<String>(
//                this,
//                R.layout.dropdown_item,
//                sem);
//        binding.semester.setAdapter(semAdapter);

        binding.semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedSem = binding.semester.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = binding.emailBox.getText().toString();
                pass = binding.passwordBox.getText().toString();
                name = binding.nameBox.getText().toString();

                if(validate()){
                    dialog.show();
//                    signupNewUser();
                    Toast.makeText(SignupActivity.this, selectedYear+selectedSem+selectedBranch, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private boolean validate(){
        if(name.isEmpty()){
            binding.nameBox.setError("Enter your Name");
            return false;
        }
        if(email.isEmpty()){
            binding.emailBox.setError("Enter Email ID");
            return false;
        }
        if(pass.isEmpty()){
            binding.nameBox.setError("Enter Password");
            return false;
        }

        if(selectedYear.equals("Select Year")){
            Toast.makeText(this, "Please select valid year", Toast.LENGTH_SHORT).show();
            binding.year.requestFocus();
            return false;
        }
        if(selectedSem.equals("Select Semester") || selectedSem.isEmpty()){
            Toast.makeText(this, "Please select valid Semester", Toast.LENGTH_SHORT).show();
            binding.semester.requestFocus();
            return false;
        }
        if(selectedBranch.equals("Select Branch")){
            Toast.makeText(this, "Please select valid Branch", Toast.LENGTH_SHORT).show();
            binding.branch.requestFocus();
            return false;
        }

        return true;
    }

    private void signupNewUser(){
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();

                            DBQuery.createUserData(email, name, new MyCompleteListener() {
                                @Override
                                public void onSuccess() {

                                    DBQuery.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}