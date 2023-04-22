package Food4One.app.View.MainScreen.ui.Perfil;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import Food4One.app.Model.User.User;
import Food4One.app.R;
import Food4One.app.databinding.ActivityUserSettingsBinding;

public class UserSettingsActivity extends AppCompatActivity {

    private UserSettingsViewModel userSettingsViewModel;
    private ActivityUserSettingsBinding binding;
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
                binding.arrUsrAlergiasSettings.setText(userSettingsViewModel.getAlergiasText().getValue());
            }
        };

        userSettingsViewModel.getAlergiasText().observe(this, alergiasObserver);

        //Observer del userName
        final Observer<String> userNameObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.userNameSettings.setText(userSettingsViewModel.getUserName().getValue());
            }
        };
        userSettingsViewModel.getUserName().observe(this, userNameObserver);

        //Observer de la descripcion del usuario
        final Observer<String> descripcionObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.descripcionSettings.setText(userSettingsViewModel.getDescription().getValue());
            }
        };
        userSettingsViewModel.getDescription().observe(this, descripcionObserver);

    }

    private void initLayout(){
        // Iniciamos todos los textos a su respectivo nombre.

        // Descripcion del usuario
        binding.descripcionSettings.setText(User.getInstance().getDescripcion());

        // UserName
        binding.userNameSettings.setText(User.getInstance().getUserName());

        // ClickListener al Layout de UserName para tener AlertDialog que cambie el username
        binding.layoutUseNameSettings.setOnClickListener(v -> {
            userSettingsViewModel.changeUserNameListener(UserSettingsActivity.this);
        });

        binding.layoutDescripcionSettings.setOnClickListener(v -> {
            userSettingsViewModel.changeDescriptionListener(UserSettingsActivity.this);
        });

    }
    private void configAlergias() {

        // Config text view de las alergias del usuario
        binding.arrUsrAlergiasSettings.setText(User.getInstance().getAlergias().toString());

        alergias_arr = getResources().getStringArray(R.array.Alergias);

        selectedAlergies = User.getInstance().getBooleanArrayAlergias(alergias_arr);

        linearLayout = binding.layoutAlergiasSettings;

        linearLayout.setOnClickListener(v -> {
            userSettingsViewModel.configAlergias(alergias_arr, selectedAlergies, UserSettingsActivity.this);
        });

    }

}
