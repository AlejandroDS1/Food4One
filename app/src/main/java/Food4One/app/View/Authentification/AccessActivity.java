package Food4One.app.View.Authentification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Food4One.app.Model.User.UserRepository;
import Food4One.app.R;

public class AccessActivity extends AppCompatActivity {

    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        getSupportActionBar().hide();

        //Animación entrada de App (Splash)----------------------------------------------------------
/*
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.desplazar_abajo);
        findViewById(R.id.logo_splashView).setAnimation(animation);
*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Si el usuario ya se registró en la App y ya hizo un login anteriormente, ya puede acceder
                //directamente al navegador Home...
                activityCorrespondent();
                finish();
            }
        }, 700);
    }


    public void activityCorrespondent() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) //Si el Usuario no existe, creamos la ventana LOGIN.
            startActivity(new Intent(AccessActivity.this, LoginActivity.class));
        //Sino, nos dirigimos al Navegador con el Usuario Logeado.
        else {
            UserRepository.getInstance().loadUserFromDDB(user.getEmail(), AccessActivity.this);
            startActivity(new Intent(AccessActivity.this, HelpActivity.class));
        }
    }
}