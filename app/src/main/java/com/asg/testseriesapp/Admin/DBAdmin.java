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

    public static String subject_collection = "mcq"; //"test"

    public static String question_collection = "questions"; //"testQuestion"

    public static void AddSubject(String subName,String imageLink,int numOfTest, String year, String branch, String sem, MyCompleteListener myCompleteListener){
        Map<String, Object> userData = new ArrayMap<>();

        if(imageLink.isEmpty()){
            imageLink = "https://www.inventicons.com/uploads/iconset/1298/wm/512/Math-52.png";
        }

        String categoryId = "subject"+ String.valueOf(g_subCount+1);

        userData.put("categoryName", subName);
        userData.put("categoryImage", imageLink);
        userData.put("numberOfTest", numOfTest);
        userData.put("yearId", year);
        userData.put("branchId", branch);
        userData.put("semesterId", sem);
        userData.put("categoryId", categoryId);

        DocumentReference userDoc = g_admin_firestore.collection(subject_collection).document(categoryId);

        WriteBatch batch = g_admin_firestore.batch();
        batch.set(userDoc,userData);

        DocumentReference countDoc = g_admin_firestore.collection(subject_collection).document("categories");
        batch.update(countDoc, "count", FieldValue.increment(1));
        batch.update(countDoc, "cat"+String.valueOf(g_subCount+1)+"Id", categoryId);

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        AddTest(numOfTest, 20, categoryId, myCompleteListener);
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
        g_admin_firestore.collection(subject_collection).document("categories")
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

    public static void SaveAdminData(String subName,String imageLink,int numOfTest, String year, String branch, String sem, MyCompleteListener completeListener)
    {
        GetSubjectCount(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                AddSubject(subName, imageLink, numOfTest, year, branch, sem, completeListener);
            }

            @Override
            public void onFailure() {
                completeListener.onFailure();
            }
        });
    }

    public static void AddTest(int numOfTest, int time, String categoryId, MyCompleteListener myCompleteListener){

        Map<String, Object> testData = new ArrayMap<>();

        for(int i = 1; i <= numOfTest; i++)
        {
            testData.put("test"+String.valueOf(i)+"Id", String.valueOf(i));
            testData.put("test"+String.valueOf(i)+"Time", time);
        }

        DocumentReference testDoc = g_admin_firestore.collection(subject_collection).document(categoryId).collection("testsList").document("testsInfo");

        WriteBatch batch = g_admin_firestore.batch();
        batch.set(testDoc,testData);

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        for(int i=1; i<= numOfTest; i++) {
                            AddQuestion(categoryId, String.valueOf(i),"1+1=?","1", "2", "3", "4", 2, myCompleteListener);
                            AddQuestion(categoryId, String.valueOf(i),"What is Java ?","Instrument", "Sports", "Programming Language", "Number", 3, myCompleteListener);
                            AddQuestion(categoryId, String.valueOf(i),"Capital of India ?","Delhi", "Mumbai", "Kolkata", "Pune", 1, myCompleteListener);
                            AddQuestion(categoryId, String.valueOf(i),"Windows is ?","Hardware", "Operating System", "Programming Language", "Subject", 2, myCompleteListener);
                            AddQuestion(categoryId, String.valueOf(i),"Orange is ?","Monument", "Subject", "OS", "Fruit", 4, myCompleteListener);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();
                    }
                });
    }

    public static void AddQuestion(String categoryId, String testId,String question, String A, String B, String C, String D, int answer, MyCompleteListener myCompleteListener){

        Map<String, Object> quesData = new ArrayMap<>();

        quesData.put("category", categoryId);
        quesData.put("question", question);
        quesData.put("a", A);
        quesData.put("b", B);
        quesData.put("c", C);
        quesData.put("d", D);
        quesData.put("answer", answer);
        quesData.put("test", testId);

        DocumentReference quesDoc = g_admin_firestore.collection(question_collection).document();

        WriteBatch batch = g_admin_firestore.batch();
        batch.set(quesDoc, quesData);

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
}
