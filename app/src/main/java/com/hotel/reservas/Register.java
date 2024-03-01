package com.hotel.reservas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ArrayAdapter;


import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import com.hbb20.CountryCodePicker;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import  com.hotel.reservas.mvc.BLL.UsuarioBll;
import  com.hotel.reservas.mvc.Entidad.Usuario;
import  com.hotel.reservas.mvc.Recurso.recurso;


public class Register extends AppCompatActivity {

    ImageButton retroceder ;
    Button registrar;

    String clave;


    UsuarioBll bll = new UsuarioBll();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        retroceder = findViewById(R.id.btnatras);
        EditText nombre = findViewById(R.id.txtnombre);
        EditText user = findViewById(R.id.txtusuario);
        TextView fecha = findViewById(R.id.txtfecha);
        EditText correo = findViewById(R.id.txtCorreo);
        EditText phone = findViewById(R.id.txtnumero);
        Spinner genero = findViewById(R.id.spinnerGenero);
        CountryCodePicker cnp = findViewById(R.id.countryCodePicker);
        registrar = findViewById(R.id.btnregistrar);



        Spinner spinnerGenero = findViewById(R.id.spinnerGenero);
        TextView editTextFecha = findViewById(R.id.txtfecha);

        String[] generoOptions = getResources().getStringArray(R.array.genero_options);

        String[] generoOptionsWithPlaceholder = new String[generoOptions.length + 1];
        generoOptionsWithPlaceholder[0] = "Selecciona un género";
        System.arraycopy(generoOptions, 0, generoOptionsWithPlaceholder, 1, generoOptions.length);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, generoOptionsWithPlaceholder);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapter);



        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usuario = new Usuario();
                usuario.setNombre(nombre.getText().toString().trim());
                usuario.setUsuario(user.getText().toString().trim());
                usuario.setFecha(fecha.getText().toString().trim());
                usuario.setCorreo(correo.getText().toString().trim());
                String numero = cnp.getSelectedCountryCode() + phone.getText().toString().trim();
                usuario.setNumero( numero);
                if (genero.getSelectedItemPosition() != 0) {
                    usuario.setGenero(genero.getSelectedItemPosition());
                } else {
                    Toast.makeText(Register.this, "Selesccione un genero", Toast.LENGTH_SHORT).show();
                }
                usuario.setClave(recurso.generarClave());

                // Crear la cadena HTML después de establecer todas las propiedades del usuario
                String HTML = "Bienvenido  \n"+ "Sus Credenciales son estas\n"+ "correo : "+ usuario.getCorreo() +"\n" + "Clave : " +usuario.getClave();

                // Llamar al método registrar con el objeto Usuario creado
                Boolean exitoso =  bll.Registrar(usuario , HTML);

                if( exitoso == true){
                    Toast.makeText(Register.this, "Se registro correctamente ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Register.this, "Hubo fallas", Toast.LENGTH_SHORT).show();
                }


            }
        });



        editTextFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar minDate = Calendar.getInstance();
                minDate.add(Calendar.YEAR, -18);

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Crea un DatePickerDialog y configura la fecha actual como predeterminada
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Aquí puedes manejar la fecha seleccionada
                                String selectedDate = formatDate(dayOfMonth, month + 1, year);
                                editTextFecha.setText(selectedDate);
                            }
                        }, year, month, dayOfMonth);

                // Establece la fecha mínima permitida
                datePickerDialog.getDatePicker().setMaxDate(minDate.getTimeInMillis());

                // Muestra el DatePickerDialog
                datePickerDialog.show();
            }
        });





        retroceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
            }
        });
    }




    public void onBackButtonClicked() {
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private String formatDate(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("es"));
        return dateFormat.format(calendar.getTime());
    }
}


