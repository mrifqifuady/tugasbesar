package com.fuad.donasi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageView imgDonatur, imageViewPanti, imageViewJenis, imageViewLaporan, imageViewBulan,ImageViewMaps;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        session = new Session(getApplicationContext());
        Toast.makeText(getApplicationContext(), session.getsession("id_donatur"), Toast.LENGTH_LONG).show();
        imageViewPanti = (ImageView) findViewById(R.id.imageViewPanti);
        imageViewJenis = (ImageView) findViewById(R.id.imageViewJenis);
        imageViewLaporan = (ImageView) findViewById(R.id.imageViewLaporan);
        imageViewBulan = (ImageView) findViewById(R.id.imageViewBulan);
        ImageViewMaps = (ImageView) findViewById(R.id.imageViewMaps);


        imageViewPanti.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PantiActivity.class);
                startActivity(i);
            }
        });
        imageViewJenis.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), JenisDonasiActivity.class);
                startActivity(i);
            }
        });
        imageViewLaporan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LaporanActivity.class);
                startActivity(i);
            }
        });
        imageViewBulan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), BulanActivity.class);
                startActivity(i);
            }
        });
        ImageViewMaps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
            }
        });


    }
}
