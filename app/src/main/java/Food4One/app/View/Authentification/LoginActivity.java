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
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import Food4One.app.MainActivity;
import Food4One.app.Model.User.User;
import Food4One.app.Model.User.UserRepository;
import Food4One.app.R;
import Food4One.app.View.MainScreen.MainScreen;
import Food4One.app.ViewModel.MainScreenViewModel;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText userName, passwordUser;
    private Button in;
    private TextView registrarse;
    private FirebaseUser currentUser;
    /*En este atributo de guardan a todos los usuarios logeados de la App,
     * cuando el usuario consiga entrar a la App, lo guardaremos en este atributo y en
     * la base de datos de la App */
    private UserRepository mUserRespository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //FIREBASE ____INFORMATION_________
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();// Obtenemos la instancia de FirebaseAuth
        mUserRespository = UserRepository.getInstance();

        initLayout(); // Iniciamos comoponentes del layout
        initButtonsListeners(); // Iniciamos los listeners de los botones

    }

    private void initButtonsListeners() {
        //Listener para el botón de Login
        in.setOnClickListener(view -> {
            String email = userName.getText().toString();
            String password = passwordUser.getText().toString();

            //Nos aseguramos de que el Email no esté vacío y tenga un formato correcto
            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (!password.isEmpty()) {

                    //Intentamos acceder a la App con el correo y contraseña dadas por el Usuario
                    auth.signInWithEmailAndPassword(email, password).
                            addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    if (authResult.getUser().isEmailVerified()) {
                                        Toast.makeText(getApplicationContext(), "LoginSuccess", Toast.LENGTH_SHORT).show();

                                        /*Ahora que es seguro que el usuario existirá en la App,
                                         *lo añadimos a los demás usuarios guardados en el Respository de la App*/
                                        afegirPerfilCuenta(getIntent().getStringExtra("nameUser"), auth.getCurrentUser().getEmail());
                                        //Cuando vaya bien empezará la ventana Main
                                        startActivity(new Intent(LoginActivity.this, MainScreen.class));
                                        finish();
                                    } else
                                        Toast.makeText(getApplicationContext(), "No se ha verificado el correo", Toast.LENGTH_SHORT).show();

                                }
                                //EN el caso de que no se pueda hacer el Login se muestra por pantalla el fallo--------------------------
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else  //SI la contraaseña está vacía al momento del LOGIN
                    passwordUser.setError("Debe rellenar la contraseña");

            } else if (email.isEmpty())
                userName.setError("Rellene el Email...");

            else //EMAIL no Válido para el LOGIN
                userName.setError("Email inválido");
        });

        //Listener para poder registrarse
        registrarse.setOnClickListener(view-> {
            //Inicio directamente la ventana para registrarse
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        });
    }


    private void initLayout() {

        //Inicio de los componentes, tomando los valores dentro de la pantalla
        userName = findViewById(R.id.editTextTextEmailAddress);
        passwordUser =  findViewById(R.id.editTextRegisterPassword);
        registrarse = findViewById(R.id.registrarse);
        in = findViewById(R.id.entrarBtn);

    }

    /*Este método se llama después de haber pasado a otras ventanas, en nuestro caso sería el
    * RegisterActivity. Si allí nos registramos, al regresar a este View, podemos llenar le corre
    * para que el acceso del usuario sea más fácil y rápido...*/
    @Override
    protected void onRestart() {
        super.onRestart();
        entryUser();
        if(currentUser != null)
                userName.setText(currentUser.getEmail());
    }

    public void entryUser(){
        /*EN el caso de que el usuario no haya hecho un SING OUT después de entrar a la app,
        el programa empezará directamente en el HomeFragment*/
        if( currentUser != null && currentUser.isEmailVerified() )
            startActivity(new Intent(LoginActivity.this, MainScreen.class));
    }


    /**
     * Método que añade a la Base de Datos la entrada a los datos del Usuario.
     * @param nameUser Nombre del Usuario
     * @param email Entrada a los datos del Usuario
     */
    private void afegirPerfilCuenta(String nameUser, String email) {

        /*EL usuario inicialmente guarda en la base de datos su nombre y descrpición, ya a medida que use la app
        se irán añadiendo los demás datos que cree, como las recetas de su perfil*/
        mUserRespository.addUser(nameUser, email); //Lo añadimos a la base de datos...
    }
}