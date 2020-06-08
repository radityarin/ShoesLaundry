package com.shoes.shoeslaundry.data.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.model.Order;
import com.shoes.shoeslaundry.data.model.Promotion;

import java.util.ArrayList;

public class AdapterPromo extends RecyclerView.Adapter<AdapterPromo.ViewHolder> {

    private final ArrayList<Promotion> listsewa;
    private final Context context;

    public AdapterPromo(ArrayList<Promotion> listsewa, Context context) {
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
        holder.display(listsewa.get(position));
    }

    @Override
    public int getItemCount() {
        return listsewa.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final View view;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void display(Promotion promotion) {
//            TextView tvnamalapangan = view.findViewById(R.id.namapenyewa);
//            if (penyedia) {
//                if (statussewa.equalsIgnoreCase("Belum dikonfirmasi")) {
//                    tvnamalapangan.setText(namapenyewa);
//                } else {
//                    tvnamalapangan.setText(namapenyewa + " Lap. no " + nomorlapangan);
//                }
//            } else {
//                if (statussewa.equalsIgnoreCase("Belum dikonfirmasi")) {
//                    tvnamalapangan.setText(lapangansewa);
//                } else {
//                    tvnamalapangan.setText(lapangansewa + " Lap. no " + nomorlapangan);
//                }
//            }
//            TextView tvtanggalsewa = view.findViewById(R.id.tanggalsewa);
//            tvtanggalsewa.setText(tanggalsewa);
//            TextView tvjamsewa = view.findViewById(R.id.jamsewa);
//            tvjamsewa.setText(jamsewa);
//            TextView tvstatussewa = view.findViewById(R.id.statussewa);
//            tvstatussewa.setText(statussewa);
        }

    }
}