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
import com.shoes.shoeslaundry.ui.admin.HistoryMonthActivity;

import java.util.ArrayList;

public class AdapterMonth extends RecyclerView.Adapter<AdapterMonth.ViewHolder> {

    private final ArrayList<String> listsewa;
    private final Context context;

    public AdapterMonth(ArrayList<String> listsewa, Context context) {
        this.listsewa = listsewa;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_month_order, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.display(listsewa.get(position));
    }

    @Override
    public int getItemCount() {
        return listsewa.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMonthYear;

        ViewHolder(View itemView) {
            super(itemView);
            tvMonthYear = itemView.findViewById(R.id.tvMonthYear);
        }

        void display(final String monthYear) {
            tvMonthYear.setText(monthYear);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, HistoryMonthActivity.class);
                intent.putExtra("monthYear", monthYear);
                context.startActivity(intent);
            });
        }

    }
}