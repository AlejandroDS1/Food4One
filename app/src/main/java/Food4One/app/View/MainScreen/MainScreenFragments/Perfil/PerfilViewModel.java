package Food4One.app.View.MainScreen.MainScreenFragments.Perfil;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;
import Food4One.app.Model.User.User;
import Food4One.app.Model.User.UserRepository;

public class PerfilViewModel extends ViewModel {
    private final String TAG = "FotosmeActivityViewModel";
    private FirebaseStorage mStorage;
    /* Elements observables del ViewModel */
    // Les receptes que la RecyclerView mostra al perfil
    private final MutableLiveData<ArrayList<Recipe>> mRecetas;
    private final MutableLiveData<ArrayList<User>> mUsers;
    private final MutableLiveData<String> mDescription;
    private final MutableLiveData<String> mPictureUrl; // URL de la foto de l'usuari logat
    private final MutableLiveData<String> mUserName;
    private static PerfilViewModel perfilViewModel;
    private final MutableLiveData<String> mText;

    /*Repositori (base de dades) de les recetes-Details*/
    private RecipeRepository mRecetaRepository;
    private UserRepository mUserRepository;

    public static PerfilViewModel getInstance(){
        if (perfilViewModel == null) perfilViewModel = new PerfilViewModel();
        return perfilViewModel;
    }
    public PerfilViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");

        mUsers = new MutableLiveData<>(new ArrayList<>());
        mStorage = FirebaseStorage.getInstance();
        mRecetaRepository = RecipeRepository.getInstance();
        mRecetas = new MutableLiveData<>(new ArrayList<>());
        mUserName = new MutableLiveData<>();
        mDescription = new MutableLiveData<>();
        mPictureUrl = new MutableLiveData<>();
        mUserRepository = UserRepository.getInstance();

        userPictureliteners();
        recetasPictureListeners();
        infoUserListener();
    }

    private void infoUserListener() {
        mUserRepository.setOnLoadUserNameListener(new UserRepository.OnLoadUserNameListener() {
            @Override
            public void OnLoadUserName(String name) {
                PerfilViewModel.this.setmUserName(name);
            }
        });

        mUserRepository.setOnLoadUserDescription(new UserRepository.OnLoadUserDescriptionListener() {
            @Override
            public void OnLoadUserDescription(String description) {
                setUserDescription(description);
            }
        });
    }

    public void setUserDescription(String description){ this.mDescription.setValue(description);}

    public void setmUserName(String name){
        this.mUserName.setValue(name);
    }

    private void recetasPictureListeners() {
        //Al cargar las recetas el observador será notificado y se acutalizará la lista
        mRecetaRepository.addOnLoadRecetaListener(new RecipeRepository.OnLoadRecetaListener() {
            @Override
            public void onLoadRecetas(ArrayList<Recipe> recetas) {
                PerfilViewModel.this.setRecetes(recetas);
            }
        });
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
                if(pictureUrl != null) mPictureUrl.setValue(pictureUrl);
            }
        });
    }

    /*
     * Retorna les recetes perquè PerfilFragment pugui subscriure-hi l'observable.
     */
    public LiveData<ArrayList<Recipe>> getRecetes() {
        return mRecetas;
    }

    public MutableLiveData<String> getmText() {
        return mText;
    }

    /*
     * Retorna el LiveData de la URL de la foto per a què PerfilFragment
     * pugui subscriure-hi l'observable.
     */
    public LiveData<String> getPictureProfileUrl() { return mPictureUrl;  }
    public MutableLiveData<String> getmUserName() { return mUserName;  }
    public MutableLiveData<String> getmDescription() { return mDescription;  }
    public void setRecetes(ArrayList<Recipe> recetas) {
        mRecetas.setValue(recetas);
    }

    public void setUsers(ArrayList<User> users){
        mUsers.setValue(users);
    }
    public void setmPictureUrl(String url){ mPictureUrl.setValue(url);}

    public void loadPictureOfUser(String email){ mUserRepository.loadPictureOfUser(email);}

    public void loadRecetasOfUserFromRepository(ArrayList<String> idRecetasUser){
         mRecetaRepository.loadRecetasUser(mRecetas.getValue(), idRecetasUser, "PERFIL");
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
                    String uploadUrl = task.getResult().toString();
                    // un cop pujat, passa-li la URL de la imatge a l'adapter de
                    // la Base de Dades per a que l'associï a l'usuari
                    Log.d(TAG, "DownloadTask: " + uploadUrl);
                    mUserRepository.setPictureUrlOfUser(userId, uploadUrl);
                    mPictureUrl.setValue(uploadUrl);
                }
            }
        });
    }
    public UserRepository.OnLoadUserNameListener getOnLoadUserListener(){
        return mUserRepository.mOnLoadUserNameListener;
    }
    public UserRepository.OnLoadUserDescriptionListener getOnLoadUserDescrptionListener(){
        return mUserRepository.mOnLoadUserDescritionListener;
    }

}
