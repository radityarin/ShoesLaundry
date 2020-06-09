package com.shoes.shoeslaundry.ui.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.model.Promo;
import com.shoes.shoeslaundry.utils.DatePickerFragment;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class CreatePromotionActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private Button backbutton, btn_tanggalselesai, btn_buatpromo;
    private ImageView iv_fotopromo;
    private EditText et_judulpromo, et_deskripsipromo;

    private String urlfotopromo;
    private String tanggal;

    private Promo promo;

    private StorageReference imageStorage;
    private final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_promotion);

        promo = getIntent().getParcelableExtra("promo");


        backbutton = findViewById(R.id.backbutton);
        btn_tanggalselesai = findViewById(R.id.btn_tanggalselesai);
        btn_buatpromo = findViewById(R.id.btn_buatpromo);
        iv_fotopromo = findViewById(R.id.iv_fotopromo);
        et_judulpromo = findViewById(R.id.et_judulpromo);
        et_deskripsipromo = findViewById(R.id.et_deskripsipromo);

        backbutton.setOnClickListener(this);
        btn_buatpromo.setOnClickListener(this);
        btn_tanggalselesai.setOnClickListener(this);
        iv_fotopromo.setOnClickListener(this);

        imageStorage = FirebaseStorage.getInstance().getReference();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backbutton:
                onBackPressed();
                break;
            case R.id.btn_tanggalselesai:
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker ");
                break;
            case R.id.btn_buatpromo:
                createPromotion();
                break;
            case R.id.iv_fotopromo:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                break;
        }
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

    private String getTodayDate() {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, 0);
        Date tomorrow = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(tomorrow);
        return formattedDate;
    }

    private void createPromotion() {
        String key = FirebaseDatabase.getInstance().getReference().child("Promo").push().getKey();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Promo").child(key);
        Promo promo = new Promo();
        promo.setTitle(et_judulpromo.getText().toString());
        promo.setDescription(et_deskripsipromo.getText().toString());
        promo.setPhoto(urlfotopromo);
        promo.setStartdate(getTodayDate());
        promo.setEnddate(tanggal);
        promo.setIdpromotion(key);
        myRef.setValue(promo);
        Intent intent = new Intent(CreatePromotionActivity.this,MainAdminActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            final Uri imageUri = Objects.requireNonNull(data).getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            iv_fotopromo.setImageBitmap(bitmap);

            final StorageReference filepath = imageStorage.child("Foto Promo").child(UUID.randomUUID().toString() + ".jpg");
            filepath.putFile(Objects.requireNonNull(imageUri)).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }

                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        //mendapatkan link foto
                        Uri downloadUri = task.getResult();
                        urlfotopromo = downloadUri.toString();
                    }
                }
            });
        }
    }
}
