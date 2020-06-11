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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.adapter.AdapterPromo;
import com.shoes.shoeslaundry.data.model.Order;
import com.shoes.shoeslaundry.data.model.Promo;
import com.shoes.shoeslaundry.ui.user.ChatActivity;
import com.shoes.shoeslaundry.utils.notifications.APIService;
import com.shoes.shoeslaundry.utils.notifications.Client;
import com.shoes.shoeslaundry.utils.notifications.Data;
import com.shoes.shoeslaundry.utils.notifications.Response;
import com.shoes.shoeslaundry.utils.notifications.Sender;
import com.shoes.shoeslaundry.utils.notifications.Token;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AdminOrderDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Order order;
    private TextView tv_ordernumber, tv_washtype, tv_droptime, tv_totalprice, tv_status, tv_alamat;
    private Button btn_back, btn_ubahstatus, btn_chat, btn_call;
    private String status;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth auth;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_detail);

        auth = FirebaseAuth.getInstance();
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

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
                Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", order.getNohp(), null));
                startActivity(intentCall);
                break;
        }
    }

    private void changeStatus(){
        sendNotification(order.getIduser(), order.getOrdernumber(), order.getStatus(), "new_status");
        order.setStatus(status);
        tv_status.setText(status);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("Order").child(order.getIdorder()).setValue(order);
        finish();
    }

    private void sendNotification(final String hisUID, final String name, final String text, String title) {
        if (title.equals("new_message")) {
            title = "New Message";
        } else if (title.equals("new_status")) {
            title = "New Status";
        }
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUID);
        String finalTitle = title;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Token token = dt.getValue(Token.class);
                    Data data = new Data(auth.getUid(), "Order #"+name + " : " + status, finalTitle, hisUID, R.drawable.wash);
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
//                                    Toast.makeText(AdminOrderDetailActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
