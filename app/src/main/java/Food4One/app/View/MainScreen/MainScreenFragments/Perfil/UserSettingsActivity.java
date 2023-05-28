package Food4One.app.View.MainScreen.MainScreenFragments.Perfil;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import Food4One.app.Model.User.User;
import Food4One.app.Model.User.UserRepository;
import Food4One.app.R;
import Food4One.app.databinding.ActivityUserSettingsBinding;

public class UserSettingsActivity extends AppCompatActivity {

    private UserSettingsViewModel userSettingsViewModel;
    private ActivityUserSettingsBinding binding;
    private PerfilViewModel perfilViewModel;
    private LinearLayout linearLayout;
    boolean [] selectedAlergies;
    String [] alergias_arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        // Conseguimos el objeto viewmodel para relizar operaciones
        userSettingsViewModel = new ViewModelProvider(this).get(UserSettingsViewModel.class);
        perfilViewModel = PerfilViewModel.getInstance();

        setObservers();

        initLayout();
        configAlergias();
    }

    private void setObservers(){

        // Inicio todos los observers

        //Observer de alergiastxt
        final Observer<String> alergiasObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.arrUsrAlergiasSettings.setText(s);
            }
        };

        userSettingsViewModel.getAlergiasText().observe(this, alergiasObserver);

        //Observer del userName
        final Observer<String> userNameObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) { binding.userNameSettings.setText(s); }
        };
        userSettingsViewModel.getUserName().observe(this, userNameObserver);
        perfilViewModel.getmUserName().observe(this, userNameObserver);

        //Observer de la descripcion del usuario
        final Observer<String> descripcionObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.descripcionSettings.setText(s);
            }
        };
        userSettingsViewModel.getDescription().observe(this, descripcionObserver);
        perfilViewModel.getmDescription().observe(this, descripcionObserver);

    }

    private void initLayout(){

        // Iniciamos todos los textos a su respectivo nombre.

        // Descripcion del usuario
        binding.descripcionSettings.setText(UserRepository.getUser().getDescripcion());

        // UserName
        binding.userNameSettings.setText(UserRepository.getUser().getUserName());

        // ClickListener al Layout de UserName para tener AlertDialog que cambie el username
        binding.layoutUseNameSettings.setOnClickListener(v -> {
            userSettingsViewModel.changeUserNameListener(UserSettingsActivity.this);
        });

        binding.layoutDescripcionSettings.setOnClickListener(v -> {
            userSettingsViewModel.changeDescriptionListener(UserSettingsActivity.this);
        });
        binding.premiumButton.setVisibility(View.GONE);

        binding.layoutPasswordSettings.setOnClickListener(v->{
            FirebaseAuth.getInstance().sendPasswordResetEmail(UserRepository.getUser().getEmail())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                        Toast.makeText(getApplicationContext(), "Correo de Cambio de contraseña enviado", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    private void configAlergias() {

        // Config text view de las alergias del usuario
        ArrayList<String> alergias = UserRepository.getUser().getAlergias();
        if(alergias!=null)
            binding.arrUsrAlergiasSettings.setText(alergias.toString());

        alergias_arr = getResources().getStringArray(R.array.Alergias);

        selectedAlergies = UserRepository.getUser().getBooleanArrayAlergias(alergias_arr);

        linearLayout = binding.layoutAlergiasSettings;

        linearLayout.setOnClickListener(v -> {
            userSettingsViewModel.configAlergias(alergias_arr, selectedAlergies, UserSettingsActivity.this);
        });
    }
}