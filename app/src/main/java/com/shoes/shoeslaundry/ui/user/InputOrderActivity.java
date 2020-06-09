package com.shoes.shoeslaundry.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoes.shoeslaundry.R;
import com.shoes.shoeslaundry.data.model.Order;
import com.shoes.shoeslaundry.data.model.User;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class InputOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_washtype, tv_totalorder;
    private EditText edt_alamat;
    private Button btn_lokasi, btn_order;
    static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private AlertDialog.Builder dialog;
    private LayoutInflater inflater;
    private View dialogView;

    private String washtype;
    private int totalprice;

    private TextView tv_nama, tv_nohp, tv_alamat, tv_jeniscuci, tv_totalharga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_order);

        washtype = getIntent().getStringExtra("washtype");
        totalprice = getIntent().getIntExtra("totalprice", 0);

        tv_washtype = findViewById(R.id.washtype);
        tv_totalorder = findViewById(R.id.tv_totalorder);

        edt_alamat = findViewById(R.id.alamatpelanggan);
        btn_lokasi = findViewById(R.id.btn_lokasi);
        btn_lokasi.setOnClickListener(this);
        btn_order = findViewById(R.id.buttonorder);
        btn_order.setOnClickListener(this);
        tv_washtype.setText(washtype);

        getOrderID();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_lokasi:
                getCurrentLocation();
                break;
            case R.id.buttonorder:
                if (edt_alamat.getText().toString().equals("")) {
                    Toast.makeText(this, "Lengkapi alamat", Toast.LENGTH_SHORT).show();
                } else {
                    confirmation();
                }
                break;
        }

    }

    private void getOrderID() {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Order");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long total = dataSnapshot.getChildrenCount();
                tv_totalorder.setText(String.valueOf(total + 1));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void confirmation() {
        getUsersData();
        dialog = new AlertDialog.Builder(InputOrderActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.confirmation_layout, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        tv_nama = dialogView.findViewById(R.id.tv_namapelanggan);
        tv_nohp = dialogView.findViewById(R.id.tv_nomorhppelanggan);
        tv_alamat = dialogView.findViewById(R.id.tv_alamatpelanggan);
        tv_jeniscuci = dialogView.findViewById(R.id.tv_tipecuci);
        tv_totalharga = dialogView.findViewById(R.id.tv_totalharga);

        tv_alamat.setText(edt_alamat.getText().toString());
        tv_jeniscuci.setText(washtype);
        tv_totalharga.setText("Rp " + totalprice);

        dialog.setPositiveButton("Sudah Benar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nama = tv_nama.getText().toString();
                String nohp = tv_nohp.getText().toString();
                String alamat = edt_alamat.getText().toString();
                String jenis = washtype;
                String ordernumber = tv_totalorder.getText().toString();
                int totalharga = totalprice;
                order(nama, nohp, alamat, jenis, totalharga, ordernumber);
            }
        });
        dialog.setNegativeButton("Cek Kembali", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void order(String nama, String nohp, String alamat, String jenis, int totalharga, String orderno) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String key = FirebaseDatabase.getInstance().getReference().child("Order").push().getKey();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Order").child(key);

        Order order = new Order(nama, orderno,key, auth.getUid(), nohp, getTodayDate(), jenis, "Belum diterima", alamat, totalharga);
        myRef.setValue(order);

        Intent intent = new Intent(InputOrderActivity.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private String getTodayDate() {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, 0);
        Date tomorrow = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(tomorrow);
        return formattedDate;
    }


    private void getUsersData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(Objects.requireNonNull(auth.getUid()));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User profil = dataSnapshot.getValue(User.class);
                tv_nama.setText(profil.getNamaUser());
                tv_nohp.setText(profil.getNomorHP());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    private String getCityAndProvince(Location location) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        String address = null;
        geocoder = new Geocoder(InputOrderActivity.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());
            address = addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String currentLocation = address;
        return currentLocation;
    }

    private void getCurrentLocation() {

        final LocationManager locationManager = (LocationManager) InputOrderActivity.this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    try {
                        Log.d("cek", "onLocationChanged: " + getCityAndProvince(location));
                        edt_alamat.setText(getCityAndProvince(location));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InputOrderActivity.this);

                // set title dialog
                alertDialogBuilder.setTitle("GPS mati");

                // set pesan dari dialog
                alertDialogBuilder
                        .setMessage("Apakah ingin menghidupkan GPS?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // jika tombol diklik, maka akan menutup activity ini
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // jika tombol ini diklik, akan menutup dialog
                                // dan tidak terjadi apa2
                                dialog.cancel();
                            }
                        });

                // membuat alert dialog dari builder
                AlertDialog alertDialog = alertDialogBuilder.create();

                // menampilkan alert dialog
                alertDialog.show();
            }
        };

        if (ActivityCompat.checkSelfPermission(InputOrderActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(InputOrderActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 50, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 50, locationListener);
    }

}
