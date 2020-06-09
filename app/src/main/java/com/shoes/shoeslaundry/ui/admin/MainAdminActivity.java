package com.shoes.shoeslaundry.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.utils.AlarmReceiver;

public class MainAdminActivity extends AppCompatActivity {

    private AlarmReceiver alarmReceiver;
    private AlarmReceiver alarmReceiverBencana;

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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_admin);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        OrderFragment orderFragment = new OrderFragment();
        fragmentTransaction.replace(R.id.main_frame_admin, orderFragment, "Order Fragment");
        fragmentTransaction.commit();
        setTitle("Order");

//        alarmReceiver = new AlarmReceiver();
//        alarmReceiverBencana = new AlarmReceiver();
//        alarmReceiver.setNewUsersAndBencanaAlarm(this, AlarmReceiver.TYPE_NEWUSER);
//        alarmReceiverBencana.setNewUsersAndBencanaAlarm(this,AlarmReceiver.TYPE_NEWBENCANA);

    }
}
