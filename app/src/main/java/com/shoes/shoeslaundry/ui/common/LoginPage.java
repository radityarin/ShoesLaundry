package com.shoes.shoeslaundry.ui.common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.ui.admin.MainAdminActivity;
import com.shoes.shoeslaundry.ui.user.MainActivity;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    private Button btnMasuk, btndaftar, backbutton;
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private TextView tvlupapassword;
    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        backbutton = findViewById(R.id.backbutton);
        inputEmail = findViewById(R.id.emaillogin);
        inputPassword = findViewById(R.id.passwordlogin);
        btnMasuk = findViewById(R.id.buttonlogin);
        btndaftar = findViewById(R.id.daftar);
        tvlupapassword = findViewById(R.id.lupapassword);

        tvlupapassword.setOnClickListener(this);
        backbutton.setOnClickListener(this);
        btnMasuk.setOnClickListener(this);
        btndaftar.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backbutton:
                finish();
                break;
            case R.id.daftar:
                Intent intent = new Intent(LoginPage.this, SignUpPage.class);
                startActivity(intent);
                finish();
                break;
            case R.id.lupapassword:
                Intent intent2 = new Intent(LoginPage.this, ForgotPasswordActivity.class);
                startActivity(intent2);
                break;
            case R.id.buttonlogin:
                login();
                break;
        }
    }

    private void login() {
        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();
        PD = new ProgressDialog(LoginPage.this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);
        PD.show();


            try {
                if (password.length() > 0 && email.length() > 0) {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        PD.dismiss();
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(LoginPage.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                        if (email.equals("admin@gmail.com") || password.equals("gscadmin123")) {
                                            Intent intent3 = new Intent(LoginPage.this, MainAdminActivity.class);
                                            startActivity(intent3);
                                            finishAffinity();
                                        } else {
                                            Intent i = new Intent(LoginPage.this, MainActivity.class);
                                            startActivity(i);
                                            finishAffinity();
                                        }
                                    } else {
                                        PD.dismiss();
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginPage.this, "GAGAL", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(
                            LoginPage.this,
                            "Fill All Fields",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

}
