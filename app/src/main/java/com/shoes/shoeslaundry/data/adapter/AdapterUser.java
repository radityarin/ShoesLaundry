package com.shoes.shoeslaundry.data.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.model.Promo;
import com.shoes.shoeslaundry.data.model.User;
import com.shoes.shoeslaundry.ui.admin.ChatAdminActivity;
import com.shoes.shoeslaundry.ui.user.OrderDetailActivity;

import java.util.ArrayList;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolder> {

    private final ArrayList<User> listsewa;
    private final Context context;

    public AdapterUser(ArrayList<User> listsewa, Context context) {
        this.listsewa = listsewa;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        User promo = listsewa.get(position);
        holder.display(promo);
    }

    @Override
    public int getItemCount() {
        return listsewa.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_username;

        ViewHolder(View itemView) {
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_username);
        }

        void display(final User promotion) {
            tv_username.setText(promotion.getNamaUser());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatAdminActivity.class);
                    intent.putExtra("uidpelanggan", promotion.getUserId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

    }
}