package com.shoes.shoeslaundry.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.model.User;
import com.shoes.shoeslaundry.ui.generalactivity.LandingPage;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();

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
        finish();
    }
}
