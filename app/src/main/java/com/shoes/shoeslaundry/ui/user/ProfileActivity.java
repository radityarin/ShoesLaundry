package com.shoes.shoeslaundry.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.model.User;
import com.shoes.shoeslaundry.ui.common.LandingPage;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_logout, btn_editprofile, btn_back;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        btn_logout = findViewById(R.id.btn_logout);
        btn_editprofile = findViewById(R.id.btn_editprofile);
        btn_back = findViewById(R.id.backbutton);
        btn_logout.setOnClickListener(this);
        btn_editprofile.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        displayProfile();
    }

    private void displayProfile() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(Objects.requireNonNull(auth.getUid()));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User profil = dataSnapshot.getValue(User.class);
                TextView nama = findViewById(R.id.namaprofile);
                TextView email = findViewById(R.id.emailprofile);
                TextView nohp = findViewById(R.id.nohpprofile);
                nama.setText(Objects.requireNonNull(profil).getNamaUser());
                email.setText(profil.getEmailUser());
                nohp.setText(profil.getNomorHP());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LandingPage.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:
                logout();
                break;
            case R.id.btn_editprofile:
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.backbutton:
                onBackPressed();
                break;
        }
    }
}
