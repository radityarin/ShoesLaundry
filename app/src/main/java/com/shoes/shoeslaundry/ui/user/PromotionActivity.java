package com.shoes.shoeslaundry.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.adapter.AdapterPromo;
import com.shoes.shoeslaundry.data.model.Promo;

import java.util.ArrayList;

public class PromotionActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        btn_back = findViewById(R.id.backbutton);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        auth = FirebaseAuth.getInstance();
        getPromotion();
    }

    private void getPromotion() {

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference sewaRef = FirebaseDatabase.getInstance().getReference().child("Promo");
        sewaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Promo> listsewa = new ArrayList<>();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Promo mSewa = dt.getValue(Promo.class);
                        listsewa.add(mSewa);
                }
                recyclerView.setAdapter(new AdapterPromo(listsewa, getApplicationContext(),false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
