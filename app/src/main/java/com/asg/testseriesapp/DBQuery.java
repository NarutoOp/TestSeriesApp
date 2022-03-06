package com.asg.testseriesapp;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Map;

public class DBQuery {
    // Access a Cloud Firestore instance from your Activity
    public static FirebaseFirestore g_firestore;

    public static void createUserData(String email,String name){

        Map<String, Object> userData = new ArrayMap<>();

        userData.put("email",email);
        userData.put("name", name);
        userData.put("totalScore",0);

        DocumentReference userDoc = g_firestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = g_firestore.batch();
        batch.set(userDoc,userData);

        DocumentReference countDoc = g_firestore.collection("users").document("totalUsers");
        batch.update(countDoc, "count", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

}
