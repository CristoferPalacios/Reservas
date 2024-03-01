package com.hotel.reservas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hotel.reservas.mvc.Recurso.recurso;
import com.hotel.reservas.mvc.BLL.UsuarioBll;
import  com.hotel.reservas.mvc.Entidad.Usuario;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import com.facebook.CallbackManager;



public class Login extends AppCompatActivity {

    Button loguear;
    TextView registrarse;
    EditText usuarios, clave;
    ImageView google;

    private FirebaseAuth auth;
    private GoogleSignInClient iniciogoogle;
    private static final int RC_SIGN_IN = 9001;

    private CallbackManager callbackManager;

    Usuario usuario = new Usuario();
    UsuarioBll bll = new UsuarioBll();

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Future<Boolean> loginFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registrarse = findViewById(R.id.btnregistrar);
        google = findViewById(R.id.google_btn);

        auth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });

        // Llamada al método crearSolicitud dentro del bloque de inicialización onCreate
        crearSolicitud();
    }

    private void onIngresarClick() {
        usuario.setUsuario(usuarios.getText().toString());
        usuario.setClave(recurso.sha256(clave.getText().toString()));
        loginFuture = executorService.submit(() -> bll.Login( usuario.getUsuario(), usuario.getClave()));
        executorService.execute(() -> {
            try {
                boolean exitoso = loginFuture.get();
                runOnUiThread(() -> {
                     if (exitoso) {
                        Intent intent = new Intent(Login.this, Menu.class);
                        startActivity(intent);
                    } else {
                        mostrarCredencialesIncorrectas();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void mostrarCredencialesIncorrectas() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle("Credenciales Incorrecta")
                .setMessage("Las credenciales proporcionadas son incorrectas. Por favor, inténtalo de nuevo.")
                .setNegativeButton("Aceptar", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdownNow(); // Detener la ejecución de tareas en segundo plano al destruir la actividad
    }

    private void crearSolicitud() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        iniciogoogle = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        if (iniciogoogle != null) {
            Intent signInIntent = iniciogoogle.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            Toast.makeText(this, "hubo errores", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                AutenticacionFirebase(account);

            }catch (ApiException ex){
                Toast.makeText(this , ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void AutenticacionFirebase(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                String uid = user.getUid();
                                String correo = user.getEmail();
                                String nombre = user.getDisplayName();
                                Uri photoUri = user.getPhotoUrl();
                                String foto;

                                if (photoUri != null) {
                                    foto = photoUri.toString();
                                } else {
                                    // En caso de que la URL de la foto sea nula (si el usuario no tiene una foto de perfil configurada)
                                    foto = "";  // o cualquier valor por defecto que desees
                                }

                                HashMap<Object,String> DatosUsuarios = new HashMap<>();
                                DatosUsuarios.put("id" , uid);
                                DatosUsuarios.put("correo", correo);
                                DatosUsuarios.put("pass", "");
                                DatosUsuarios.put("nombre" , "");
                                DatosUsuarios.put("apellios", "");
                                DatosUsuarios.put("telefono" , "");
                                DatosUsuarios.put("edad","");
                                DatosUsuarios.put("direccion","");
                                DatosUsuarios.put("imagen" ,foto);

                            }

                            Intent intent = new Intent(Login.this , Menu.class);
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(Login.this, "Hubo problemas", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}