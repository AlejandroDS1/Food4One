package Food4One.app.Model.User;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/** Classe que fa d'adaptador entre la base de dades (Cloud Firestore) i les classes del model
 * Segueix el patró de disseny Singleton.
 */
public class UserRepository {
    private static final String TAG = "Repository";

    /** Autoinstància, pel patró singleton */
    private static final UserRepository mInstance = new UserRepository();

    /** Referència a la Base de Dades */
    private FirebaseFirestore mDb;

    /** Definició de listener (interficie),
     *  per escoltar quan s'hagin acabat de llegir els usuaris de la BBDD */
    public interface OnLoadUsersListener {
        void onLoadUsers(ArrayList<User> users);
    }

    public ArrayList<OnLoadUsersListener> mOnLoadUsersListeners = new ArrayList<>();

    /** Definició de listener (interficie)
     * per poder escoltar quan s'hagi acabat de llegir la Url de la foto de perfil
     * d'un usuari concret */
    public interface OnLoadUserPictureUrlListener {
        void OnLoadUserPictureUrl(String pictureUrl);
    }

    public OnLoadUserPictureUrlListener mOnLoadUserPictureUrlListener;

    /**
     * Constructor privat per a forçar la instanciació amb getInstance(),
     * com marca el patró de disseny Singleton class
     */
    private UserRepository() {
        mDb = FirebaseFirestore.getInstance();
    }

    /**
     * Retorna aqusta instancia singleton
     * @return
     */
    public static UserRepository getInstance() {
        return mInstance;
    }

    /**
     * Afegir un listener de la operació OnLoadUsersListener.
     * Pot haver-n'hi només un. Fem llista, com a exemple, per demostrar la flexibilitat
     * d'aquest disseny.
     * @param listener
     */
    public void addOnLoadUsersListener(OnLoadUsersListener listener) {
        mOnLoadUsersListeners.add(listener);
    }

    /**
     * Setejem un listener de la operació OnLoadUserPictureUrlListener.
     * En aquest cas, no és una llista de listeners. Només deixem haver-n'hi un,
     * també a tall d'exemple.
     * @param listener
     */
    public void setOnLoadUserPictureListener(OnLoadUserPictureUrlListener listener) {
        mOnLoadUserPictureUrlListener = listener;
    }

    /**
     * Mètode que llegeix els usuaris. Vindrà cridat des de fora i quan acabi,
     * avisarà sempre als listeners, invocant el seu OnLoadUsers.
     */
    public void loadUsers(ArrayList<User> users){
        users.clear();
        mDb.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                User user = new User(
                                        document.getString("Name"),
                                        document.getString("Email"),
                                        (ArrayList<String>) document.get("idRecetasUser")
                                );
                                users.add(user);
                            }
                            /* Callback listeners */
                            for (OnLoadUsersListener l: mOnLoadUsersListeners) {
                                l.onLoadUsers(users);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Mètode que llegeix la Url d'una foto de perfil d'un usuari indicat pel seu
     * email. Vindrà cridat des de fora i quan acabi, avisarà sempre al listener,
     * invocant el seu OnLoadUserPictureUrl.
     */
    public void loadPictureOfUser(String email) {
        mDb.collection("Users")
                .document(email)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                mOnLoadUserPictureUrlListener.OnLoadUserPictureUrl(document.getString("PictureUser"));
                            } else {
                                Log.d("LOGGER", "No such document");
                            }
                        } else {
                            Log.d("LOGGER", "get failed with ", task.getException());
                        }
                    }
                });
    }

    /**
     * Mètode que afegeix un nou usuari a la base de dades. Utilitzat per la funció
     * de Sign-Up (registre) de la SignUpActivity.
     * @param email
     * @param firstName
     */
    public void addUser(
            String firstName,
            String email
    ) {
        // Obtenir informació personal de l'usuari
        Map<String, Object> signedUpUser = new HashMap<>();
        signedUpUser.put("Alergias", new ArrayList<String>());
        signedUpUser.put("Name", firstName);
        signedUpUser.put("Email", email);
        signedUpUser.put("Description", " ");
        signedUpUser.put("idRecetas", new ArrayList<String>());
        signedUpUser.put("idCollections", new ArrayList<String>());
        signedUpUser.put("PictureUser", null);

        // Afegir-la a la base de dades
        mDb.collection("Users").document(email).set(signedUpUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Sign up completion succeeded");
                        } else {
                            Log.d(TAG, "Sign up completion failed");
                        }
                    }
                });
    }

    /**
     * Mètode que guarda la Url d'una foto de perfil que un usuari hagi pujat
     * des del PerfilFragment a la BBDD. Concretament, es cridat pel PerfilViewModel.
     * @param userId
     * @param pictureUrl
     */
    public void setPictureUrlOfUser(String userId, String pictureUrl) {
        Map<String, Object> userEntry = new HashMap<>();
        userEntry.put("PictureUser", pictureUrl);

        mDb.collection("Users")
                .document(userId)
                .set(userEntry, SetOptions.merge())
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Photo upload succeeded: " + pictureUrl);
                })
                .addOnFailureListener(exception -> {
                    Log.d(TAG, "Photo upload failed: " + pictureUrl);
                });
    }

    public void setUserNameDDB(String email, String userName){

        HashMap<String, String> store = new HashMap<>();
        store.put("Name", userName);

        mDb.collection("Users").document(email).set(store, SetOptions.merge()).addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "User Name updated to " + userName);
                })
                .addOnFailureListener(exception -> {
                    Log.d(TAG, "User Name update failed: " + userName);
                });
    }

    public void loadUserFromDDB(String email){
        mDb.collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    User user = User.getInstance(document.getString(User.NAME_TAG), email);

                    user.setAlergias((ArrayList<String>) document.get(User.ALERGIAS_TAG));

                    user.setDescripcion(document.getString(User.DESCRIPCION_TAG));

                    user.setProfilePictureURL(document.getString(User.PICTUREURL_TAG));

                    user.setIdCollections((ArrayList<String>) document.get(User.IDCOLLECTIONS_TAG));

                    user.setIdRecetas((ArrayList<String>) document.get(User.IDRECETAS_TAG));
                }
            }
        });
    }
}
