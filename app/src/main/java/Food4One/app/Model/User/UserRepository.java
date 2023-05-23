package Food4One.app.Model.User;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;
import Food4One.app.View.Authentification.AccesActivityViewModel;
import Food4One.app.View.MainScreen.MainScreenFragments.Coleccion.ShoppingListViewModel;
import Food4One.app.View.MainScreen.MainScreenFragments.Explore.ExploreViewModel;
import Food4One.app.View.MainScreen.MainScreenFragments.Perfil.PerfilViewModel;


/** Classe que fa d'adaptador entre la base de dades (Cloud Firestore) i les classes del model
 * Segueix el patró de disseny Singleton.
 */
public class UserRepository {
    private static final String TAG = "Repository";
    public static String CHARGE = "NotCharge";

    /** Autoinstància, pel patró singleton */
    private static final UserRepository mInstance = new UserRepository();
    /** Referència a la Base de Dades */
    private FirebaseFirestore mDb;
    private static User user;


    /** Definició de listener (interficie),
     *  per escoltar quan s'hagin acabat de llegir els usuaris de la BBDD */
    public interface OnLoadUsersListener {
        void onLoadUsers(ArrayList<User> users);
    }

    public ArrayList<OnLoadUsersListener> mOnLoadUsersListeners = new ArrayList<>();

    // DEFINICIONES DE LISTENERS PARA EL CARGADO DE BASE DE DATOS.

    /** Definició de listener (interficie)
     * per poder escoltar quan s'hagi acabat de llegir la Url de la foto de perfil
     * d'un usuari concret */
    public interface OnLoadUserPictureUrlListener {
        void OnLoadUserPictureUrl(String pictureUrl);
    }

    public interface OnLoadUserNameListener{
        void OnLoadUserName(String name);
    }
    public interface OnLoadUserDescriptionListener{
        void OnLoadUserDescription(String description);
    }


    public OnLoadListIngredientesListener onLoadListIngredientesListener;
    public interface OnLoadListIngredientesListener {
        void onLoadListIngredientes();
    }

    public interface OnSetListIngredientesListener {
        void onSetListIngredientes(final boolean state);
    }

    // Listener atributes
    public OnSetListIngredientesListener onSetListIngredientesListener;
    public OnLoadUserNameListener mOnLoadUserNameListener;

    public OnLoadUserPictureUrlListener mOnLoadUserPictureUrlListener;

    public OnLoadUserDescriptionListener mOnLoadUserDescritionListener;

    /**
     * Constructor privat per a forçar la instanciació amb getInstance(),
     * com marca el patró de disseny Singleton class
     */
    private UserRepository() {
        mDb = FirebaseFirestore.getInstance();
    }

    /**
     * Retorna aquesta instancia singleton
     * @return
     */
    public static UserRepository getInstance() {
        return mInstance;
    }


    // Getters para conseguir el usuario de la APP.
    public static User getUser(String userName, String email){
        if (user == null) user = new User(userName, email);
        return user;
    }
    public static User getUser(){
        return user;
    }

    // Para el logout tenemos que elimiar el usuario
    public static void logOutUser(){
        user = null;
    }

    // METODOS SETTER PARA LOS LISTENERS
    public void setOnSetListIngredientesListener(@NonNull final OnSetListIngredientesListener listener) {
        this.onSetListIngredientesListener = listener;
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
     * Añadir listener para el cargado de datos
     * @param listener
     */
    public void setOnLoadListIngredientesListener(OnLoadListIngredientesListener listener){
        this.onLoadListIngredientesListener = listener;
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
    public void setOnLoadUserNameListener( OnLoadUserNameListener listener){
        this.mOnLoadUserNameListener = listener;
    }
    public void setOnLoadUserDescription(OnLoadUserDescriptionListener listener){
        this.mOnLoadUserDescritionListener = listener;
    }

    /**
     * Mètode que llegeix els usuaris. Vindrà cridat des de fora i quan acabi,
     * avisarà sempre als listeners, invocant el seu OnLoadUsers.
     */
    public void loadUsers(ArrayList<User> users){
        users.clear();
        mDb.collection(User.TAG)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                User user = new User(
                                        document.getString(User.NAME_TAG),
                                        document.getString("Email"),
                                        (ArrayList<String>) document.get(User.ALERGIAS_TAG)
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
        mDb.collection(User.TAG)
                .document(email)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                mOnLoadUserPictureUrlListener.OnLoadUserPictureUrl(document.getString(User.PICTUREURL_TAG));
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
        signedUpUser.put(User.ALERGIAS_TAG, new ArrayList<String>());
        signedUpUser.put(User.NAME_TAG, firstName);
        signedUpUser.put("Email", email);
        signedUpUser.put(User.DESCRIPCION_TAG, " ");
        signedUpUser.put(User.IDRECETAS_TAG, new ArrayList<String>());
        signedUpUser.put(User.IDCOLLECTIONS_TAG, new ArrayList<String>());
        signedUpUser.put(User.LIKESRECIPES_TAG, new ArrayList<String>());
        signedUpUser.put(User.PICTUREURL_TAG, null);
        signedUpUser.put(User.IDINGREDIENTES_LIST_TAG, new ArrayList<String>());

        // Afegir-la a la base de dades
        mDb.collection(User.TAG).document(email).set(signedUpUser)
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
     * Este metodo devuelve un objeto IngredientesList extraido de los ingredientes guardados en la base de datos.
     * @return IngredientesList objeto que contiene los ingredientes guardados.
     */
    public void loadUserIngredientesList(@NonNull final MutableLiveData<Map<String, Map<String, Boolean>>> _listaIngredientes){

        mDb.collection(User.TAG).document(UserRepository.getUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                if (task.isSuccessful())
                    // Conseguimos el atributo que queremos
                    _listaIngredientes.setValue((Map<String, Map<String, Boolean>>) task.getResult().get(User.IDINGREDIENTES_LIST_TAG));

                onLoadListIngredientesListener.onLoadListIngredientes();
            }
        });
    }

    /**
     * Este metodo actualiza la base de datso de Firebase con las actualizaciones que puedan haber habido en la lista de ingredientes
     * @param viewModel viewMOdel que contiene el objeto a añadir a base de datos
     */
    public void setUserIngredientesListDDBB(@NonNull final ShoppingListViewModel viewModel){

        Map<String, Object> ingredientesList = new HashMap<>();

        ingredientesList.put(User.IDINGREDIENTES_LIST_TAG, viewModel.getMapAllLists_toDDBB());

        // TODO Todavia no se puede eliminar bien, falta implementacion
        mDb.collection(User.TAG)
                .document(UserRepository.getUser().getEmail())
                .set(ingredientesList, SetOptions.merge())
                .addOnSuccessListener(succesListener -> {
                    if (this.onSetListIngredientesListener != null)
                        onSetListIngredientesListener.onSetListIngredientes(true);

                }).addOnFailureListener(onFailure -> {
                    if (this.onSetListIngredientesListener != null)
                        onSetListIngredientesListener.onSetListIngredientes(false);
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
        userEntry.put(User.PICTUREURL_TAG, pictureUrl);

        mDb.collection(User.TAG)
                .document(userId)
                .set(userEntry, SetOptions.merge())
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Photo upload succeeded: " + pictureUrl);
                    RecipeRepository.getInstance().setURLUserToRecipes(UserRepository.getUser().getIdRecetas(), pictureUrl);

                })
                .addOnFailureListener(exception -> {
                    Log.d(TAG, "Photo upload failed: " + pictureUrl);
                });

    }

    public boolean setUserNameDDB(String email, String userName){

        HashMap<String, String> store = new HashMap<>();
        store.put(User.NAME_TAG, userName);

        mDb.collection(User.TAG).document(email).set(store, SetOptions.merge()).addOnSuccessListener(documentReference -> {
                    UserRepository.getUser().setUserName(userName); //Lo cambiamos en el User Global
                    PerfilViewModel.getInstance().getOnLoadUserListener().OnLoadUserName(userName); //Y en la ventana Perfil
        });

        // Comprovamos si ha entrado en el OnSucces, si retorna falso es porque no se ha subido a base de datos.
        return !UserRepository.getUser().userName.equals(userName);
    }



    // TODO: Si podemos mejorar el paso por parametro de un MutableLiveData mejor
    // Se puede utilizar pasandole un null sino se utiliza para actualizar el texto de UserSettings
    public void setUserDescriptionDDB(String email, String description, @Nullable MutableLiveData<String> mdescription){

        HashMap<String, String> store = new HashMap<>();
        store.put(User.DESCRIPCION_TAG, description);

        mDb.collection(User.TAG).document(email)
                .set(store, SetOptions.merge())
                .addOnSuccessListener(succesListener -> {
                    if (mdescription != null)
                        mdescription.setValue(description);

                    UserRepository.getUser().setDescripcion(description);
                    PerfilViewModel.getInstance().getOnLoadUserDescrptionListener().OnLoadUserDescription(description);
                });
    }

    public void setUserAlergiasDDB(String email, ArrayList<String> alergias, @Nullable MutableLiveData<String> alergiasTxt) {

        HashMap<String, ArrayList<String>> store = new HashMap<>();
        store.put(User.ALERGIAS_TAG, alergias);

        mDb.collection(User.TAG).document(email)
                .set(store, SetOptions.merge())
                .addOnSuccessListener(succesListener -> {
                    if (alergiasTxt != null){
                        // Creamos el texto que tenemos que poner en alergiasTxt y lo cambiamos.
                        // Por defecto el ArrayListObject.toString() da como resultado [ Item1, Item2...] Aqui quitamos los "[]"
                        String _alergias = alergias.toString().substring(1, alergias.toString().length()-1);
                        alergiasTxt.setValue(_alergias);
                    }
                    UserRepository.getUser().setAlergias(alergias);
                });
    }

    public void setUserIdRecipe(){

        final User user = UserRepository.getUser();

        final Map<String, ArrayList<String>> toStore = new HashMap<>();
        toStore.put(User.IDRECETAS_TAG, user.getIdRecetas());
        // TODO: Alomejor retocar este metodo.
        mDb.collection(User.TAG).document(user.getEmail())
                .set(toStore, SetOptions.merge());

    }

    public void loadUserFromDDB(String email, Activity accessActivity, ViewModel viewModel){

        mDb.collection(User.TAG).document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    User user = UserRepository.getUser(document.getString(User.NAME_TAG), email);

                    user.setAlergias((ArrayList<String>) document.get(User.ALERGIAS_TAG));

                    user.setDescripcion(document.getString(User.DESCRIPCION_TAG));

                    user.setProfilePictureURL(document.getString(User.PICTUREURL_TAG));

                    user.setIdCollections(createHashMap((ArrayList<String>) document.get(User.IDCOLLECTIONS_TAG)));

                    user.setIdRecetas((ArrayList<String>) document.get(User.IDRECETAS_TAG));

                    user.setLikesRecipes( createHashMap((ArrayList<String>) document.get(User.LIKESRECIPES_TAG)));

                    ExploreViewModel.getInstance();

                    ((AccesActivityViewModel) viewModel).setCompleted(true);

                    //accessActivity.startActivity(new Intent(accessActivity.getApplicationContext(), MainScreen.class));
                }
            }

            private HashMap<String, Boolean> createHashMap(ArrayList<String> strings) {

                HashMap<String, Boolean> mapForUser = new HashMap<>();
                for(String data: strings)
                    mapForUser.put(data, true);

                return mapForUser;
            }
        });
    }

    public void setUserLikeDDB(Recipe recipe, boolean like) {

        User user =  UserRepository.getUser();
        HashMap<String, Boolean> idLikesUser = user.getLikesRecipes();

        ArrayList<String> actualLikes=  new ArrayList<>(idLikesUser.keySet());
        String recipeName = recipe.getNombre();

        int likeDonat = 0;

        if (like){
            actualLikes.add(recipeName);
            idLikesUser.put(recipeName, true);
            recipe.setLikeFromUser(true);
            likeDonat = 1;
        } else {
            likeDonat = -1;
            actualLikes.remove(recipeName);
            idLikesUser.remove(recipeName);
            recipe.setLikeFromUser(false);
        }

        HashMap<String, ArrayList<String> >  store = new HashMap<>();
        store.put(User.LIKESRECIPES_TAG, actualLikes);

        mDb.collection(User.TAG).document(user.getEmail()).set(store, SetOptions.merge())
                .addOnSuccessListener(succesListener -> {
                    Log.d(TAG, "Like Receta: " + recipeName + " Añadida");
        });

        //Després cal canviar els likes que tenim a la base de dades de les recetes.
        recipe.setLikes(likeDonat);
        RecipeRepository.getInstance().setLikesRecipeDDB(recipeName, recipe.getLikes());
    }

    public void setUserRecetaCollectionDDB(Recipe recipe, boolean saved) {
        User user =  UserRepository.getUser();
        HashMap<String, Boolean> idCollectionUser = user.getIdCollections();

        ArrayList<String> actualCollection =  new ArrayList<>(idCollectionUser.keySet());
        String recipeName = recipe.getNombre();
        if(saved) {
            actualCollection.add(recipeName);
            idCollectionUser.put(recipeName, true);
        }else {
            actualCollection.remove(recipeName);
            idCollectionUser.remove(recipeName);
        }

        HashMap<String, ArrayList<String> > store = new HashMap<>();
        store.put(User.IDCOLLECTIONS_TAG, actualCollection);

        mDb.collection(User.TAG).document(user.getEmail())
                .set(store, SetOptions.merge())
                .addOnSuccessListener(succesListener -> {
                    Log.d(TAG, "User's Collection is update");
                }).addOnFailureListener(failurelistener-> {
                        Log.d(TAG, "User's Collection is not working");
                    });
    }
}
