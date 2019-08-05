package com.example.obi1.testapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseClassUtil {
public static FirebaseDatabase myFirebase;
public static DatabaseReference mydatabase;
private static FirebaseClassUtil firebaseUtil; //Reference to firebase
public static ArrayList<TravelDealNG> mDeals;

    private FirebaseClassUtil(){}//Private constructor to avoid this class from getting instantiated

    public static void openFbReference(String ref) { //Generic method that will open the reference of a DB child passed as a parameter
        myFirebase = FirebaseDatabase.getInstance(); //Instance of Firebase DB
        mDeals = new ArrayList<TravelDealNG>(); //New Empty array
        mydatabase = myFirebase.getReference().child(ref); //Opens the path that was passed in as an argument (parameter)
    }
}
