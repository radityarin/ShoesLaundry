package com.shoes.shoeslaundry.ui.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.adapter.AdapterOrder;
import com.shoes.shoeslaundry.data.model.Order;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private ArrayList<Order> listsewa;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        auth = FirebaseAuth.getInstance();
        getOrder();
        return view;
    }

    private void getOrder() {
        listsewa = new ArrayList<>();

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference sewaRef = FirebaseDatabase.getInstance().getReference().child("Order");
        sewaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listsewa.clear();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Order mSewa = dt.getValue(Order.class);
                    if (mSewa.getStatus().equals("Pesanan Selesai")) {
                        listsewa.add(mSewa);
                    }
                }
                recyclerView.setAdapter(new AdapterOrder(listsewa, getContext(),false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
