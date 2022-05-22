package com.asg.testseriesapp.Admin;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.asg.testseriesapp.Helpers.MyCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Map;

public class DBAdmin {

    public static FirebaseFirestore g_admin_firestore;
    public static Long g_subCount;

    public static void AddSubject(String email,String name, MyCompleteListener myCompleteListener){
        Map<String, Object> userData = new ArrayMap<>();

        userData.put("email",email);
        userData.put("name", name);

        DocumentReference userDoc = g_admin_firestore.collection("test").document("subject"+ String.valueOf(g_subCount+1));

        WriteBatch batch = g_admin_firestore.batch();
        batch.set(userDoc,userData);

        DocumentReference countDoc = g_admin_firestore.collection("test").document("category");
        batch.update(countDoc, "count", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        myCompleteListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();
                    }
                });
    }

    public static void GetSubjectCount(MyCompleteListener myCompleteListener)
    {
        g_admin_firestore.collection("test").document("category")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        g_subCount = documentSnapshot.getLong("count");
                        myCompleteListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();
                    }
                });
    }

    public static void SaveAdminData(String email,String name, MyCompleteListener completeListener)
    {
        GetSubjectCount(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                AddSubject(email, name, completeListener);
            }

            @Override
            public void onFailure() {
                completeListener.onFailure();
            }
        });
    }
}
