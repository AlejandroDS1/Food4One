package Food4One.app.View.MainScreen.ui.Perfil;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import Food4One.app.Model.User.User;

public class UserSettingsViewModel extends ViewModel {

    private final String TAG = "UserSettingsActivityViewModel";

    // Variable para guardar un string con tadas las alergias que tiene el usuario
    private final MutableLiveData<String> alergiasText;
    private final MutableLiveData<String> userName;
    private final MutableLiveData<String> description;


    public UserSettingsViewModel(){

        User user = User.getInstance();
        String _alergias = user.getAlergias().toString();

        this.alergiasText = new MutableLiveData<>();
        this.alergiasText.setValue(_alergias.substring(1, _alergias.length() - 1));

        this.userName = new MutableLiveData<>();
        this.userName.setValue(user.getUserName());

        this.description = new MutableLiveData<>();
        this.description.setValue(user.getDescripcion());
    }

    public void configAlergias(String[] alergias_arr, boolean[] selectedAlergies, Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Selecciona tus alergias");

        builder.setMultiChoiceItems(alergias_arr, selectedAlergies, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {

            }
        });

        builder.setCancelable(false);

        // Este boton ejecuta finalmente los cambios, modificando las alergias.
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {

                // Creamos el nuevo arrayList
                ArrayList<String> alergias = new ArrayList<String>();

                // Introducimos o quitamos las nuevas alergias
                for (int i = 0; i < alergias_arr.length; i++)
                    if (selectedAlergies[i]) alergias.add(alergias_arr[i]);

                // Cambiamos las alergias por las nuevas.
                setAlergias(alergias);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {

                ArrayList<String> alergias = User.getInstance().getAlergias();

                for (int i = 0; i < alergias_arr.length; i++){
                    if (alergias.contains(alergias_arr[i])) selectedAlergies[i] = true;
                    else selectedAlergies[i] = false;
                }
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void changeUserNameListener(Context context){
        AlertDialog.Builder build = new AlertDialog.Builder(context);

        build.setTitle("User Name");

        EditText input = new EditText(context);

        input.setText(User.getInstance().getUserName());

        build.setView(input);

        build.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO: COMPROVAR QUE SE PUEDE ACEPTAR ESE NOMBRE DE USUARIO. TMB BASE DE DATOS.
                User.getInstance().setUserName(input.getText().toString());
                setUserName(input.getText().toString());
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = build.create();
        dialog.show();
    }

    public void changeDescriptionListener(Context context) {

        AlertDialog.Builder build = new AlertDialog.Builder(context);

        build.setTitle("Tu descripcion");

        EditText input = new EditText(context);

        input.setMaxLines(10);
        input.setSingleLine(false);
        input.setText(User.getInstance().getDescripcion());
        build.setView(input);

        build.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                User.getInstance().setDescripcion(input.getText().toString());
                setDescription(input.getText().toString());
            }
        });

        AlertDialog dialog = build.create();
        dialog.show();
    }

    //Setters
    public void setUserName(String userName){
        this.userName.setValue(userName);
    }

    public void setDescription(String description){
        this.description.setValue(description);
    }
    public void setAlergiasText(String txt){
        this.alergiasText.setValue(txt);
    }

    private void setAlergias(ArrayList<String> alergias){

        User.getInstance().setAlergias(alergias);

        String _alergias = alergias.toString().substring(1, alergias.toString().length()-1);

        setAlergiasText(_alergias);
    }


    // Getters
    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<String> getDescription() {
        return description;
    }
    public LiveData<String> getAlergiasText() {
        return alergiasText;
    }


}
