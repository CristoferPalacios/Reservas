package com.hotel.reservas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Inicio2 extends AppCompatActivity {

    Button siguiente3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio2);
        siguiente3=(Button) findViewById(R.id.siguiente3);

        siguiente3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Inicio2.this, Login.class);
                startActivity(i);
            }
        });

    }
}