package com.shoes.shoeslaundry.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.adapter.AdapterOrder;
import com.shoes.shoeslaundry.data.model.Order;

import java.util.ArrayList;

public class TrackActivity extends AppCompatActivity {
    private ArrayList<Order> listsewa;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        auth = FirebaseAuth.getInstance();
        listsewa = new ArrayList<>();

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference sewaRef = FirebaseDatabase.getInstance().getReference().child("Order");
        sewaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listsewa.clear();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Order mSewa = dt.getValue(Order.class);
                    if (mSewa.getIduser().equals(auth.getUid())) {
                        listsewa.add(mSewa);
                    }
                }
                recyclerView.setAdapter(new AdapterOrder(listsewa,getApplicationContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
