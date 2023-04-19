package com.example.apptry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccessActivity extends AppCompatActivity {

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Si el usuario ya se registró en la App y ya hizo un login anteriormente, ya puede acceder
        //directamente al navegador Home...

        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) //Si el Usuario no existe, creamos la ventana LOGIN.
            startActivity(new Intent(AccessActivity.this, LoginActivity.class));
        //Sino, nos dirigimos al Navegador con el Usuario Logeado.
        startActivity(new Intent(AccessActivity.this, NavMainActivity.class));
    }
}