package Food4One.app.View.Authentification;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import Food4One.app.R;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth; // Para autentificar al usuario con Firebase

    private FirebaseFirestore fibase = FirebaseFirestore.getInstance(); // Firestiore BDD
    private Button regisBtn;
    private EditText userName, emailUser, passwordUser, passUConfirm;
    private TextView tornaLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // FirebaeAuth object to Sig/Log in.
        auth = FirebaseAuth.getInstance();

        // Init objetos view xml.
        initLayout();

        tornaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });


        regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //COMPROBAR SU CORREO ELECTRONICO
                                    FirebaseUser user = auth.getCurrentUser();
                                    user.sendEmailVerification();
                                    //-------------------------------------------------------
                                    Toast.makeText(getApplicationContext(), "Se ha enviado un correo de Verificación", Toast.LENGTH_SHORT).show();

                                    //Toca crear la información del Usuario.
                                    afegirPerfilCuenta(nameUser, email);

                                    //Cuando vaya bien empezará la ventana Main
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "SignUp Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }

            }
        });

    };

    private void afegirPerfilCuenta(String nameUser, String email) {
        Map<String, Object> perfil = new HashMap<>();
        perfil.put("Email", email);
        perfil.put("Name", nameUser);

        Context context = this.getApplicationContext();

        DocumentReference userInformation = fibase.document("Users/"+perfil.get("Email"));
        userInformation.set(perfil).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Log.d(TAG, "Document added! ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding Document", e);
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