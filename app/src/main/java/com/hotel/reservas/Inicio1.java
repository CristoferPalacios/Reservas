package com.hotel.reservas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Inicio1 extends AppCompatActivity {

    Button siguiente2;
    Button saltar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio1);
        siguiente2=(Button) findViewById(R.id.siguiente2);
        saltar2=(Button) findViewById(R.id.saltar2);

        siguiente2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Inicio1.this, Inicio2.class);
                startActivity(i);
            }
        });

        saltar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Inicio1.this, Menu.class);
                startActivity(i);
            }
        });
    }
}