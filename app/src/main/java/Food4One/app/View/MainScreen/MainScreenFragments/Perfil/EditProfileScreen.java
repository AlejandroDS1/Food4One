package Food4One.app.View.MainScreen.MainScreenFragments.Perfil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Food4One.app.Model.User.UserRepository;
import Food4One.app.R;

public class EditProfileScreen extends AppCompatActivity {
    private PerfilViewModel perfilViewModel;
    private ImageView mLoggedPictureUser;
    private LinearLayout galeryAccess, camaraAccess, eliminarAccess;
    private FrameLayout frameEditProfile;
    private CardView cardCameraGalery;
    private TextView backButton, editButton;
    private Uri mPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_screen);

        perfilViewModel = PerfilViewModel.getInstance();
        initObjectsOnTheView();
        initListenersOfTheViews();
        perfilViewModel.loadPictureOfUser(UserRepository.getUser().getEmail());
        getSupportActionBar().hide();
    }



    private void initListenersOfTheViews() {
        //Abrir la actividad para tomar la foto con la cámara
        setTakeCameraPictureListener(camaraAccess);
        //Abrir la galerìa para poder escoger una foto y subirla
        setChoosePictureListener(galeryAccess);
        //Borrar la imagen del Usuario y colocar una por predeterminada
        setErasePictureListener(eliminarAccess);

        //Regresamos manualmente al Fragment anterior
        backButton.setOnClickListener(v -> {
            finish();
        });

        //Tenemos que escuchar cuando el usuario haya cambiado su photo de perfil
        final Observer<String> observerPictureUrl = new Observer<String>() {
            @Override
            public void onChanged(String pictureUrl) {
                if(pictureUrl == null)
                    mLoggedPictureUser.setImageResource(R.mipmap.ic_launcher_foreground);
                else {
                    if (!pictureUrl.equals(" ")) {
                        Picasso.get()
                                .load(pictureUrl).resize(1000, 1000)
                                .into(mLoggedPictureUser);
                        UserRepository.getUser().setProfilePictureURL(pictureUrl);
                    } else
                        mLoggedPictureUser.setImageResource(R.mipmap.ic_launcher_foreground);
                }
            }
        };
        perfilViewModel.getPictureProfileUrl().observe(this, observerPictureUrl);

        editButton.setOnClickListener(view->{
            cardCameraGalery.setVisibility(View.VISIBLE);
            Animation a = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            cardCameraGalery.startAnimation(a);
            editButton.setVisibility(View.GONE);
        });

        frameEditProfile.setOnClickListener(view->{
            if(editButton.getVisibility() == View.GONE) {
                Animation a = AnimationUtils.loadAnimation(this, R.anim.slide_out);
                cardCameraGalery.startAnimation(a);
                cardCameraGalery.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setErasePictureListener(LinearLayout eliminarAccess) {
        eliminarAccess.setOnClickListener(view ->{
            UserRepository.getInstance().setPictureUrlOfUser(UserRepository.getUser().getEmail(), " ");
            perfilViewModel.setmPictureUrl(" ");
        });
    }

    private void initObjectsOnTheView() {

        camaraAccess = findViewById(R.id.camaraSelection);
        galeryAccess = findViewById(R.id.galerySelection);
        eliminarAccess = findViewById(R.id.eliminarSelection);
        backButton = findViewById(R.id.backflecha);
        editButton = findViewById(R.id.editPictureButton);
        mLoggedPictureUser = findViewById(R.id.photoAvatarFullScreen);

        frameEditProfile = findViewById(R.id.photoFrameEditProfile);
        cardCameraGalery = findViewById(R.id.cardEditCameraGalery);
        cardCameraGalery.setVisibility(View.GONE);
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
                                UserRepository.getUser().getEmail(), contentUri);
                        Toast.makeText(this, "Se ha modificado la foto de Perfil", Toast.LENGTH_SHORT).show();
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
                                    UserRepository.getUser().getEmail(), mPhotoUri
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

            File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

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
            Uri photoUri = FileProvider.getUriForFile(this,
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