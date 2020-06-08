package com.shoes.shoeslaundry.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.model.Order;

public class OrderDetailActivity extends AppCompatActivity {

    private Order order;
    private TextView tv_ordernumber,tv_washtype, tv_droptime, tv_totalprice, tv_status;
    private Button btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order = getIntent().getParcelableExtra("order");

        btn_back = findViewById(R.id.backbutton);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_ordernumber = findViewById(R.id.ordernumber);
        tv_washtype = findViewById(R.id.washtype);
        tv_droptime = findViewById(R.id.droptime);
        tv_totalprice = findViewById(R.id.totalprice);
        tv_status = findViewById(R.id.status);

        tv_ordernumber.setText("No #"+order.getOrdernumber());
        tv_washtype.setText(order.getWashtype());
        tv_droptime.setText(order.getDroptime());
        tv_totalprice.setText(String.valueOf(order.getTotalprice()));
        tv_status.setText(order.getStatus());
    }
}
