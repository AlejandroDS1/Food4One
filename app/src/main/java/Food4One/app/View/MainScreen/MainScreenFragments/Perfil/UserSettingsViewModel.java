package Food4One.app.View.MainScreen.MainScreenFragments.Perfil;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import Food4One.app.Model.User.User;
import Food4One.app.Model.User.UserRepository;

public class UserSettingsViewModel extends ViewModel {

    private final String TAG = "UserSettingsActivityViewModel";

    // Variable para guardar un string con tadas las alergias que tiene el usuario
    private final MutableLiveData<String> alergiasText;
    private final MutableLiveData<String> userName;
    private final MutableLiveData<String> description;

    public UserSettingsViewModel(){

        User user = UserRepository.getUser();
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

                // Cambiamos las alergias por las nuevas y las añadimos a la base de datos.
                UserRepository.getInstance().setUserAlergiasDDB(UserRepository.getUser().getEmail(), alergias, alergiasText);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {

                ArrayList<String> alergias = UserRepository.getUser().getAlergias();

                for (int i = 0; i < alergias_arr.length; i++)
                    selectedAlergies[i] = alergias.contains(alergias_arr[i]);

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

        input.setText(UserRepository.getUser().getUserName());

        build.setView(input);

        build.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final String _newUserName = input.getText().toString();

                // Si el nombre es el mismo que el anterior salimos del metodo y no actualizamos la base de datos
                if (UserRepository.getUser().userName.equals(_newUserName)) return;

                // Comprovamos si el nombre tiene almenos un caracter, sino no se puede tener un nombre vacio
                if (_newUserName.isEmpty()) {
                    Toast.makeText(context, "Rellena el campo", Toast.LENGTH_SHORT).show();
                    return; // Salimos para no actualizar el texto
                }

                // Actualizamos el userName en la base de datos.
                boolean succes = UserRepository.getInstance().setUserNameDDB(UserRepository.getUser().getEmail(),
                        _newUserName);

                // Si el anterior metodo a dado como resultado true, esque se a cambiado el userName correctamente
                // por lo que podemos actulizar el MutableLiveData para notificar al observer
                if (succes) userName.setValue(_newUserName);
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
        input.setText(UserRepository.getUser().getDescripcion());
        build.setView(input);

        build.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (description.getValue().equals(input.getText().toString())) return;

                UserRepository.getInstance().setUserDescriptionDDB(UserRepository.getUser().getEmail(), input.getText().toString(), description);
            }
        });

        build.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
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
