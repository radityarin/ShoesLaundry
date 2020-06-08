package com.shoes.shoeslaundry.ui.generalactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.ui.user.MainActivity;
import com.shoes.shoeslaundry.ui.user.SignUpPage;


public class LandingPage extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();

        Button masuk = findViewById(R.id.buttonmasuk);
        masuk.setOnClickListener(this);

        Button daftar = findViewById(R.id.buttondaftar);
        daftar.setOnClickListener(this);

    }

    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LandingPage.this, MainActivity.class));
            finish();
        }
    }


        @Override
        public void onClick (View v){
            switch (v.getId()) {
                case R.id.buttonmasuk:
                    Intent intent = new Intent(LandingPage.this, LoginPage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case R.id.buttondaftar:
                    Intent intent2 = new Intent(LandingPage.this, SignUpPage.class);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
            }
        }
    }
