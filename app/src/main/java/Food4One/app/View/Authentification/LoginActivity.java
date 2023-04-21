package Food4One.app.View.Authentification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import Food4One.app.Model.User.User;
import Food4One.app.R;
import Food4One.app.View.MainScreen.MainScreen;
import Food4One.app.ViewModel.MainScreenViewModel;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    //private FirebaseAuth.AuthStateListener authStateListener; //Listener del Usuario
    private EditText userName, passwordUser;
    private Button in; // Boton login
    private TextView registrarse;

    private MainScreenViewModel mainScreenViewModel;
    private Button deleteInvitadosButton; // TODO: Boton para entrar rapido para pruebas rapidas, eliminar, no afecta en nada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //FIREBASE____INFORMATION
        auth = FirebaseAuth.getInstance(); // Obtenemos la instancia de FirebaseAuth

        initLayout(); // Iniciamos comoponentes del layout
        initButtonsListeners(); // Iniciamos los listeners de los botones
    }

    private void initButtonsListeners(){

        //Listener para el botón de Login
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = userName.getText().toString();
                String password = passwordUser.getText().toString();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    if(!password.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                if (authResult.getUser().isEmailVerified()) {
                                    //FIREBASE______INFORMATION_______________________________________________
                                    Toast.makeText(getApplicationContext(), "LoginSuccess", Toast.LENGTH_SHORT).show();
                                    //Cuando vaya bien empezará la ventana Main

                                    //todo: Creamos un único usuario para el dominio del programa
                                    DocumentReference dmReference = FirebaseFirestore.getInstance().document("Users/" + email);

                                    dmReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            // Ahora el user esta guardado en el mainScreenViewModel por lo que deberia de guardarse
                                            //mainScreenViewModel.setUser(new User(documentSnapshot.getString("Name"), email));


                                            // Guardamos el usuario de manera global.
                                            User.getInstance(documentSnapshot.getString("Name"), email);

                                            startActivity(new Intent(LoginActivity.this, MainScreen.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }else
                                    Toast.makeText(getApplicationContext(), "No se ha verificado el correo", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        passwordUser.setError("Debe rellenar la contraseña");
                    }
                else if (email.isEmpty()) {
                    userName.setError("Rellene el Email...");
                }
                else
                    userName.setError("Email inválido");
            }
        });

        //Listener para poder registrarse
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Inicio directamente la ventana para registrarse
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void initLayout(){

        // Eliminar este boton
        deleteInvitadosButton = findViewById(R.id.deleteButtonInivitado);
        deleteInvitadosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Guardamos un usuario prueba en mainScreenVM
                User.getInstance("UserName_Prueba", "UserPrueba@gmail.com");

                ArrayList<String> alergias = new ArrayList<>();

                alergias.add("Frutos Secos");
                alergias.add("Pescado");                alergias.add("Huevo");


                User.getInstance().setAlergias(alergias);

                Toast.makeText(getApplicationContext(), "Sesion como Invitado", Toast.LENGTH_SHORT).show();

                // Pasamos el usuario con un intent a la nueva Activity
                startActivity(new Intent(LoginActivity.this, MainScreen.class));
            }
        });

        //Inicio de los componentes, tomando los valores dentro de la pantalla
        userName = findViewById(R.id.editTextTextEmailAddress);
        passwordUser =  findViewById(R.id.editTextRegisterPassword);
        registrarse = findViewById(R.id.registrarse);
        //Info del button en el Intro para poder acceder a la ventana Login
        in = findViewById(R.id.entrarBtn);
    }

}