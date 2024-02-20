package com.hotel.reservas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Inicio extends AppCompatActivity {

    Button siguiente1;
    Button saltar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        siguiente1=(Button) findViewById(R.id.siguiente1);
        saltar1=(Button) findViewById(R.id.saltar1);

        siguiente1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Inicio.this, Inicio1.class);
                startActivity(i);
            }
        });

        saltar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Inicio.this, Login.class);
                startActivity(i);
            }
        });
    }
}