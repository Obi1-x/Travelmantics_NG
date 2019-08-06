package com.example.obi1.testapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FirebaseClassUtil {
    public static FirebaseDatabase myFirebase;
    public static DatabaseReference mydatabase;
    public static FirebaseAuth myFirebaseAuth; //FB authentication variable
    public static FirebaseAuth.AuthStateListener myAuthListener; //FB authentication listener variable
    public static FirebaseStorage mStorage;
    public static StorageReference mStorageRef;
    private static FirebaseClassUtil firebaseUtil; //Reference to firebase
    public static ArrayList<TravelDealNG> mDeals;
    private static final int RC_SIGN_IN = 123;
    private static Listactivity caller; // Caller activity variable
    private FirebaseClassUtil(){}//Private constructor to avoid this class from getting instantiated
    public static boolean isAdmin; //Used for admin rights

    public static void openFbReference(String ref, final Listactivity callerActivity) { //Generic method that will open the reference of a DB child passed as a parameter
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseClassUtil();
            myFirebase = FirebaseDatabase.getInstance(); //Instance of Firebase DB
            myFirebaseAuth = FirebaseAuth.getInstance(); //FB auth object initialization
            caller = callerActivity; //Starts the login activity

            myAuthListener = new FirebaseAuth.AuthStateListener() { //FB authstate listener object initialization
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) { //This checks if the current user is signed in
                        FirebaseClassUtil.signIn(); //Signin if not
                    } else {
                        String userId = firebaseAuth.getUid(); //Retrieves the user id of the current user
                        checkAdmin(userId); //Checks if the user is an admin with the acquired id
                    }
                    Toast.makeText(callerActivity.getBaseContext(), "Welcome back!", Toast.LENGTH_LONG).show();
                }
            };
            connectStorage();
        }

        mDeals = new ArrayList<TravelDealNG>(); //New Empty array
        mydatabase = myFirebase.getReference().child(ref); //Opens the path that was passed in as an argument (parameter)
    }

    private static void signIn() { //Sign in method
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }

    private static void checkAdmin(String uid) {
        FirebaseClassUtil.isAdmin=false; //Sets the isAdmin variable to false
        DatabaseReference ref = myFirebase.getReference().child("Admins")
                .child(uid); //Gets a db reference to Admins child of Travelmantics
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { //This method will get trigerred only when a child with the match uid was detected
                FirebaseClassUtil.isAdmin=true; //Sets the isAdmin variable to true
                caller.showMenu();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(listener);
    }

    public static void attachListener() {
        myFirebaseAuth.addAuthStateListener(myAuthListener); //Attach auth statelistener when needed.
    }
    public static void detachListener() {
        myFirebaseAuth.removeAuthStateListener(myAuthListener);//detach auth statelistener when not needed.
    }

    public static void connectStorage() {
        mStorage = FirebaseStorage.getInstance(); //Instance of firebase storage
        mStorageRef = mStorage.getReference().child("deals pictures");
    }
}
