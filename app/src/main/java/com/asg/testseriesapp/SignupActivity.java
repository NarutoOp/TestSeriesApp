package com.asg.testseriesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.asg.testseriesapp.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth auth;
//    FirebaseFirestore database;
    ProgressDialog dialog;
    String email, pass, name, referCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("We're creating new account...");

        binding.createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = binding.emailBox.getText().toString();
                pass = binding.passwordBox.getText().toString();
                name = binding.nameBox.getText().toString();
                referCode = binding.referBox.getText().toString();

//                final User user = new User(name, email, pass, referCode);
                if(validate()){
                    dialog.show();
                    signupNewUser();
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
                                    Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    SignupActivity.this.finish();
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