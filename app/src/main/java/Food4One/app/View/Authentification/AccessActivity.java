package Food4One.app.View.Authentification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import Food4One.app.Model.User.User;
import Food4One.app.Model.User.UserRepository;
import Food4One.app.R;
import Food4One.app.View.MainScreen.MainScreen;

public class AccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        getSupportActionBar().hide();

        UserRepository.getInstance().addOnLoadUsersListener(new UserRepository.OnLoadUsersListener() {
            @Override
            public void onLoadUsers(ArrayList<User> users) {
                startActivity(new Intent(AccessActivity.this, MainScreen.class));
            }
        });

        //Animación entrada de App (Splash)----------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Si el usuario ya se registró en la App y ya hizo un login anteriormente, ya puede acceder
                //directamente al navegador Home...
                activityCorrespondent();

            }
        }, 500);
    }


    public void activityCorrespondent() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) //Si el Usuario no existe, creamos la ventana LOGIN.
            startActivity(new Intent(AccessActivity.this, LoginActivity.class));
        //Sino, nos dirigimos al Navegador con el Usuario Logeado.
        else
            UserRepository.getInstance().loadUserFromDDB(user.getEmail());

    }
}