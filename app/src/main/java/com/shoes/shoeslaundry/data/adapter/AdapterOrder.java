package com.shoes.shoeslaundry.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.model.Order;
import com.shoes.shoeslaundry.ui.admin.AdminOrderDetailActivity;
import com.shoes.shoeslaundry.ui.user.OrderDetailActivity;

import java.util.ArrayList;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> {

    private final ArrayList<Order> listsewa;
    private final Context context;
    private boolean admin;

    public AdapterOrder(ArrayList<Order> listsewa, Context context, boolean admin) {
        this.listsewa = listsewa;
        this.context = context;
        this.admin = admin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_card_order, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Order order = listsewa.get(position);
        holder.display(order);
    }

    @Override
    public int getItemCount() {
        return listsewa.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_ordernumber;
        private TextView tv_status;

        ViewHolder(View itemView) {
            super(itemView);
            tv_ordernumber = itemView.findViewById(R.id.ordernumber);
            tv_status = itemView.findViewById(R.id.orderstatus);
        }

        void display(final Order order) {
            tv_ordernumber.setText("Order No : #" + order.getOrdernumber());
            tv_status.setText(order.getStatus());
            if (admin) {
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, AdminOrderDetailActivity.class);
                    intent.putExtra("order", order);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
            } else {
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("order", order);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
            }
        }

    }
}