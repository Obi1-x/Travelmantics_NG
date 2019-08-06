package com.example.obi1.testapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DealActivity extends AppCompatActivity {
    //FirebaseDatabase mFirebaseDatabase; //Firebase DB object
    //DatabaseReference mDatabaseReference; //Travelmantics_NG DB reference in FB DB
    //private FirebaseDatabase myFirebase;
    FirebaseDatabase myFirebase;
    DatabaseReference mydatabase;
    EditText txtTitle; //Title variable
    EditText txtDescription; //Descip Variable
    EditText txtPrice; //Price Variable
    ImageView imageView;
    private TravelDealNG deal; //Travel Deal object container
    private static final int PICTURE_RESULT = 42; //the answer to everything

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK) { //Checks if the picture was acquired successfully
            Uri imageUri = data.getData(); //Places the file to upload in the uri object. First step to uploading a file to FB CS.
            final StorageReference ref = FirebaseClassUtil.mStorageRef.child(imageUri.getLastPathSegment()); //Gets a reference to the cloud storage
            ref.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() { //Uploads the image
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { //Recieves a tasksnapshot. that the asynchrounous task.
                    String url = ref.getDownloadUrl().toString();
                    String pictureName = taskSnapshot.getStorage().getPath();
                    deal.setImageUrl(url);
                    deal.setImageName(pictureName);
                    Log.d("Url: ", url);
                    Log.d("Name", pictureName);
                    showImage(url);
                }
            }); //This method returns an asynchrous upload task (Upload Task) where the code can listen for a success or failure

        }
    }

    private void showImage(String url) {
        if (url != null && !url.isEmpty()) { //Resizes the image to match the screen width and 2/3 of the image view height, if the url isnt empty
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(this)
                    .load(url)
                    .resize(width, width*2/3)
                    .centerCrop()
                    .into(imageView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);
      //  myFirebase = FirebaseDatabase.getInstance(); //GET FIREBASE
      //  mydatabase = myFirebase.getReference().child("traveldeals");
      //  FirebaseClassUtil.openFbReference("traveldeals"); //Replaces the Firebase reference above
        myFirebase = FirebaseClassUtil.myFirebase;
        mydatabase = FirebaseClassUtil.mydatabase;
        txtTitle = findViewById(R.id.txtTitle); //Title edit text reference
        txtDescription = findViewById(R.id.txtDescription); //Descrip edit text reference
        txtPrice = findViewById(R.id.txtPrice); //Price edit text reference
        imageView = (ImageView) findViewById(R.id.image);
        //deal = new TravelDealNG(txtTitle.toString(), txtDescription.toString(), txtPrice.toString()); //RETURN TO THIS LINE LATE!!! //Pass in an instance of TravelDealNG, using its constructor

        Intent intent = getIntent(); //Receives intent used to start this activity from list activity
        TravelDealNG deal = (TravelDealNG) intent.getSerializableExtra("Deal"); //Extracts the extras
        if(deal == null) { //if the extra is empty
            deal = new TravelDealNG(); //Create a new Travel deal
        }
        this.deal = deal;
        txtTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());
        showImage(deal.getImageUrl());
        Button btnImage = findViewById(R.id.btnImage); //Refernce to the add images button
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // Intent of type get contents  used to the an image resource.
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true); //The source of the image should be local
                startActivityForResult(intent.createChooser(intent,
                        "Insert Picture"), PICTURE_RESULT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //Takes a menu resource as an argument
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater(); //Fetches the menu inflater
        inflater.inflate(R.menu.save_menu, menu); // Inflates the menu item
        if (FirebaseClassUtil.isAdmin) {
            menu.findItem(R.id.delete_menu).setVisible(true); //Activates the delete and save menu for admins only
            menu.findItem(R.id.save_menu).setVisible(true);
            enableEditTexts(true);
            findViewById(R.id.btnImage).setEnabled(true);
        }
        else {
            menu.findItem(R.id.delete_menu).setVisible(false);
            menu.findItem(R.id.save_menu).setVisible(false);
            enableEditTexts(false); //Makes sure the users are unable to write to the edit texts
            findViewById(R.id.btnImage).setEnabled(false);
        }


        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    private void enableEditTexts(boolean isEnabled) {
        txtTitle.setEnabled(isEnabled);
        txtDescription.setEnabled(isEnabled);
        txtPrice.setEnabled(isEnabled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Takes a menu Item as a parameter
        switch (item.getItemId()) { // Checks for the menu item id that was click
            case R.id.save_menu:
                saveDeal(); //Save the deal
                Toast.makeText(this, "Deal saved", Toast.LENGTH_LONG).show();
                clean(); //Calls Clean method to clear all Edit texts
                backToList(); //Sends the user back to list activity
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
        Log.d("image name", deal.getImageName());
        if(deal.getImageName() != null && deal.getImageName().isEmpty() == false) {
            StorageReference picRef = FirebaseClassUtil.mStorage.getReference().child(deal.getImageName());
            picRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Delete Image", "Image Successfully Deleted");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Delete Image", e.getMessage());
                }
            });
        }
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
