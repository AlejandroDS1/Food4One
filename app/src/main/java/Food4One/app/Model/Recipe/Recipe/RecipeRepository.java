package Food4One.app.Model.Recipe.Recipe;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

import Food4One.app.Model.Recipe.Ingredients.Ingrediente;
import Food4One.app.Model.Recipe.Ingredients.IngredientesList;
import Food4One.app.Model.User.User;

/** Classe que fa d'adaptador entre la base de dades (Cloud Firestore) i les classes del model
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

//----------------------------------------------------------------------------------------------

    /**
     * Definició de listener (interficie),
     * per escoltar quan s'hagin acabat de llegir les receptes de la BBDD
     */
    public interface OnLoadRecetaListener {
        void onLoadRecetas(ArrayList<Recipe> recetas);
    }

    public ArrayList<OnLoadRecetaListener> mOnloadRecetaListeners = new ArrayList<>();

//-----------------------------------------------------------------------------------------------

    /**
     * Definició de listener (interficie)
     * per poder escoltar quan s'hagi acabat de llegir la Url de la foto de perfil
     * d'un usuari concret
     */
    public interface OnLoadRecetaPictureUrlListener {
        void OnLoadRecetaPictureUrl(String pictureUrl);
    }

    public interface OnLoadRecetaAppListener{
        void OnLoadRecetaApp(ArrayList<Recipe> recetas);
    }
    public interface  OnLoadRecipeToMake{
        void OnLoadRecipe(Recipe recipeToDo);
    }
    public ArrayList<OnLoadRecipeToMake> mOnLoadRecipeToMake = new ArrayList<>();
    public ArrayList<OnLoadRecetaAppListener> monLoadRecetaAppListener = new ArrayList<>();

    public OnLoadRecetaPictureUrlListener mOnLoadRecetaPictureUrlListener ;
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

    public void addOnLoadRecetaAppListener(OnLoadRecetaAppListener listenr){
        this.monLoadRecetaAppListener.add(listenr);
    }
    public void addOnLoadRecipeToMake(OnLoadRecipeToMake listener){
        this.mOnLoadRecipeToMake.add(listener);
    }    /**
     * Setejem un listener de la operació OnLoadUserPictureUrlListener.
     * En aquest cas, no és una llista de listeners. Només deixem haver-n'hi un,
     * també a tall d'exemple.
     *
     * @param listener
     */
    public void setOnLoadUserPictureListener(OnLoadRecetaPictureUrlListener listener) {
        mOnLoadRecetaPictureUrlListener = listener;
    }

//-------------------------------------------------------------------------------------------------


    public void loadRecipeToMake(Recipe recipe){
    //Avisamos a los listeners que se ha cambiado la receta a hacer...
    for(OnLoadRecipeToMake listener: mOnLoadRecipeToMake)
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
        String userID = User.getInstance().getEmail();
        while (iterator.hasNext()) {

            String idReceta = (String) iterator.next();
            mDb.collection(Recipe.TAG).document(idReceta).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    //Cargamos la receta que tiene el Mismo ID---------------------------------
                    Recipe recetaUser = documentSnapshot.toObject(Recipe.class);

                    cargarIngredientes((ArrayList<String>) documentSnapshot.get(Recipe.INGREDIENTES_APP_TAG));
                    recetaUser.setNombre(documentSnapshot.getId());
                    cargarIngredientes((ArrayList<String>) documentSnapshot.get("Ingredientes"));
                    recetaUser.setNombre(documentSnapshot.getId().split("@")[0]);
                    recetaUser.setIdUser(userID);
                    //---------------------------------------------------------------------------

                    //Lo añadimos a la lista de recetas que se mostrarán...
                    recetaUsers.add(recetaUser);

                    //Cuando se consigan todas las recetas del usuario, se llaman a los listeners
                    //para que puedan cargar las recetas al RecycleView
                    if (recetaUsers.size() == idRecetasUser.size())
                        onLoadRecetasListenerMethod();
                }

                private void  onLoadRecetasListenerMethod() {
                    /*Llamamos a sus listeners*/
                    for (OnLoadRecetaAppListener l : monLoadRecetaAppListener)
                        l.OnLoadRecetaApp(recetaUsers);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.toString());
                }
            });
        }

    }


    public void loadRecetas(ArrayList<Recipe> recetaUsers) {
        recetaUsers.clear();
        //Se cargan todas las recetas de la base de datos...
        mDb.collection(Recipe.TAG).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                    //Se tiene que cargar el String ID de los ingredientes...


                    Log.d(TAG, document.getId() + " => " + document.getData());
                    Recipe recetaUser = new Recipe(document.getId(),
                            document.getString("pictureURL"),
                            Integer.parseInt(document.getString("likes")),
                            cargarIngredientes((ArrayList<String>) document.get(Recipe.INGREDIENTES_APP_TAG)),
                            (ArrayList<String>) document.get("pasos")
                    );
                    recetaUsers.add(recetaUser);
                }
                /*Luego llamamos a sus listeners*/
                for (OnLoadRecetaListener l: mOnloadRecetaListeners){
                    l.onLoadRecetas(recetaUsers);
                }
            }

        });
    }
    public void loadRecipesApp(ArrayList<Recipe> recetaUsers, String selection) {
        recetaUsers.clear();

        mDb.collection("RecetasApp/Barbarcoa/BarbacoaTypes/").get().
                addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Recipe recetaUser = new Recipe(document.getId(),
                                    document.getString(Recipe.PICTURE_APP_TAG),0,
                                    cargarIngredientes((ArrayList<String>) document.get(Recipe.INGREDIENTES_APP_TAG)),
                                    (ArrayList<String>) document.get(Recipe.PASOS_APP_TAG)
                            );
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

    /*Se supone que el IdIngredientes jamás será nulo, porque siempre habrá como mínimo un ingrediente
     * Si aparece un error aquí, es porque no han colocado ningún ingrediente en la base de datos...*/
    private IngredientesList cargarIngredientes(ArrayList<String> IdIngredientes ) {

        ArrayList<Ingrediente> ingredientesList = new ArrayList<>();
        for(String ingredienteId : IdIngredientes){
            Ingrediente ingrediente = new Ingrediente(ingredienteId);
            ingredientesList.add(ingrediente);
        }
        return new IngredientesList(ingredientesList);
    }


}
