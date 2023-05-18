package Food4One.app.Model.Recipe.Recipe;


import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import Food4One.app.Model.Recipe.Ingredients.Ingrediente;
import Food4One.app.Model.Recipe.Ingredients.IngredientesList;
import Food4One.app.Model.User.User;
import Food4One.app.Model.User.UserRepository;

/**
 * Classe que fa d'adaptador entre la base de dades (Cloud Firestore) i les classes del model
 * Segueix el patró de disseny Singleton.
 */
public class RecipeRepository {

    private static final String TAG = "Repository";

    /**
     * Autoinstància, pel patró singleton
     */
    private static final RecipeRepository mInstance = new RecipeRepository();

    /**
     * Referència a la Base de Dades
     */
    private FirebaseFirestore mDb;

    public void setURLUserToRecipes(ArrayList<String> recetasIdUser, String pictureUrl) {
        HashMap<String, String> store = new HashMap<>();
        for (String idReceta : recetasIdUser) {
            store.put(User.PICTUREURL_TAG + "user", pictureUrl);
            mDb.collection(Recipe.TAG).document(idReceta)
                    .set(store, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Photo upload to Recipes succeeded: " + pictureUrl);
                        }
                    });
        }
    }


//----------------------------------------------------------------------------------------------

    /**
     * Definició de listener (interficie),
     * per escoltar quan s'hagin acabat de llegir les receptes de la BBDD
     */
    public interface OnLoadRecetaListener {
        void onLoadRecetas(ArrayList<Recipe> recetas);
    }

    public interface OnLoadRecipeExplorer {
        void onLoadRecipeExplorer(ArrayList<Recipe> recetas);
    }
    public ArrayList<OnLoadRecetaListener> mOnloadRecetaListeners = new ArrayList<>();


    public OnLoadRecipeExplorer mOnLoadRecetasExplorer;

    public interface OnLoadRecetaApp{
        void onLoadRecipeApp(ArrayList<Recipe> recetas);
    }

//-----------------------------------------------------------------------------------------------

    /**
     * Definició de listener (interficie)
     * per poder escoltar quan s'hagi acabat de llegir la Url de la foto de perfil
     * d'un usuari concret
     */
    public interface OnLoadRecetaPictureUrlListener {
        void OnLoadRecetaPictureUrl(String pictureUrl);
    }

    public interface OnLoadRecetaAppListener {
        void OnLoadRecetaApp(ArrayList<Recipe> recetas);
    }

    public interface OnLoadRecipeToMake {
        void OnLoadRecipe(Recipe recipeToDo);
    }

    public interface OnLoadURLUserFromRecipe {
        void OnLoadURLUserRecipe(String URL);
    }

    public OnLoadURLUserFromRecipe mOnLoadURLfromRecipe;
    public ArrayList<OnLoadRecipeToMake> mOnLoadRecipeToMake = new ArrayList<>();
    public ArrayList<OnLoadRecetaAppListener> monLoadRecetaAppListener = new ArrayList<>();

    public OnLoadRecetaPictureUrlListener mOnLoadRecetaPictureUrlListener;
//-------------------------------------------------------------------------------------------------

    /**
     * Constructor privat per a forçar la instanciació amb getInstance(),
     * com marca el patró de disseny Singleton class
     */

    private RecipeRepository() {
        mDb = FirebaseFirestore.getInstance();
    }

    /**
     * Retorna aqusta instancia singleton
     *
     * @return
     */
    public static RecipeRepository getInstance() {
        return mInstance;
    }

//-------------------------------------------------------------------------------------------------

    /**
     * Afegir un listener de la operació OnLoadRecetaListener.
     * Pot haver-n'hi només un. Fem llista, com a exemple, per demostrar la flexibilitat
     * d'aquest disseny.
     *
     * @param listener
     */
    public void addOnLoadRecetaListener(OnLoadRecetaListener listener) {
        mOnloadRecetaListeners.add(listener);
    }

    public void addOnLoadRecetaAppListener(OnLoadRecetaAppListener listenr) {
        this.monLoadRecetaAppListener.add(listenr);
    }

    public void addOnLoadRecipeToMake(OnLoadRecipeToMake listener) {
        this.mOnLoadRecipeToMake.add(listener);
    }

    /**
     * Setejem un listener de la operació OnLoadUserPictureUrlListener.
     * En aquest cas, no és una llista de listeners. Només deixem haver-n'hi un,
     * també a tall d'exemple.
     *
     * @param listener
     */
    public void setOnLoadUserPictureListener(OnLoadRecetaPictureUrlListener listener) {
        mOnLoadRecetaPictureUrlListener = listener;
    }

    public void setOnLoadRecetasExplorer(OnLoadRecipeExplorer listener){
        this.mOnLoadRecetasExplorer = listener;
    }
    public void setmOnLoadURLfromRecipe(OnLoadURLUserFromRecipe listener){
        mOnLoadURLfromRecipe = listener;
    }

//-------------------------------------------------------------------------------------------------


    public void loadRecipeToMake(Recipe recipe) {
        //Avisamos a los listeners que se ha cambiado la receta a hacer...
        for (OnLoadRecipeToMake listener : mOnLoadRecipeToMake)
            listener.OnLoadRecipe(recipe);
    }

    /**
     * Mètode que llegeix les recetes. Vindrà cridat des de fora i quan acabi,
     * avisarà sempre als listeners, invocant el seu OnLoadReceta.
     */
    public void loadRecetasUser(ArrayList<Recipe> recetaUsers, ArrayList<String> idRecetasUser) {
        recetaUsers.clear();
        //Se cargan todas las recetas de la base de datos...

        Iterator iterator = idRecetasUser.iterator();
        String userID = UserRepository.getUser().getEmail();
        while (iterator.hasNext()) {

            String idReceta = (String) iterator.next();
            mDb.collection(Recipe.TAG).document(idReceta).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    //Cargamos la receta que tiene el Mismo ID---------------------------------
                    Recipe recetaUser = documentSnapshot.toObject(Recipe.class);
                    recetaUser.setIngredientes(cargarIngredientes((ArrayList<String>) documentSnapshot.get(Recipe.INGREDIENTES_APP_TAG)));
                    recetaUser.setNombre(documentSnapshot.getId());
                    recetaUser.setPhotoUser(documentSnapshot.getString(User.PICTUREURL_TAG + "user"));
                    recetaUser.setIdUser(userID);

                    //---------------------------------------------------------------------------
                    //Lo añadimos a la lista de recetas que se mostrarán...
                    recetaUsers.add(recetaUser);

                    //Cuando se consigan todas las recetas del usuario, se llaman a los listeners
                    //para que puedan cargar las recetas al RecycleView
                    if (recetaUsers.size() == idRecetasUser.size())
                        onLoadRecetasListenerMethod();
                }

                private void onLoadRecetasListenerMethod() {
                    /*Llamamos a sus listeners*/
                    for (OnLoadRecetaListener l : mOnloadRecetaListeners)
                        l.onLoadRecetas(recetaUsers);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.toString());
                }
            });
        }
    }


    public void loadRecetas(ArrayList<Recipe> recetaUsers, String fragment) {
        recetaUsers.clear();

            //Se cargan todas las recetas de la base de datos...
            mDb.collection(Recipe.TAG).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        //Se tiene que cargar el String ID de los ingredientes...
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Recipe receta = document.toObject(Recipe.class);
                        receta.setIngredientes(cargarIngredientes((ArrayList<String>) document.get(Recipe.INGREDIENTES_APP_TAG)));
                        receta.setPasos((ArrayList<String>) document.get(Recipe.PASOS_APP_TAG));
                        receta.setNombre(document.getId());
                        receta.setPhotoUser(document.getString(User.PICTUREURL_TAG + "user"));
                        //Le damos el like a la receta si el usuario logado ya lo ha hecho
                        if (UserRepository.getUser().getLikesRecipes().get(receta.getNombre()) != null) {
                            receta.setLikeFromUser(true);
                        } else
                            receta.setLikeFromUser(false);

                        recetaUsers.add(receta);
                    }
                    if(fragment.equals("EXPLORER"))
                        mOnLoadRecetasExplorer.onLoadRecipeExplorer(recetaUsers);
                    else
                        /*Luego llamamos a sus listeners*/
                        for (OnLoadRecetaListener l : mOnloadRecetaListeners) {
                            l.onLoadRecetas(recetaUsers);
                        }

                }

            });


    }

    /*Se supone que el IdIngredientes jamás será nulo, porque siempre habrá como mínimo un ingrediente
     * Si aparece un error aquí, es porque no han colocado ningún ingrediente en la base de datos...*/
    private IngredientesList cargarIngredientes(ArrayList<String> IdIngredientes) {

        ArrayList<Ingrediente> ingredientesList = new ArrayList<>();

        for (String ingredienteId : IdIngredientes) {
            Ingrediente ingrediente = new Ingrediente(ingredienteId);
            ingredientesList.add(ingrediente);
        }

        return new IngredientesList(ingredientesList);

    }

    public void loadRecipesApp(ArrayList<Recipe> recetaUsers, String selection) {
        recetaUsers.clear();

        String path = "RecetasApp/" + selection + "/" + selection + "Types/";

        mDb.collection(path).get().
                addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Recipe recetaUser = new Recipe(document.getId(),
                                    document.getString(Recipe.DESCRIPTION_APP_TAG),
                                    cargarIngredientes((ArrayList<String>) document.get(Recipe.INGREDIENTES_APP_TAG)),
                                    document.getString(Recipe.PICTURE_APP_TAG),
                                    0,
                                    (ArrayList<String>) document.get(Recipe.PASOS_APP_TAG),
                                    (ArrayList<String>) document.get(Recipe.ALERGIAS_APP_TAG)

                            );
                            recetaUser.setDescription(document.getString(Recipe.DESCRIPTION_APP_TAG));
                            recetaUsers.add(recetaUser);
                        }
                        /*Luego llamamos a sus listeners*/
                        for (OnLoadRecetaAppListener l : monLoadRecetaAppListener) {
                            l.OnLoadRecetaApp(recetaUsers);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println();
                    }
                });
    }

    public final void uploadRecipe(Recipe newRecipe, MutableLiveData<Byte> completed) {

        final String idReceta = newRecipe.getNombre() + "@" + UserRepository.getUser().getUserName(); // Este sera el id de la receta

        final HashMap<String, Object> recipe = new HashMap<>(); // Creamos el HashMap que insertaremos en el documento

        // Llenamos el HashMap con cada campo que tenemos disponible
        recipe.put(Recipe.USER_ID_TAG, UserRepository.getUser().getEmail());
        recipe.put(Recipe.PICTURE_URL_USER_TAG, UserRepository.getUser().getProfilePictureURL());
        recipe.put(Recipe.DESCRIPTION_APP_TAG, newRecipe.getDescription());
        recipe.put(Recipe.INGREDIENTES_APP_TAG, newRecipe.getIngredientes().toArrayStringId());
        recipe.put(Recipe.ALERGIAS_APP_TAG, newRecipe.getAlergias());
        recipe.put(Recipe.PASOS_APP_TAG, newRecipe.getPasos());
        recipe.put(Recipe.LIKES_TAG, newRecipe.getLikes());

        // Creamos la URI para subir al FireBase Storage
        final Uri imageURL = Uri.parse(newRecipe.getPictureURL());

        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads")
                .child(imageURL.getLastPathSegment());


        // Crea una tasca de pujada de fitxer a FileStorage
        UploadTask uploadTask = fileRef.putFile(imageURL);

        // Añadimos un on complete Listener,

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
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        recipe.put(Recipe.PICTURE_APP_TAG, task.getResult().toString()); // Ponemos al HashMap la URL que nos falta.

                        // Subimos el documento con la receta.
                        mDb.collection(Recipe.TAG).document(idReceta).set(recipe)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        completed.setValue((byte) 1);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        completed.setValue((byte) 2);
                                    }
                                });
                    } else {
                        completed.setValue((byte) 2);
                    }
                }
        });
    }

    public void setLikesRecipeDDB(String idRecipe, int like){

        HashMap<String, Integer> store = new HashMap<>();
        store.put(Recipe.LIKES_TAG, like);
        mDb.collection(Recipe.TAG).document(idRecipe).set(store, SetOptions.merge()).addOnSuccessListener(documentReference -> {
            Log.d(TAG, "Like guardado en la receta");
        });
    }

}
