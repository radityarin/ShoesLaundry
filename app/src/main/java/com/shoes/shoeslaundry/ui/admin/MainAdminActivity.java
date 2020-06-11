package com.shoes.shoeslaundry.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.ui.common.LandingPage;
import com.shoes.shoeslaundry.ui.user.MainActivity;
import com.shoes.shoeslaundry.utils.AlarmReceiver;
import com.shoes.shoeslaundry.utils.notifications.Token;

public class MainAdminActivity extends AppCompatActivity {

    private AlarmReceiver alarmReceiver;
    private AlarmReceiver alarmReceiverBencana;
    String mUID;
    FirebaseAuth auth;

    private final BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.orderbutton:
                    OrderFragment orderFragment = new OrderFragment();
                    fragmentTransaction.replace(R.id.main_frame_admin, orderFragment, "Order Fragment");
                    fragmentTransaction.commit();
                    setTitle("Order");
                    return true;
                case R.id.promobutton:
                    PromotionFragment promotionFragment = new PromotionFragment();
                    fragmentTransaction.replace(R.id.main_frame_admin, promotionFragment, "Promotion Fragment");
                    fragmentTransaction.commit();
                    setTitle("Promotion");
                    return true;
                case R.id.chatbutton:
                    ChatFragment chatFragment = new ChatFragment();
                    fragmentTransaction.replace(R.id.main_frame_admin, chatFragment, "Chat Fragment");
                    fragmentTransaction.commit();
                    setTitle("Chat");
                    return true;
                case R.id.riwayatbutton:
                    HistoryFragment historyFragment = new HistoryFragment();
                    fragmentTransaction.replace(R.id.main_frame_admin, historyFragment, "History Fragment");
                    fragmentTransaction.commit();
                    setTitle("History");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        auth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_admin);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        OrderFragment orderFragment = new OrderFragment();
        fragmentTransaction.replace(R.id.main_frame_admin, orderFragment, "Order Fragment");
        fragmentTransaction.commit();
        setTitle("Order");

        checkUserStatus();

//        setNotification();
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void updateToken(String token) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken = new Token(token);
        databaseReference.child(mUID).setValue(mToken);
    }

    private void checkUserStatus(){
        FirebaseUser user = auth.getCurrentUser();
        if(user!= null){
            mUID = user.getUid();

            SharedPreferences sp = getSharedPreferences("SP_USER",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Current_USERID",mUID);
            editor.apply();
        } else {
            startActivity(new Intent(MainAdminActivity.this, LandingPage.class));
            finish();
        }
    }
    @Override
    public void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    public void onResume() {
        checkUserStatus();
        super.onResume();
    }
}
