package com.shoes.shoeslaundry.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.model.User;

import java.util.Objects;

import static android.content.ContentValues.TAG;


public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth auth;
    private EditText inputNama, inputNOHP;
    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Button btnBack = findViewById(R.id.backbutton);
        btnBack.setOnClickListener(view -> finish());

        auth = FirebaseAuth.getInstance();

        inputNama = findViewById(R.id.nama);
        inputNOHP = findViewById(R.id.noHp);

        displayProfile();

        Button btnDaftar = findViewById(R.id.buttondaftar);
        btnDaftar.setOnClickListener(this);

    }

    private void displayProfile() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(Objects.requireNonNull(auth.getUid()));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User profil = dataSnapshot.getValue(User.class);
                inputNama.setText(Objects.requireNonNull(profil).getNamaUser());
                inputNOHP.setText(profil.getNomorHP());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void update() {
        final String nama = inputNama.getText().toString();
        final String nohp = inputNOHP.getText().toString();
        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        PD.show();

        if (nama.length() > 0 && nohp.length() > 0) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users").child(Objects.requireNonNull(auth.getUid()));
            myRef.child("namaUser").setValue(nama);
            myRef.child("nomorHP").setValue(nohp);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(
                    this,
                    "Fill All Fields",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttondaftar:
                update();
                break;
        }
    }
}
