package com.asg.testseriesapp;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBQuery {
    // Access a Cloud Firestore instance from your Activity
    public static FirebaseFirestore g_firestore;
    public static ArrayList<CategoryModel> g_categoryList = new ArrayList<>();
    public static List<TestModel> g_testList = new ArrayList<>();
    public static int g_selected_cat_index = 0;
    public static int g_selected_test_index = 0;
    public static List<Question> g_questionList = new ArrayList<>();

    public static void createUserData(String email,String name, MyCompleteListener myCompleteListener){

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

    public static void loadCategories(MyCompleteListener completeListener) {
        g_categoryList.clear();

        g_firestore.collection("mcq").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                        {
                            docList.put(doc.getId(), doc);
                        }

                        QueryDocumentSnapshot catListDoc = docList.get("categories");

                        long catCount = catListDoc.getLong("count");

                        for(int i=1; i<=catCount; i++){
                            String catId = catListDoc.getString("cat" + String.valueOf(i) + "Id");
                            QueryDocumentSnapshot catDoc = docList.get(catId);

                            int noOfTest = catDoc.getLong("numberOfTest").intValue();

                            String catName = catDoc.getString("categoryName");

                            String catImage = catDoc.getString("categoryImage");

                            g_categoryList.add(new CategoryModel(catId,catName,catImage,noOfTest));
                        }

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }

    public static void loadTestData(final MyCompleteListener myCompleteListener){
        g_testList.clear();

        String a = g_categoryList.get(g_selected_cat_index).getCategoryId();

        g_firestore.collection("mcq").document(g_categoryList.get(g_selected_cat_index).getCategoryId())
                .collection("testsList").document("testsInfo")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        int noOfTests = g_categoryList.get(g_selected_cat_index).getNoOfTests();

                        for(int i=1; i<= noOfTests; i++){
                            g_testList.add(new TestModel(
                                documentSnapshot.getString("test"+String.valueOf(i)+"Id"),
                                0,
                                documentSnapshot.getLong("test"+String.valueOf(i)+"Time").intValue()
                            ));
                        }

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

    public  static void loadQuestions(MyCompleteListener myCompleteListener)
    {
        g_firestore.collection("questions")
                .whereEqualTo("category",g_categoryList.get(g_selected_cat_index).getCategoryId())
                .whereEqualTo("test",g_testList.get(g_selected_test_index).getTestID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc: queryDocumentSnapshots){
                            g_questionList.add(new Question(
                                    doc.getString("question"),
                                    doc.getString("a"),
                                    doc.getString("b"),
                                    doc.getString("c"),
                                    doc.getString("d"),
                                    doc.getLong("answer").intValue()
                            ));
                        }

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
