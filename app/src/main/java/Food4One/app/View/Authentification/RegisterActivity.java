package Food4One.app.View.Authentification;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import Food4One.app.Model.User.UserRepository;
import Food4One.app.R;
public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    //GUARDAR TODA LA INFORMACIÓN DEL USUARIO
    private FirebaseFirestore fibase = FirebaseFirestore.getInstance();
    private Button regisBtn;
    private EditText userName, emailUser, passwordUser, passUConfirm;
    private TextView tornaLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        // FirebaeAuth object to Sig/Log in.
        auth = FirebaseAuth.getInstance();

        // Init objetos view xml.
        initLayout();

        tornaLogin.setOnClickListener(view-> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

        regisBtn.setOnClickListener(view-> {

            if (userName.length() == 0 || passwordUser.length() == 0|| emailUser.length()==0 )
                //Mensaje que muestra en pantalla que el Registro ha resultado en un fracaso.
                Toast.makeText(getApplicationContext(), "Rellena todos los espacios para iniciar", Toast.LENGTH_SHORT).show();

            else if (! passwordUser.getText().toString().equals(passUConfirm.getText().toString())) {
                //Si no se ha confirmado correctamente la contraseña se muestra un mensaje avisando al usuario
                Toast.makeText(getApplicationContext(), "Error al confirmar la contraseña", Toast.LENGTH_SHORT).show();

            }else{
                String nameUser = userName.getText().toString();
                String email = emailUser.getText().toString();
                String pass = passwordUser.getText().toString();
                //FIREBASE______INFORMATION_______________________________________________
                auth.createUserWithEmailAndPassword(email, passwordUser.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //COMPROBAR SU CORREO ELECTRONICO
                                    auth.getCurrentUser().sendEmailVerification();
                                    //-------------------------------------------------------
                                    Toast.makeText(getApplicationContext(),"Se ha enviado un correo de Verificación",Toast.LENGTH_SHORT).show();

                                    /*Teniendo la supuesta cuenta del usuario creada, habrá que esperar
                                     * a que confirme su correo. Por lo tanto, aún no lo añadiremos a
                                     * la base de datos, sino que almacenaremos el nombre del usuario
                                     * en cuestión en el Intento, y se lo pasamos así al LOGIN*/

                                    UserRepository.getInstance().addUser(nameUser,email);

                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent.putExtra("nameUser", nameUser);
                                    startActivity(intent);    //Vamos al LOGIN a verificar al usuario.

                                } else {
                                    Toast.makeText(getApplicationContext(), "Asegúrate de no tener espacios en el Gmail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


    private void initLayout(){
        tornaLogin = findViewById(R.id.backLogin);
        userName =  findViewById(R.id.editTextRegisterName);
        emailUser = findViewById(R.id.editTextRegisterMail);
        passwordUser = findViewById(R.id.editTextRegisterPassword);
        passUConfirm = findViewById(R.id.editTextConfitmPassword);
        regisBtn = findViewById(R.id.registrarseBtn);
    }

}