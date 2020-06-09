package com.shoes.shoeslaundry.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.shoes.shoeslaundry.data.model.Promo;
import com.shoes.shoeslaundry.data.model.User;
import com.shoes.shoeslaundry.utils.AlarmReceiver;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView button_notification, button_profile;
    private TextView tv_name, tv_active_order;
    private RecyclerView rv_promo, rv_order;
    private LinearLayout button_track, button_chat, button_promo, button_history;
    private LinearLayout ll_noorder, ll_promo;
    private FloatingActionButton fab_order;

    private ShimmerFrameLayout mShimmerViewContainer;
    private ShimmerFrameLayout mShimmerViewContainer2;

    private FirebaseAuth auth;

    private AlarmReceiver alarmReceiverMessage, alarmReceiverStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer2 = findViewById(R.id.shimmer_view_container2);

        button_notification = findViewById(R.id.buttonnotification);
        button_profile = findViewById(R.id.buttonprofile);
        tv_name = findViewById(R.id.tv_name);
        tv_active_order = findViewById(R.id.tv_active_order);
        rv_promo = findViewById(R.id.recycler_view_promo);
        rv_order = findViewById(R.id.recycler_view_order);
        button_track = findViewById(R.id.buttontrack);
        button_chat = findViewById(R.id.buttonchat);
        button_promo = findViewById(R.id.buttonpromotion);
        button_history = findViewById(R.id.buttonhistory);
        ll_noorder = findViewById(R.id.ll_no_order);
        ll_promo = findViewById(R.id.ll_no_promo);
        fab_order = findViewById(R.id.buttonorder);

        button_notification.setOnClickListener(this);
        button_profile.setOnClickListener(this);
        button_track.setOnClickListener(this);
        button_chat.setOnClickListener(this);
        button_promo.setOnClickListener(this);
        button_history.setOnClickListener(this);
        fab_order.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        getPromotion();
        getActiveOrder();
        displayName();

        setNotification();
    }

    private void setNotification() {
        alarmReceiverMessage = new AlarmReceiver();
        alarmReceiverStatus = new AlarmReceiver();
        alarmReceiverMessage.setAlarm(this, AlarmReceiver.TYPE_NEWMESSAGE);
        alarmReceiverStatus.setAlarm(this,AlarmReceiver.TYPE_NEWSTATUS);
    }

    private void getPromotion() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Promo");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Promo> promotions = new ArrayList<>();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Promo promo = dt.getValue(Promo.class);
                    promotions.add(promo);
                }
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                if (promotions.size() == 0) {
                    ll_promo.setVisibility(View.GONE);
                } else {
                    ll_promo.setVisibility(View.VISIBLE);
                    AdapterPromo adapterBencanaTerdekat = new AdapterPromo(promotions, getApplicationContext(), false);
                    rv_promo.setVisibility(View.VISIBLE);
                    rv_promo.setAdapter(adapterBencanaTerdekat);
                    rv_promo.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                }
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
                    if (Objects.requireNonNull(auth.getUid()).equals(promo.getIduser()) && !promo.getStatus().equals("Pesanan Selesai")) {
                        promotions.add(promo);
                    }
                }

                mShimmerViewContainer2.stopShimmerAnimation();
                mShimmerViewContainer2.setVisibility(View.GONE);
                if (promotions.size() == 0) {
                    ll_noorder.setVisibility(View.VISIBLE);
                } else {
                    tv_active_order.setText("Active Order (" + promotions.size()+")");
                    AdapterOrder adapterBencanaTerdekat = new AdapterOrder(promotions, getApplicationContext(),false);
                    rv_order.setVisibility(View.VISIBLE);
                    rv_order.setAdapter(adapterBencanaTerdekat);
                    rv_order.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayName() {
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
                intentChat.putExtra("name",tv_name.getText().toString());
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
            case R.id.buttonorder:
                Intent intentOrder = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intentOrder);
        }
    }

    @Override
    public void onStart() {
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer2.startShimmerAnimation();
        super.onStart();
    }

    @Override
    public void onResume() {
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer2.startShimmerAnimation();
        super.onResume();
    }

}
