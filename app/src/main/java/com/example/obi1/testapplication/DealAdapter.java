package com.example.obi1.testapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {
    ArrayList<TravelDealNG> deals;
    private FirebaseDatabase myFirebase;
    private DatabaseReference mydatabase;
    private ChildEventListener mChildListener;

    public DealAdapter() {
        FirebaseClassUtil.openFbReference("traveldeals"); //Replaces the Firebase reference above
        myFirebase = FirebaseClassUtil.myFirebase;
        mydatabase = FirebaseClassUtil.mydatabase;
        deals = FirebaseClassUtil.mDeals; //Will be used to populate array list
        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDealNG td = dataSnapshot.getValue(TravelDealNG.class); //Populatess the List using the datasnapshot passed by to the method
                td.setId(dataSnapshot.getKey()); //Sets id os deal to the pushid generated by FB usignd the get key method
                Log.d("Deal: ", td.getTitle());
                deals.add(td); //Adds td to the deals array
                notifyItemInserted(deals.size()-1); // This notifies the UI that an item has been added, so it can update and display it
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
        mydatabase.addChildEventListener(mChildListener);
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.rv_row, parent, false);
        return new DealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DealAdapter.DealViewHolder holder, int position) {
        TravelDealNG deal = deals.get(position); // Gets the deal at position and binds it to the holder
        holder.bind(deal);
    }

    @Override
    public int getItemCount() { //Counts data on the array list and returns it size
        return deals.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { //To populate recycler view
        TextView tvTitle;
        TextView tvDescription;
        TextView tvPrice;

        public DealViewHolder(View itemView) { //Describes how to bind data to a single RV row
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle); //Reference to Title TV
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription); //Reference to Description TV
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice); //Reference to Price TV
            itemView.setOnClickListener(this); //Sets Onclick listener to deal item
        }

        public void bind (TravelDealNG deal) { //Binds data to each RV row
            tvTitle.setText(deal.getTitle());
            tvDescription.setText(deal.getDescription());
            tvPrice.setText(deal.getPrice());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); //Get position of item that clicked
            Log.d("Click", String.valueOf(position));
            TravelDealNG selectedDeal = deals.get(position); //Find selected travel deal through its position. It calls the get method of the array List
            Intent intent = new Intent(view.getContext(), DealActivity.class);
            intent.putExtra("Deal", selectedDeal);
            view.getContext().startActivity(intent);
        }
    }
}