package Food4One.app.View.MainScreen;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Food4One.app.Model.User.User;
import Food4One.app.R;
import Food4One.app.ViewModel.MainScreenViewModel;
import Food4One.app.databinding.ActivityMainScreenBinding;

public class MainScreen extends AppCompatActivity {
    private ActivityMainScreenBinding binding;
    private User user;

    private MainScreenViewModel mainScreenViewModel;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Conseguimos el Layout con el binding
        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        prueba();
    }

    private void prueba(){

        // Conseguimos el user desde el intent que hemos pasado
        user = (User) getIntent().getSerializableExtra(User.TAG);

        // Tendriamos que crear al mainScreenViewModel y pasarle el User
        //mainScreenViewModel = new ViewModelProvider(this).get(MainScreenViewModel.class);
        //mainScreenViewModel.setUser(user);

        /*TextView t = findViewById(R.id.DebugText);

        if (user == null){
            t.setText("User is null");
            return;
        }

        t.setText("El user name es: " + user.getUserName() + "\nDebugText");*/

    }
    private void init(){
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_newRecipie, R.id.navigation_recipies, R.id.navigation_explore, R.id.navigation_coleccion, R.id.navigation_perfil)
                .build();

        getSupportActionBar().hide();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_screen);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }
}