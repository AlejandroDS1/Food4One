package Food4One.app.View.MainScreen.ui.Perfil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import Food4One.app.Model.Alergias;
import Food4One.app.R;
import Food4One.app.databinding.ActivityUserSettingsBinding;

public class UserSettingsActivity extends AppCompatActivity {


    private ActivityUserSettingsBinding binding;
    private LinearLayout linearLayout;

    boolean [] selectedAlergies = new boolean[10];
    ArrayList<Integer> intAlergiasList = new ArrayList<>();


    String [] alergias_arr= {"GLUTEN","HUEVO", "LACTEOS", "MARISCO", "SOJA", "TRIGO", "MAIZ", "PESCADO", "FRUTOS_SECOS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prueba();
    }

    private void prueba() {

        linearLayout = binding.layoutAlergiasSettings;

        linearLayout.setOnClickListener(v -> {

            showCursesDialog();
        });

    }


    private void showCursesDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(UserSettingsActivity.this);

        builder.setTitle("Que alergias tienes?");


        builder.setMultiChoiceItems(alergias_arr, selectedAlergies, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {

                if(isChecked) intAlergiasList.add(i);
                else intAlergiasList.remove(i);
            }
        });

        builder.setCancelable(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();

    }
}