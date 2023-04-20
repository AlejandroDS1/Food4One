package Food4One.app.Model.Recipie.Recipie;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Passar codi guardar receptes en RecipeList
 */
public class RecipeListAdapter /*extends ArrayAdapter<Recipie> */{

    private ArrayList<Recipe> recipies;
    private Context contexto;
    /** Referència a la Base de Dades */
    private FirebaseFirestore mDb;
    private static final String TAG = "Repository";

    /** Autoinstància, pel patró singleton */
    private static final RecipeListAdapter mInstance = new RecipeListAdapter();

    public interface OnLoadRecipesListener {
        void OnLoadRecipes(ArrayList<Recipe> recipes);
    }

    public ArrayList<OnLoadRecipesListener> mOnLoadRecipesListeners = new ArrayList<>();

/*
    public RecipieListAdapter(@NonNull Context context, int resource, @NonNull List<Recipie> objects) {
        super(context, resource, objects);
        recipies = (ArrayList<Recipie>) objects;
        contexto = context;
        mDb = FirebaseFirestore.getInstance();
    }*/

    /**
     * Constructor pel singleton pero ns si fa falta
     *
     */
    public RecipeListAdapter() {
        this.mDb = FirebaseFirestore.getInstance();
    }

    public static RecipeListAdapter getInstance() {
        return mInstance;
    }

    public void addOnLoadRecipesListener(RecipeListAdapter.OnLoadRecipesListener listener) {
        mOnLoadRecipesListeners.add(listener);
    }

    public void loadRecipes(ArrayList<Recipe> recipes){ //Canviar elements i tb del constructor de recepte
        recipes.clear();
        mDb.collection("Recetas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Recipe recipe = new Recipe(document.getId().split("@")[0]/*document.getString("Name")*/, document.getString("pictureURL")); //Agafar ID en comptes de name i fer .split("@")[0]
                        recipes.add(recipe);
                        System.out.println("OKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEYOKEY");
                    }

                    for (OnLoadRecipesListener l: mOnLoadRecipesListeners) {
                        l.OnLoadRecipes(recipes);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    //Malament I think
    public void addRecipe(String name){
        Map<String, Object> loadedRecipes = new HashMap<>();
        loadedRecipes.put("Name", name);
        loadedRecipes.put("pictureURL", null);

        mDb.collection("Recetas").document(name).set(loadedRecipes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Loading completed");
                } else {
                    Log.d(TAG, "Loading failed");
                }
            }
        });
    }
}
