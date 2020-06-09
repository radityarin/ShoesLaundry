package com.shoes.shoeslaundry.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.adapter.AdapterPromo;
import com.shoes.shoeslaundry.data.model.Order;
import com.shoes.shoeslaundry.data.model.Promo;

import java.util.ArrayList;

public class AdminOrderDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Order order;
    private TextView tv_ordernumber, tv_washtype, tv_droptime, tv_totalprice, tv_status, tv_alamat;
    private Button btn_back, btn_ubahstatus, btn_chat, btn_call;
    private String status;
    private DatabaseReference mFirebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_detail);

        order = getIntent().getParcelableExtra("order");
        btn_back = findViewById(R.id.backbutton);
        btn_ubahstatus = findViewById(R.id.btn_ubahstatus);
        btn_chat = findViewById(R.id.btn_chat);
        btn_call = findViewById(R.id.btn_call);
        btn_back.setOnClickListener(this);
        btn_ubahstatus.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        btn_call.setOnClickListener(this);

        if (order.getStatus().equals("Belum diterima")){
            btn_ubahstatus.setText("Terima Pesanan");
            status = "Pesanan diterima";
        } else if (order.getStatus().equals("Pesanan diterima")){
            btn_ubahstatus.setText("Proses Pencucian");
            status = "Pencucian";
        } else if (order.getStatus().equals("Pencucian")){
            btn_ubahstatus.setText("Proses Pengeringan");
            status = "Pengeringan";
        } else if (order.getStatus().equals("Pengeringan")){
            btn_ubahstatus.setText("Selesai");
            status = "Pesanan Selesai";
        }

        tv_ordernumber = findViewById(R.id.ordernumber);
        tv_washtype = findViewById(R.id.washtype);
        tv_droptime = findViewById(R.id.droptime);
        tv_totalprice = findViewById(R.id.totalprice);
        tv_status = findViewById(R.id.status);
        tv_alamat = findViewById(R.id.alamatpelanggan);

        tv_ordernumber.setText("No #" + order.getOrdernumber());
        tv_washtype.setText(order.getWashtype());
        tv_droptime.setText(order.getDroptime());
        tv_totalprice.setText(String.valueOf(order.getTotalprice()));
        tv_status.setText(order.getStatus());
        tv_alamat.setText(order.getAddress());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ubahstatus:
                changeStatus();
                break;
            case R.id.btn_chat:
                Intent intent = new Intent(AdminOrderDetailActivity.this, ChatAdminActivity.class);
                intent.putExtra("uidpelanggan", order.getIduser());
                startActivity(intent);
                break;
            case R.id.backbutton:
                onBackPressed();
                break;
            case R.id.btn_call:
                Intent intentcall = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", order.getNohp(), null));
                startActivity(intentcall);
                break;
        }
    }

    private void changeStatus(){
        order.setStatus(status);
        tv_status.setText(status);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("Order").child(order.getIdorder()).setValue(order);
        finish();
    }

}
