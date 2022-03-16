package com.asg.testseriesapp.Helpers;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.asg.testseriesapp.Models.CategoryModel;
import com.asg.testseriesapp.Models.ProfileModel;
import com.asg.testseriesapp.Models.QuestionModel;
import com.asg.testseriesapp.Models.RankModel;
import com.asg.testseriesapp.Models.TestModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
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
    public static List<QuestionModel> g_questionList = new ArrayList<>();

    public static RankModel myPerformance = new RankModel("NULL", 0, -1);

    public static ProfileModel myProfile = new ProfileModel("NA", null, null);

    public static List<RankModel> g_usersList = new ArrayList<>();
    public static int g_usersCount = 0;
    public static boolean isMeOnTopList = false;

    public static final int NOT_VISITED = 0;
    public static final int UNANSWERED = 1;
    public static final int ANSWERED = 2;
    public static final int REVIEW = 3;


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

    public  static void loadMyScores(MyCompleteListener completeListener){
        g_firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                .collection("userData").document("myScores")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for(int i=0; i<g_testList.size(); i++){
                            int top=0;
                            if(documentSnapshot.get(g_testList.get(i).getTestID()) != null){
                                top = documentSnapshot.getLong(g_testList.get(i).getTestID()).intValue();
                            }

                            g_testList.get(i).setTopScore(top);
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

    public static void saveResult(int score, MyCompleteListener myCompleteListener) {
        WriteBatch batch = g_firestore.batch();

        DocumentReference userDoc = g_firestore.collection("users").document(FirebaseAuth.getInstance().getUid());

        batch.update(userDoc, "totalScore", score);

        if(score > g_testList.get(g_selected_test_index).getTopScore()){
            DocumentReference scoreDoc = userDoc.collection("userData").document("myScores");

            Map<String,Object> testData = new ArrayMap<>();
            testData.put(g_testList.get(g_selected_test_index).getTestID(), score);

            batch.set(scoreDoc, testData, SetOptions.merge());

        }

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(score > g_testList.get(g_selected_test_index).getTopScore())
                            g_testList.get(g_selected_test_index).setTopScore(score);

                        myPerformance.setScore(score);

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
        g_questionList.clear();

        g_firestore.collection("questions")
                .whereEqualTo("category",g_categoryList.get(g_selected_cat_index).getCategoryId())
                .whereEqualTo("test",g_testList.get(g_selected_test_index).getTestID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc: queryDocumentSnapshots){
                            g_questionList.add(new QuestionModel(
                                    doc.getString("question"),
                                    doc.getString("a"),
                                    doc.getString("b"),
                                    doc.getString("c"),
                                    doc.getString("d"),
                                    doc.getLong("answer").intValue(),
                                    -1,
                                    NOT_VISITED
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

    public static void loadData(final MyCompleteListener completeListener){
        loadCategories(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                getUserData(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        getUsersCount(completeListener);
                    }

                    @Override
                    public void onFailure() {
                        completeListener.onFailure();
                    }
                });
            }

            @Override
            public void onFailure() {
                completeListener.onFailure();
            }
        });
    }

    public static void saveProfileData(String name, String phone, MyCompleteListener completeListener){
        Map<String, Object> profileData = new ArrayMap<>();
        profileData.put("name", name);

        if(phone != null){
            profileData.put("phone", phone);
        }

        g_firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                .update(profileData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        myProfile.setName(name);
                        if(phone!=null){
                            myProfile.setPhone(phone);
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

    public static void getUserData(final MyCompleteListener completeListener){
        g_firestore.collection("users").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        myProfile.setName(documentSnapshot.getString("name"));
                        myProfile.setEmail(documentSnapshot.getString("email"));

                        if(documentSnapshot.getString("phone") != null){
                            myProfile.setPhone(documentSnapshot.getString("phone"));
                        }

                        myPerformance.setName(documentSnapshot.getString("name"));
                        myPerformance.setScore(documentSnapshot.getLong("totalScore").intValue());

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

    public static void getTopUsers(MyCompleteListener completeListener){
        g_usersList.clear();

        String myUID = FirebaseAuth.getInstance().getUid();

        g_firestore.collection("users")
                .whereGreaterThan("totalScore", 0)
                .orderBy("totalScore", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        int rank = 1;
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                            g_usersList.add(new RankModel(
                                    doc.getString("name"),
                                    doc.getLong("totalScore").intValue(),
                                    rank
                            ));

                            if(myUID.compareTo(doc.getId()) == 0){
                                isMeOnTopList = true;
                                myPerformance.setRank(rank);
                            }

                            rank++;

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

    public static void getUsersCount(MyCompleteListener completeListener){
        g_firestore.collection("users").document("totalUsers")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        g_usersCount = documentSnapshot.getLong("count").intValue();
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

}
