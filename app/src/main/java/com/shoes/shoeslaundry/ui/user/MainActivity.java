package com.shoes.shoeslaundry.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView button_notification, button_profile;
    private TextView tv_name, tv_active_order;
    private RecyclerView rv_order;
    private LinearLayout button_track, button_chat, button_promo, button_history;
    private LinearLayout ll_promo, ll_active_order;
    private FloatingActionButton fab_order;

    private Button btn_saran;
    private EditText edt_saran;

    private ShimmerFrameLayout mShimmerViewContainer;
    private ShimmerFrameLayout mShimmerViewContainer2;

    private FirebaseAuth auth;

    private AlarmReceiver alarmReceiverMessage, alarmReceiverStatus;
    CarouselView customCarouselView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customCarouselView = findViewById(R.id.carouselView);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer2 = findViewById(R.id.shimmer_view_container2);

        button_notification = findViewById(R.id.buttonnotification);
        button_profile = findViewById(R.id.buttonprofile);
        tv_name = findViewById(R.id.tv_name);
        tv_active_order = findViewById(R.id.tv_active_order);
        rv_order = findViewById(R.id.recycler_view_order);
        button_track = findViewById(R.id.buttontrack);
        button_chat = findViewById(R.id.buttonchat);
        button_promo = findViewById(R.id.buttonpromotion);
        button_history = findViewById(R.id.buttonhistory);
        ll_active_order = findViewById(R.id.ll_active_order);
        ll_promo = findViewById(R.id.ll_no_promo);
        fab_order = findViewById(R.id.buttonorder);
        btn_saran = findViewById(R.id.btn_saran);
        edt_saran = findViewById(R.id.edt_saran);

        button_notification.setOnClickListener(this);
        button_profile.setOnClickListener(this);
        button_track.setOnClickListener(this);
        button_chat.setOnClickListener(this);
        button_promo.setOnClickListener(this);
        button_history.setOnClickListener(this);
        fab_order.setOnClickListener(this);
        btn_saran.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        getPromotion();
        getActiveOrder();
        displayName();

        setNotification();
    }

    private void setNotification() {
//        alarmReceiverMessage = new AlarmReceiver();
//        alarmReceiverStatus = new AlarmReceiver();
//        alarmReceiverMessage.setAlarm(this, AlarmReceiver.TYPE_NEWMESSAGE);
//        alarmReceiverStatus.setAlarm(this,AlarmReceiver.TYPE_NEWSTATUS);
    }

    private void getPromotion() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Promo");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Promo> promotions = new ArrayList<>();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Promo promo = dt.getValue(Promo.class);
                    promotions.add(promo);
                }

                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                if (promotions.size() == 0) {
                    ll_promo.setVisibility(View.GONE);
                } else {
                    customCarouselView.setVisibility(View.VISIBLE);
                    ll_promo.setVisibility(View.VISIBLE);
                    customCarouselView.setViewListener(new ViewListener() {
                        @Override
                        public View setViewForPosition(int position) {
                            View customView = getLayoutInflater().inflate(R.layout.item_card_promotion, null);

                            TextView titlepromotion = (TextView) customView.findViewById(R.id.titlepromotion);
                            ImageView photopromotion = (ImageView) customView.findViewById(R.id.photopromotion);
                            Glide.with(getApplicationContext()).load(promotions.get(position).getPhoto()).into(photopromotion);

                            titlepromotion.setText(promotions.get(position).getTitle());

                            customCarouselView.setIndicatorGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);

                            return customView;
                        }
                    });
                    customCarouselView.setPageCount(promotions.size());
                    customCarouselView.setImageClickListener(new ImageClickListener() {
                        @Override
                        public void onClick(int position) {
                            Intent intent = new Intent(MainActivity.this, PromotionDetailActivity.class);
                            intent.putExtra("promo", promotions.get(position));
                            startActivity(intent);
                        }
                    });
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
                ArrayList<Order> promotions2 = new ArrayList<>();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Order promo = dt.getValue(Order.class);
                    if (Objects.requireNonNull(auth.getUid()).equals(promo.getIduser()) && !promo.getStatus().equals("Pesanan Selesai")) {
                        promotions.add(promo);
                    }
                }
                if (promotions.size() > 0) {
                    promotions2.add(promotions.get(0));
                }
                mShimmerViewContainer2.stopShimmerAnimation();
                mShimmerViewContainer2.setVisibility(View.GONE);
                if (promotions.size() == 0) {
//                    ll_noorder.setVisibility(View.VISIBLE);
                    ll_active_order.setVisibility(View.GONE);
                } else {
                    tv_active_order.setText("Active Order (" + promotions.size() + ")");
                    AdapterOrder adapterBencanaTerdekat = new AdapterOrder(promotions2, getApplicationContext(), false);
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
                intentChat.putExtra("name", tv_name.getText().toString());
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
                break;
            case R.id.btn_saran:
                kirimSaran();
                break;
        }
    }

    private void kirimSaran() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Saran");
        databaseReference.push().setValue(edt_saran.getText().toString());
        Toast.makeText(this, "Terima kasih telah memberi saran kepada kami", Toast.LENGTH_LONG).show();
        edt_saran.setText("");
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
