package com.shoes.shoeslaundry.ui.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.model.Promo;
import com.shoes.shoeslaundry.utils.DatePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class EditPromotionActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private Button backbutton, btn_tanggalselesai, btn_ubahpromo, btn_hapuspromo;
    private ImageView iv_fotopromo;
    private EditText et_judulpromo, et_deskripsipromo;
    private Promo promo;
    private String tanggal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_promotion);

        promo = getIntent().getParcelableExtra("promo");

        backbutton = findViewById(R.id.backbutton);
        btn_tanggalselesai = findViewById(R.id.btn_tanggalselesai);
        btn_ubahpromo = findViewById(R.id.btn_ubahpromo);
        btn_hapuspromo = findViewById(R.id.btn_hapuspromo);
        iv_fotopromo = findViewById(R.id.iv_fotopromo);
        et_judulpromo = findViewById(R.id.et_judulpromo);
        et_deskripsipromo = findViewById(R.id.et_deskripsipromo);

        et_judulpromo.setText(promo.getTitle());
        et_deskripsipromo.setText(promo.getDescription());
        btn_tanggalselesai.setText(promo.getEnddate());

        Glide.with(this)
                .load(promo.getPhoto())
                .into(iv_fotopromo);
        backbutton.setOnClickListener(this);
        btn_ubahpromo.setOnClickListener(this);
        btn_hapuspromo.setOnClickListener(this);
        btn_tanggalselesai.setOnClickListener(this);
        iv_fotopromo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backbutton:
                onBackPressed();
                break;
            case R.id.btn_tanggalselesai:
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker ");
                break;
            case R.id.btn_ubahpromo:
                createPromotion();
                break;
            case R.id.btn_hapuspromo:
                deletePromotion();
                break;
        }
    }

    private void deletePromotion() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Promo").child(promo.getIdpromotion());
        myRef.removeValue();
        Intent intent = new Intent(EditPromotionActivity.this,MainAdminActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        tanggal = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        btn_tanggalselesai.setText(tanggal);
    }

    private void createPromotion() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Promo").child(promo.getIdpromotion());
        promo.setTitle(et_judulpromo.getText().toString());
        promo.setDescription(et_deskripsipromo.getText().toString());
        promo.setEnddate(tanggal);
        myRef.setValue(promo);
        Intent intent = new Intent(EditPromotionActivity.this,MainAdminActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
