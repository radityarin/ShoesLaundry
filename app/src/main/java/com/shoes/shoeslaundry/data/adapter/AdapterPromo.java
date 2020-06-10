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
import com.shoes.shoeslaundry.ui.admin.ChatAdminActivity;
import com.shoes.shoeslaundry.ui.admin.CreatePromotionActivity;
import com.shoes.shoeslaundry.ui.admin.EditPromotionActivity;
import com.shoes.shoeslaundry.ui.user.PromotionDetailActivity;

import java.util.ArrayList;

public class AdapterPromo extends RecyclerView.Adapter<AdapterPromo.ViewHolder> {

    private final ArrayList<Promo> listsewa;
    private final Context context;
    private boolean admin;

    public AdapterPromo(ArrayList<Promo> listsewa, Context context, boolean admin) {
        this.listsewa = listsewa;
        this.context = context;
        this.admin = admin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_card_promotion, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Promo promo = listsewa.get(position);
        holder.display(promo);
    }

    @Override
    public int getItemCount() {
        return listsewa.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_item_photo;
        private TextView tv_title;

        ViewHolder(View itemView) {
            super(itemView);
            iv_item_photo = itemView.findViewById(R.id.photopromotion);
            tv_title = itemView.findViewById(R.id.titlepromotion);
        }

        void display(final Promo promotion) {
            Glide.with(itemView.getContext())
                    .load(promotion.getPhoto())
                    .into(iv_item_photo);
            tv_title.setText(promotion.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (admin) {
                        Intent intent = new Intent(context, EditPromotionActivity.class);
                        intent.putExtra("promo", promotion);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, PromotionDetailActivity.class);
                        intent.putExtra("promo", promotion);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }

    }
}