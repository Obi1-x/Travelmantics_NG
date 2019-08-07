package com.example.obi1.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Listactivity extends AppCompatActivity {

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseClassUtil.detachListener(); // Calls the Detach authlistener method
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseClassUtil.openFbReference("traveldeals", this);//Replaces the Firebase direct reference that was used before
        //ArrayList<TravelDealNG> deals = new ArrayList<TravelDealNG>();
        //FirebaseDatabase myFirebase;
        //DatabaseReference mydatabase;
        //private ChildEventListener mChildListener;
        RecyclerView rvDeals = (RecyclerView) findViewById(R.id.rvDeals);
        final DealAdapter adapter = new DealAdapter(); //Deal Adapter is populated in this onResume method so that RV gets populated after login
        rvDeals.setAdapter(adapter);
        @SuppressLint("WrongConstant") LinearLayoutManager dealsLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false); //BE MINDFUL OF THIS!!!!!!!!!!
        rvDeals.setLayoutManager(dealsLayoutManager);
        FirebaseClassUtil.attachListener(); // Calls the Attach authlistener method
    }

    public void showMenu() {
        invalidateOptionsMenu(); //Used to activate menu options for admins and deactivate for users
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert_menu:  //Was it insert menu item that got clicked?
                Intent intent = new Intent(this, DealActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout_menu:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() { //Once signout is complete
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("Logout", "User Logged Out");
                                FirebaseClassUtil.attachListener();
                            }
                        });
                FirebaseClassUtil.detachListener();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu); //Inflates the insert new deal menu at listactivity layout
        MenuItem insertMenu = menu.findItem(R.id.insert_menu);
        if (FirebaseClassUtil.isAdmin) { //Makes the insert menu vivsible for admins
            insertMenu.setVisible(true);
        }
        else {
            insertMenu.setVisible(false);//and hidden for the user
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listactivity);
        //myFirebase = FirebaseDatabase.getInstance(); //GET FIREBASE
        //mydatabase = myFirebase.getReference().child("traveldeals");
        //myFirebase = FirebaseClassUtil.myFirebase;
        //mydatabase = FirebaseClassUtil.mydatabase;
    }
}
