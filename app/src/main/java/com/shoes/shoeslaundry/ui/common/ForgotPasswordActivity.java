package com.shoes.shoeslaundry.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.shoes.shoeslaundry.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emaillogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Button backbutton = findViewById(R.id.backbutton);
        Button btnlupa = findViewById(R.id.buttonlupapassword);

        backbutton.setOnClickListener(this);
        btnlupa.setOnClickListener(this);

        emaillogin = findViewById(R.id.emaillogin);
    }

    private void forgotPassword() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(emaillogin.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SweetAlertDialog pDialog = new SweetAlertDialog(ForgotPasswordActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        pDialog.setTitleText("Reset password telah dikirim ke email anda");
                        pDialog.setCancelable(false);
                        pDialog.setContentText("Silahkan cek email anda");
                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                Intent intent = new Intent(ForgotPasswordActivity.this, LandingPage.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                            }
                        });
                        pDialog.show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backbutton:
                onBackPressed();
                break;
            case R.id.buttonlupapassword:
                forgotPassword();
                break;
        }
    }
}
