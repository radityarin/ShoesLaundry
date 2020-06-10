package com.shoes.shoeslaundry.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.model.Promo;

public class PromotionDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Promo promo;
    private TextView tv_title, tv_desc, tv_start, tv_end;
    private ImageView iv_photo;
    private Button backbutton;

    private AlertDialog.Builder dialog;
    private LayoutInflater inflater;
    private View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_detail);

        promo = getIntent().getParcelableExtra("promo");

        tv_title = findViewById(R.id.tv_titlepromo);
        tv_desc = findViewById(R.id.tv_descriptionpromo);
        tv_start = findViewById(R.id.tv_startdate);
        tv_end = findViewById(R.id.tv_enddate);
        iv_photo = findViewById(R.id.iv_fotopromo);
        backbutton = findViewById(R.id.backbutton);

        tv_title.setText(promo.getTitle());
        tv_desc.setText(promo.getDescription());
        tv_start.setText(promo.getStartdate());
        tv_end.setText(promo.getEnddate());
        Glide.with(this).load(promo.getPhoto()).into(iv_photo);
        backbutton.setOnClickListener(this);
        iv_photo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backbutton:
                onBackPressed();
                break;
            case R.id.iv_fotopromo:
                enlargeImage();
                break;
        }
    }
    private void enlargeImage() {
        dialog = new AlertDialog.Builder(PromotionDetailActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.image_layout, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        ImageView iv_photo = dialogView.findViewById(R.id.iv_image);

        Glide.with(this).load(promo.getPhoto()).into(iv_photo);

        dialog.setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
