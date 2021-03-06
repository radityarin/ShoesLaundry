package com.shoes.shoeslaundry.ui.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.adapter.AdapterOrder;
import com.shoes.shoeslaundry.data.adapter.AdapterPromo;
import com.shoes.shoeslaundry.data.model.Order;
import com.shoes.shoeslaundry.data.model.Promo;

import java.util.ArrayList;

public class PromotionFragment extends Fragment implements View.OnClickListener {


    private ArrayList<Promo> listsewa;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private FloatingActionButton btn_createpromotion;

    public PromotionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_promotion, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        btn_createpromotion = view.findViewById(R.id.btn_createpromotion);
        btn_createpromotion.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        getOrder();
        return view;
    }

    private void getOrder() {
        listsewa = new ArrayList<>();

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference sewaRef = FirebaseDatabase.getInstance().getReference().child("Promo");
        sewaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listsewa.clear();
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    Promo mSewa = dt.getValue(Promo.class);
                    listsewa.add(mSewa);

                }
                recyclerView.setAdapter(new AdapterPromo(listsewa, getContext(), true));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_createpromotion:
                Intent intent = new Intent(getContext(),CreatePromotionActivity.class);
                startActivity(intent);
                break;
        }
    }
}
