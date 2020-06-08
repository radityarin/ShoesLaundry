package com.shoes.shoeslaundry.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.model.User;
import com.shoes.shoeslaundry.ui.generalactivity.LoginPage;

import java.util.Objects;


public class SignUpPage extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth auth;
    private EditText inputNama, inputEmail, inputPassword, inputNOHP;
    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        Button btnBack = findViewById(R.id.backbutton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();

        inputNama = findViewById(R.id.nama);
        inputEmail = findViewById(R.id.email);
        inputNOHP = findViewById(R.id.noHp);
        inputPassword = findViewById(R.id.password);

        Button btndaftar = findViewById(R.id.login);
        btndaftar.setOnClickListener(this);

        Button btnDaftar = findViewById(R.id.buttondaftar);
        btnDaftar.setOnClickListener(this);

    }

    private void register() {
        final String nama = inputNama.getText().toString();
        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        final String nohp = inputNOHP.getText().toString();
        PD = new ProgressDialog(SignUpPage.this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        PD.show();
        try {
            if (password.length() > 0 && email.length() > 0) {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    PD.dismiss();
                                    Toast.makeText(
                                            SignUpPage.this,
                                            "Authentication Failed",
                                            Toast.LENGTH_LONG).show();
                                    Log.v("error", Objects.requireNonNull(task.getResult()).toString());
                                } else {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("Users").child(Objects.requireNonNull(auth.getUid()));
                                    User user = new User(auth.getUid(), nama, email, nohp);
                                    myRef.setValue(user);
                                    Intent intent = new Intent(SignUpPage.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            } else {
                Toast.makeText(
                        SignUpPage.this,
                        "Fill All Fields",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(intent);
                finish();
                break;
            case R.id.buttondaftar:
                register();
        }
    }
}
