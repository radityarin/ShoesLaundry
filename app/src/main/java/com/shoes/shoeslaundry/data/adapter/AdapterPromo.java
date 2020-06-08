package com.shoes.shoeslaundry.data.adapter;


import android.content.Context;
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

import java.util.ArrayList;

public class AdapterPromo extends RecyclerView.Adapter<AdapterPromo.ViewHolder> {

    private final ArrayList<Promo> listsewa;
    private final Context context;

    public AdapterPromo(ArrayList<Promo> listsewa, Context context) {
        this.listsewa = listsewa;
        this.context = context;
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

        void display(Promo promotion) {
            Glide.with(itemView.getContext())
                    .load(promotion.getPhoto())
                    .into(iv_item_photo);
            tv_title.setText(promotion.getTitle());
        }

    }
}