package Food4One.app.View.MainScreen.MainScreenFragments.Perfil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipesUserApp;
import Food4One.app.Model.User.User;
import Food4One.app.R;
import Food4One.app.View.Authentification.LoginActivity;
import Food4One.app.databinding.FragmentPerfilBinding;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Perfil extends Fragment {

    private final String TAG = "Perfil_Fragment";
    private FragmentPerfilBinding binding;
    private ImageView mTakePictureButton;//Editar la foto del Perfil
    private ImageView mLoggedPictureUser;//Foto del Usuario
    private Uri mPhotoUri;
    private PerfilViewModel perfilViewModel;
    private RecyclerView mRecetaCardsRV;
    private RecetaPerfilAdapter mCardRecetaRVAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser userFirebase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        userFirebase= mAuth.getCurrentUser();
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        perfilViewModel = PerfilViewModel.getInstance();

        cargarObjectsView();
        //El usuario tiene sus datos en la pantalla de Perfil, hay que cargarlos de la BDD
        cargarUsuarioDeBaseDatos();

        recycleViewGrid();//Instancia del Recycle View(Grid) que contendrá las recetas

        clickListenerObjectsView();
        setTakeCameraPictureListener(mTakePictureButton);

        observerObjectsView();

        return root;
    }

    private void clickListenerObjectsView() {

        //Se carga los procesos que realiza el fragmento...
        binding.logoutButn.setOnClickListener(view-> {
            mAuth.signOut();
            User.logOutUser();
            binding.getRoot().getContext().startActivity(
                    new Intent(binding.getRoot().getContext(), LoginActivity.class));
        });

        binding.editarPerfilBtn.setOnClickListener(view -> {
            startActivity(new Intent(getActivity().getApplicationContext(), UserSettingsActivity.class));
        });

        binding.avatarusuario.setOnClickListener(v->{
            initEditPerfilWindow();
        });

        /*-------------------------------------------------------------------------------------*/

        //Si tenim un usuari logat, alashores pot tenir una foto de Perfil...

        final Observer<String> observerPictureUrl = new Observer<String>() {
            @Override
            public void onChanged(String pictureUrl) {
                Picasso.get()
                        .load(pictureUrl).resize(200, 200)
                        .into(mLoggedPictureUser);
                User.getInstance().setProfilePictureURL(pictureUrl);
            }
        };
        perfilViewModel.getPictureProfileUrl().observe(this.getActivity(), observerPictureUrl);
    }

    private void observerObjectsView() {
        // Observer a Perfil per veure si la llista de Receta (observable MutableLiveData)
        // a PerfilViewModel ha canviat.
        final Observer<ArrayList<Recipe>> observerRecetes= new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recetas) {
                mCardRecetaRVAdapter.notifyDataSetChanged();
            }
        };
        perfilViewModel.getRecetes().observe(this.getViewLifecycleOwner(), observerRecetes);

        final Observer<String> observeNameUser = new Observer<String>() {
            @Override
            public void onChanged(String name) {
                binding.nomusuari.setText(name);
            }
        };
        perfilViewModel.getmUserName().observe(this.getActivity(), observeNameUser);
        final Observer<String> observerDescription = new Observer<String>() {
            @Override
            public void onChanged(String description) {
                binding.descripcionPerfil.setText(description);
            }
        };
        perfilViewModel.getmDescription().observe(this.getActivity(), observerDescription);
    }

    private void recycleViewGrid() {
        mRecetaCardsRV = binding.getRoot().findViewById(R.id.fotosperfil_RV);

        //Ahora le definimos un Manager Grid
        GridLayoutManager manager = new GridLayoutManager(
                this.getContext(), GridLayoutManager.VERTICAL);
        manager.setSpanCount(3); //Number of columns.
        mRecetaCardsRV.setLayoutManager(manager);

        //Luego instanciamos el Adapter de las fotos
        mCardRecetaRVAdapter = new RecetaPerfilAdapter(
                perfilViewModel.getRecetes().getValue(), getActivity());

        //Para las operaciones de las imagenes en el perfil...
        mCardRecetaRVAdapter.setOnClickDetailListener(new RecetaPerfilAdapter.OnClickDetailListener() {
            @Override
            public void OnClickDetail(int position) {
                //Al clicar se abrirá un nuevo Fragment
                initScrollViewRecipes(position);
            }
        });

        mRecetaCardsRV.setAdapter(mCardRecetaRVAdapter);
    }

    private void cargarObjectsView() {
        mLoggedPictureUser = binding.avatarusuario;
        mTakePictureButton = binding.photobuttomPerfil;
    }

    private void initScrollViewRecipes(int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("RecycleViewPosition", position);

        // Create new fragment and transaction
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true)
                .addToBackStack("PerfilFragChange") ;
        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.perfilFragment, ScrollPerfil.class, bundle);
        // Commit the transaction
        transaction.commit();

    }
    /**
     * Por ahora este método sólo carga el nombre, email y la descripción del Usuario, si queremos
     * guardar más datos del usuario, ya sea su edad, hobbies, o gustos culinarios, lo guardaríamos
     * en la base de datos y lo cargaríamos con este método...
     */
    private void cargarUsuarioDeBaseDatos() {
        User userInfo = User.getInstance();
        binding.nomusuari.setText(userInfo.getUserName());
        binding.descripcionPerfil.setText(userInfo.getDescripcion());

        perfilViewModel.loadPictureOfUser(userInfo.getEmail());
        if(RecipesUserApp.getInstance().size() == 0) //Si aún no se cargaron las recetas del usuario
            perfilViewModel.loadRecetasOfUserFromRepository(User.getInstance().getIdRecetas());
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
                                    userFirebase.getEmail(), mPhotoUri
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


    private void initEditPerfilWindow() {
        // Create new fragment and transaction
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true)
                .addToBackStack("PerfilFragChange") ;
        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.perfilFragment, new EditarPerfilScreen());
        // Commit the transaction
        transaction.commit();
    }


    @Override
    public void onResume() {
        super.onResume();
        //Pequeño tiempo antes de borrar la barra de Cargando...
        //try { Thread.sleep(700); } catch (InterruptedException e) { throw new RuntimeException(e);}
        binding.progressBarPerfil.setVisibility(View.GONE);
    }

}