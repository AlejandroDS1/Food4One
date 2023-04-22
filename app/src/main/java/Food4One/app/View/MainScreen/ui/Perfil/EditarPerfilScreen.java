package Food4One.app.View.MainScreen.ui.Perfil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Food4One.app.Model.User.User;
import Food4One.app.databinding.FragmentEditarPerfilScreenBinding;

public class EditarPerfilScreen extends Fragment {
    private FragmentEditarPerfilScreenBinding binding;
    private PerfilViewModel perfilViewModel;
    private ImageView mLoggedPictureUser;
    private LinearLayout galeryAccess;
    private LinearLayout camaraAccess;
    private TextView backButton;
    private Uri mPhotoUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding= FragmentEditarPerfilScreenBinding.inflate(inflater, container, false);

        perfilViewModel = PerfilViewModel.getInstance();

        initObjectsOnTheView();
        initListenersOfTheViews();

        perfilViewModel.loadPictureOfUser(User.getInstance().getEmail());

        return binding.getRoot();
    }

    private void initListenersOfTheViews() {
        setTakeCameraPictureListener(camaraAccess);
        setChoosePictureListener(galeryAccess);
        backButton.setOnClickListener(v -> {//Regresamos manualmente al Fragment anterior
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        });

        //Tenemos que escuchar cuando el usuario haya cambiado su photo de perfil
        final Observer<String> observerPictureUrl = new Observer<String>() {
            @Override
            public void onChanged(String pictureUrl) {
                Picasso.get()
                        .load(pictureUrl).resize(1000, 1000)
                        .into(mLoggedPictureUser);
                User.getInstance().setProfilePictureURL(pictureUrl);
            }
        };
        perfilViewModel.getPictureProfileUrl().observe(this.getActivity(), observerPictureUrl);
    }

    private void initObjectsOnTheView() {
        camaraAccess = binding.camaraSelection;
        galeryAccess = binding.galerySelection;
        backButton = binding.backflecha;
        mLoggedPictureUser = binding.photoAvatarFullScreen;
    }


    private void setChoosePictureListener(@NonNull View choosePicture) {
        // Codi que s'encarrega de rebre el resultat de l'intent de seleccionar foto de galeria
        // i que es llençarà des del listener que definirem a baix.
        ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri contentUri = data.getData(); // En aquest intent, sí que hi arriba la URI

                        perfilViewModel.setPictureUrlOfUser(
                                User.getInstance().getEmail(), contentUri);
                    }
                });

        // Listener del botó de seleccionar imatge, que llençarà l'intent amb l'ActivityResultLauncher.
        choosePicture.setOnClickListener(view -> {
            Intent data = new Intent(Intent.ACTION_GET_CONTENT);
            data.addCategory(Intent.CATEGORY_OPENABLE);
            data.setType("image/*");
            Intent intent = Intent.createChooser(data, "Choose a file");

            startActivityForResult.launch(intent);
        });
    }


    private void setTakeCameraPictureListener(@NonNull View takePictureView) {
        // Codi que s'encarrega de rebre el resultat de l'intent de fer foto des de càmera
        // i que es llençarà des del listener que definirem a baix.
        ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            perfilViewModel.setPictureUrlOfUser(
                                    User.getInstance().getEmail(), mPhotoUri
                            );
                        }
                    }
                }
        );
        // Listener del botó de fer foto, que llençarà l'intent amb l'ActivityResultLauncher.
        takePictureView.setOnClickListener(view -> {
            // Crearem un nom de fitxer d'imatge temporal amb una data i hora i format JPEG
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            // Anem a buscar el directori extern (del sistema) especificat per la variable
            // d'entorn Environment.DIRECTORY_PICTURES (pren per valor "Pictures").
            // Se li afageix, com a sufix, el directori del sistema on es guarden els fitxers.

            File storageDir = this.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            // Creem el fitxer
            File image = null;
            try {
                image = File.createTempFile(
                        imageFileName,  /* Prefix */
                        ".jpg",         /* Sufix */
                        storageDir      /* Directori on es guarda la imatge */
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Recuperem la Uri definitiva del fitxer amb FileProvider (obligatori per seguretat)
            // Per a fer-ho:
            // 1. Especifiquem a res>xml>paths.xml el directori on es guardarà la imatge
            //    de manera definitiva.
            // 2. Afegir al manifest un provider que apunti a paths.xml del pas 1
            Uri photoUri = FileProvider.getUriForFile(this.getContext(),
                    "Food4One.app.fileprovider",
                    image);

            // Per tenir accés a la URI de la foto quan es llenci l'intent de la camara.
            // Perquè encara que li passem la photoUri com a dades extra a l'intent, aquestes
            // no tornen com a resultat de l'Intent.
            mPhotoUri = photoUri;

            // Llancem l'intent amb el launcher declarat al començament d'aquest mateix mètode
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mPhotoUri);
            takePictureLauncher.launch(intent);
        });
    }
}