package Food4One.app.View.Authentification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Food4One.app.Model.User.UserRepository;
import Food4One.app.R;
import Food4One.app.View.MainScreen.MainScreen;

public class AccessActivity extends AppCompatActivity {

    private FirebaseUser user;

    private AccesActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        viewModel = new ViewModelProvider(this).get(AccesActivityViewModel.class);

        getSupportActionBar().hide();

        initObservers();

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
                //finish();
            }
        }, 700);
    }

    private void initObservers() {
        final Observer<Boolean> completedObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    startActivity(new Intent(AccessActivity.this, MainScreen.class));
            }
        };
        viewModel.getCompleted().observe(this, completedObserver);
    }


    public void activityCorrespondent() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) //Si el Usuario no existe, creamos la ventana LOGIN.
            startActivity(new Intent(AccessActivity.this, LoginActivity.class));
        //Sino, nos dirigimos al Navegador con el Usuario Logeado.
        else {
            UserRepository.getInstance().loadUserFromDDB(user.getEmail(), AccessActivity.this, viewModel);
        }
    }
}