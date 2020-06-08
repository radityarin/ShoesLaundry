package com.shoes.shoeslaundry.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.adapter.AdapterOrder;
import com.shoes.shoeslaundry.data.adapter.AdapterPromo;
import com.shoes.shoeslaundry.data.model.Order;
import com.shoes.shoeslaundry.data.model.Promotion;
import com.shoes.shoeslaundry.data.model.User;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView button_notification, button_profile;
    private TextView tv_name;
    private RecyclerView rv_promo, rv_order;
    private LinearLayout button_track, button_chat, button_promo, button_history;
    private Button button_order_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_notification = findViewById(R.id.buttonnotification);
        button_profile = findViewById(R.id.buttonprofile);
        tv_name = findViewById(R.id.tv_name);
        rv_promo = findViewById(R.id.recycler_view_promo);
        rv_order = findViewById(R.id.recycler_view_order);
        button_track = findViewById(R.id.buttontrack);
        button_chat = findViewById(R.id.buttonchat);
        button_promo = findViewById(R.id.buttonpromotion);
        button_history = findViewById(R.id.buttonhistory);
//        button_order_history = findViewById(R.id.buttonorderhistory);

        button_notification.setOnClickListener(this);
        button_profile.setOnClickListener(this);
        button_track.setOnClickListener(this);
        button_chat.setOnClickListener(this);
        button_promo.setOnClickListener(this);
        button_history.setOnClickListener(this);

        getPromotion();
        getActiveOrder();
        displayName();
    }

    private void getPromotion() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Promo");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Promotion> promotions = new ArrayList<>();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Promotion promo = dt.getValue(Promotion.class);
                    promotions.add(promo);
                }
                AdapterPromo adapterBencanaTerdekat = new AdapterPromo(promotions, getApplicationContext());
                rv_promo.setAdapter(adapterBencanaTerdekat);
                rv_promo.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getActiveOrder() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Order");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Order> promotions = new ArrayList<>();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Order promo = dt.getValue(Order.class);
                    promotions.add(promo);
                }
                AdapterOrder adapterBencanaTerdekat = new AdapterOrder(promotions, getApplicationContext());
                rv_order.setAdapter(adapterBencanaTerdekat);
                rv_order.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayName() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(Objects.requireNonNull(auth.getUid()));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User profil = dataSnapshot.getValue(User.class);
                tv_name.setText(Objects.requireNonNull(profil).getNamaUser());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonnotification:
                break;
            case R.id.buttonprofile:
                Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
                break;
            case R.id.buttontrack:
                Intent intentTrack = new Intent(MainActivity.this, TrackActivity.class);
                startActivity(intentTrack);
                break;
            case R.id.buttonchat:
                Intent intentChat = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intentChat);
                break;
            case R.id.buttonpromotion:
                Intent intentPromotion = new Intent(MainActivity.this, PromotionActivity.class);
                startActivity(intentPromotion);
                break;
            case R.id.buttonhistory:
                Intent intentLocation = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intentLocation);
                break;
        }
    }
}
