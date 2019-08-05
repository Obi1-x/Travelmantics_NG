package com.example.obi1.testapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Listactivity extends AppCompatActivity {
    ArrayList<TravelDealNG> deals = new ArrayList<TravelDealNG>();
    FirebaseDatabase myFirebase;
    DatabaseReference mydatabase;
    private RecyclerView mRVDeals;
    //private ChildEventListener mChildListener;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert_menu:  //Was it insert menu item that got clicked?
                Intent intent = new Intent(this, DealActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu); //Inflates the insert new deal menu at listactivity layout
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listactivity);
        //myFirebase = FirebaseDatabase.getInstance(); //GET FIREBASE
        //mydatabase = myFirebase.getReference().child("traveldeals");
        FirebaseClassUtil.openFbReference("traveldeals"); //Replaces the Firebase reference above
        myFirebase = FirebaseClassUtil.myFirebase;
        mydatabase = FirebaseClassUtil.mydatabase;

        mRVDeals = (RecyclerView) findViewById(R.id.rvDeals); // Recycler view object
        final DealAdapter adapter = new DealAdapter();
        mRVDeals.setAdapter(adapter);
        @SuppressLint("WrongConstant") LinearLayoutManager dealsLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false); //BE MINDFUL OF THIS!!!!!!!!!!
        mRVDeals.setLayoutManager(dealsLayoutManager);

  /*      mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { //All Childs in DB will call this method
                TextView tvDeals = (TextView) findViewById(R.id.tvDeals); //Textview reference
                TravelDealNG td = dataSnapshot.getValue(TravelDealNG.class); //Populatess the List using the datasnapshot passed by to the method
                tvDeals.setText(td.getTitle()); //Sets texts on textview after receiving
                Log.d("Deal: ", td.getTitle());
                mydatabase.addChildEventListener(mChildListener);
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
        };*/
    }
}
