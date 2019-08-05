package com.example.obi1.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealActivity extends AppCompatActivity {
    //FirebaseDatabase mFirebaseDatabase; //Firebase DB object
    //DatabaseReference mDatabaseReference; //Travelmantics_NG DB reference in FB DB
    //private FirebaseDatabase myFirebase;
    FirebaseDatabase myFirebase;
    DatabaseReference mydatabase;
    EditText txtTitle; //Title variable
    EditText txtDescription; //Descip Variable
    EditText txtPrice; //Price Variable
    private TravelDealNG deal; //Travel Deal object container

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  myFirebase = FirebaseDatabase.getInstance(); //GET FIREBASE
      //  mydatabase = myFirebase.getReference().child("traveldeals");
        FirebaseClassUtil.openFbReference("traveldeals"); //Replaces the Firebase reference above
        myFirebase = FirebaseClassUtil.myFirebase;
        mydatabase = FirebaseClassUtil.mydatabase;
        txtTitle = findViewById(R.id.txtTitle); //Title edit text reference
        txtDescription = findViewById(R.id.txtDescription); //Descrip edit text reference
        txtPrice = findViewById(R.id.txtPrice); //Price edit text reference
        deal = new TravelDealNG(txtTitle.toString(), txtDescription.toString(), txtPrice.toString()); //RETURN TO THIS LINE LATE!!! //Pass in an instance of TravelDealNG, using its constructor

        Intent intent = getIntent(); //Receives intent used to start this activity from list activity
        TravelDealNG deal = (TravelDealNG) intent.getSerializableExtra("Deal"); //Extracts the extras
        if(deal == null) { //if the extra is empty
            deal = new TravelDealNG(); //Create a new Travel deal
        }
        this.deal = deal;
        txtTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Takes a menu resource as an argument
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater(); //Fetches the menu inflater
        inflater.inflate(R.menu.save_menu, menu); // Inflates the menu item
        //return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Takes a menu Item as a parameter
        switch (item.getItemId()) { // Checks for the menu item id that was click
            case R.id.save_menu:
                saveDeal(); //Save the deal
                Toast.makeText(this, "Deal saved", Toast.LENGTH_LONG).show();
                clean(); //Calls Clean method to clear all Edit texts
                return true;
            case R.id.delete_menu:
                deleteDeal();//Applies the delete action on click
                Toast.makeText(this, "Deal removed", Toast.LENGTH_LONG).show();
                backToList(); //Sends the user back to list activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clean() {
        txtTitle.setText(""); // Empty the Edit text
        txtPrice.setText("");
        txtDescription.setText("");
        txtTitle.requestFocus(); //Returns the cursor to the title field
    }

    private void deleteDeal() { //For deleting deals
        if (deal == null) { //First checks if a deal exists
            Toast.makeText(this, "Please save the menu before deleting", Toast.LENGTH_LONG).show();
            return;
        }
        mydatabase.child(deal.getId()).removeValue(); //Deletes from DB
    }

    private void backToList() { //Used to return to list activity after saving deals to DB
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    private void saveDeal() {
        deal.setTitle(txtTitle.getText().toString());
        deal.setDescription(txtDescription.getText().toString());
        deal.setPrice(txtPrice.getText().toString());
        if (deal.getId() == null) { //Used for editing DB
            mydatabase.push().setValue(deal); //We need to update a deal if id exists
        }
        else
        {
            mydatabase.child(deal.getId()).setValue(deal);//else its a new one, so push to DB
        }
        //mydatabase.push().setValue(deal);
        //mydatabase.child(deal.getId()).setValue(deal); //Inserts deal Object into Firebase DB
    }
}
