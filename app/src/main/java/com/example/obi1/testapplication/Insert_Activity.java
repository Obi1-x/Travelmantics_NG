package com.example.obi1.testapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Insert_Activity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase; //Firebase DB object
    private DatabaseReference mDatabaseReference; //Travelmantics_NG DB reference in FB DB
    EditText txtTitle; //Title variable
    EditText txtDescription; //Descip Variable
    EditText txtPrice; //Price Variable
    private TravelDealNG deal; //Travel Deal object container

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseDatabase = FirebaseDatabase.getInstance(); //GET FIREBASE
        mDatabaseReference = mFirebaseDatabase.getReference().child("traveldeals"); //GET TRAVELMANTICS_NG DB
        txtTitle = (EditText) findViewById(R.id.txtTitle); //Title edit text reference
        txtDescription = (EditText) findViewById(R.id.txtDescription); //Descrip edit text reference
        txtPrice = (EditText) findViewById(R.id.txtPrice); //Price edit text reference
        TravelDealNG deal = new TravelDealNG(txtTitle.toString(), txtDescription.toString(), txtPrice.toString()); //RETURN TO THIS LINE LATE!!! //Pass in an instance of TravelDealNG, using its constructor
        //TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Takes a menu resource as an argument
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater(); //Fetches the menu inflater
        inflater.inflate(R.menu.save_menu, menu); // Inflates the menu item
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Takes a menu Item as a parameter
        switch (item.getItemId()) { // Checks for the menu item id that was click
            case R.id.save_menu:
                Toast.makeText(this, "Deal saved", Toast.LENGTH_LONG).show();
                clean(); //Calls Clean method to clear all Edit texts
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clean() {
        txtTitle.setText(""); // Empty the Edit text
        txtPrice.setText("");
        txtDescription.setText("");
        txtTitle.requestFocus(); //Returns the cursor to the title field
    }

    private void saveDeal() {
        String Title = txtTitle.getText().toString(); //Reads content of the 3 Edit texts
        String Price = txtTitle.getText().toString();
        String Description = txtTitle.getText().toString();

        mDatabaseReference.child(deal.getId()).setValue(deal); //Inserts deal Object into Firebase DB
    }
}
