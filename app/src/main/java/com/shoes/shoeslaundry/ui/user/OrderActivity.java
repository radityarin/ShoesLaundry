package com.shoes.shoeslaundry.ui.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.shoes.shoeslaundry.R;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_cucibersih, ll_cucicepat, ll_cucikilat, ll_repaint, ll_recolour;
    private Button btn_information;
    private AlertDialog.Builder dialog;
    private LayoutInflater inflater;
    private View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ll_cucibersih = findViewById(R.id.btn_cucibersih);
        ll_cucicepat = findViewById(R.id.btn_cucicepat);
        ll_cucikilat = findViewById(R.id.btn_cucikilat);
        ll_repaint = findViewById(R.id.btn_repaint);
        ll_recolour = findViewById(R.id.btn_recolour);
        btn_information = findViewById(R.id.btn_information);
        ll_cucibersih.setOnClickListener(this);
        ll_cucicepat.setOnClickListener(this);
        ll_cucikilat.setOnClickListener(this);
        ll_repaint.setOnClickListener(this);
        ll_recolour.setOnClickListener(this);
        btn_information.setOnClickListener(this);
    }

    private void showInformation() {
        dialog = new AlertDialog.Builder(OrderActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.information_layout, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Wash Type Information");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(OrderActivity.this, InputOrderActivity.class);
        switch (v.getId()) {
            case R.id.btn_cucibersih:
                intent.putExtra("washtype","Cuci Bersih");
                intent.putExtra("totalprice",25000);
                startActivity(intent);
                break;
            case R.id.btn_cucicepat:
                intent.putExtra("washtype","Cuci Cepat");
                intent.putExtra("totalprice",20000);
                startActivity(intent);
                break;
            case R.id.btn_cucikilat:
                intent.putExtra("washtype","Cuci Kilat");
                intent.putExtra("totalprice",30000);
                startActivity(intent);
                break;
            case R.id.btn_repaint:
                intent.putExtra("washtype","Repaint");
                intent.putExtra("totalprice",125000);
                startActivity(intent);
                break;
            case R.id.btn_recolour:
                intent.putExtra("washtype","Recolour");
                intent.putExtra("totalprice",140000);
                startActivity(intent);
                break;
            case R.id.btn_information:
                showInformation();
                break;
        }
    }
}
