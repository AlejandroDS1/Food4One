package Food4One.app.View.MainScreen.ui.Perfil;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import Food4One.app.Model.Recipie.Recipie.Recipe;
import Food4One.app.Model.Recipie.Recipie.RecipeRepository;
import Food4One.app.Model.User.User;
import Food4One.app.Model.User.UserRepository;

public class PerfilViewModel extends AndroidViewModel {

    private final String TAG = "FotosmeActivityViewModel";

    private FirebaseStorage mStorage;

    /* Elements observables del ViewModel */
    private final MutableLiveData<ArrayList<Recipe>> mRecetas; // Els usuaris que la RecyclerView mostra al home

    private final MutableLiveData<ArrayList<User>> mUsers;
    private final MutableLiveData<String> mPictureUrl; // URL de la foto de l'usuari logat
    private final MutableLiveData<String> mText;

    /*Repositori (base de dades) de les recetes-Details*/
    private RecipeRepository mRecetaRepository;
    private UserRepository mUserRepository;
    private ProgressBar progressBar;

    public PerfilViewModel(Application application) {
        super(application);

        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fra1gment");

        mUsers = new MutableLiveData<>(new ArrayList<>());
        mStorage = FirebaseStorage.getInstance();
        mRecetaRepository = RecipeRepository.getInstance();
        mRecetas = new MutableLiveData<>(new ArrayList<>());
        mPictureUrl = new MutableLiveData<>();
        mUserRepository = UserRepository.getInstance();

        userPictureliteners();
        recetasPictureListeners();
    }

    private void recetasPictureListeners() {
        //Al cargar las recetas el observador será notificado y se acutalizará la lista
        mRecetaRepository.addOnLoadRecetaListener(new RecipeRepository.OnLoadRecetaListener() {
            @Override
            public void onLoadRecetas(ArrayList<Recipe> recetas) {
                PerfilViewModel.this.setRecetes(recetas);
            }
        });

        /*mRecetaRepository.setOnLoadUserPictureListener(new RecetaUserRepository.OnLoadRecetaPictureUrlListener() {
            @Override
            public void OnLoadRecetaPictureUrl(String pictureUrl) {
                mPictureUrl.setValue(pictureUrl);
            }
        });*/
    }

    private void userPictureliteners() {
        // Quan s'acabin de llegir de la BBDD els usuaris, el ViewModel ha d'actualitzar
        // l'observable mUsers. I com que la RecyclerView de la HomeActivity està observant aquesta
        // mateixa variable (mUsers), també se n'enterarà
        mUserRepository.addOnLoadUsersListener(new UserRepository.OnLoadUsersListener() {
            @Override
            public void onLoadUsers(ArrayList<User> users) {
                PerfilViewModel.this.setUsers(users);
            }
        });

        // Quan s'acabi de llegir la URL de la foto de perfil de l'usuari logat, el ViewModel
        // actualitza també mPictureUrl, per a que el PerfilFragment la mostri en l'ImageView
        // corresponent
        mUserRepository.setOnLoadUserPictureListener(new UserRepository.OnLoadUserPictureUrlListener() {

            //Basicamente, cuando se cargue de la base de datos, aquí se pondrá la imagen en el Fragment
            @Override
            public void OnLoadUserPictureUrl(String pictureUrl) {
                //Si hay una foto en la base de datos la cambiamos, sino, dejamos la predeterminada
                if(pictureUrl!=null) mPictureUrl.setValue(pictureUrl);
            }
        });
    }

    /*
     * Retorna les recetes perquè PerfilFragment pugui subscriure-hi l'observable.
     */
    public LiveData<ArrayList<Recipe>> getRecetes() {
        return mRecetas;
    }
    /*
     * Retorna el LiveData de la URL de la foto per a què PerfilFragment
     * pugui subscriure-hi l'observable.
     */
    public LiveData<String> getPictureProfileUrl() {
        return mPictureUrl;
    }

    public void loadIDRecetasUser(String email, User user){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                ArrayList<String> recetasID = new ArrayList<>((ArrayList<String>) document.get("idRecetasUser"));
                for (String id : recetasID)
                    user.addIdReceta(id);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("No se pudieron cargar los id's");
            }
        });
    }
    public void setRecetes(ArrayList<Recipe> recetas) {
        mRecetas.setValue(recetas);
    }

    public void setUsers(ArrayList<User> users){
        mUsers.setValue(users);
    }

    public void loadPictureOfReceta() {
        mRecetaRepository.loadPictureOfReceta();
    }


    public void loadPictureOfUser(String email){ mUserRepository.loadPictureOfUser(email);}

    public void loadRecetesFromRepository(){
        mRecetaRepository.loadRecetas(mRecetas.getValue());
    }

    public void loadRecetasOfUserFromRepository(ArrayList<String > idRecetasUser){
        mRecetaRepository.loadRecetasUser(mRecetas.getValue(), idRecetasUser);
    }
    public void setPictureUrlOfUser(String userId, Uri imageUri) {

        StorageReference storageRef = mStorage.getReference();
        StorageReference fileRef = storageRef.child("uploads")
                .child(imageUri.getLastPathSegment());

        // Crea una tasca de pujada de fitxer a FileStorage
        UploadTask uploadTask = fileRef.putFile(imageUri);

        // Listener per la pujada
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.d(TAG, "Upload is " + progress + "% done");
            }
        });

        // La tasca en si: ves fent-la (pujant) i fins que s'hagi completat (onCompleteListener).
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (task.isSuccessful()) {
                    // Continue with the task to get the download URL
                    return fileRef.getDownloadUrl();
                } else {
                    throw task.getException();
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete (@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri uploadUrl = task.getResult();
                    // un cop pujat, passa-li la URL de la imatge a l'adapter de
                    // la Base de Dades per a que l'associï a l'usuari
                    Log.d(TAG, "DownloadTask: " + uploadUrl.toString());
                    mUserRepository.setPictureUrlOfUser(userId, uploadUrl.toString());
                    mPictureUrl.setValue(uploadUrl.toString());
                }
            }
        });

    }
    public void setProgressBar(ProgressBar p){
        progressBar =p;
    }

    public void initProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public void endProgressBar(){
        progressBar.setVisibility(View.GONE);
    }
}
