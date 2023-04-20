package Food4One.app.Model.Recipie.Recipie;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Food4One.app.Model.Recipie.Ingredients.Ingrediente;
import Food4One.app.Model.Recipie.Ingredients.IngredientesList;

public class RecipeList implements Serializable {



    /** Referència a la Base de Dades */
    private FirebaseFirestore mDb;
    private static final String TAG = "Repository";

    /** Autoinstància, pel patró singleton */
    private static final RecipeList mInstance = new RecipeList();

    public interface OnLoadRecipesListener {
        void OnLoadRecipes(ArrayList<Recipe> recipes);
    }

    public ArrayList<RecipeList.OnLoadRecipesListener> mOnLoadRecipesListeners = new ArrayList<>();


    public RecipeList() { this.mDb = FirebaseFirestore.getInstance(); }

    public static RecipeList getInstance() {
        return mInstance;
    }

    public void addOnLoadRecipesListener(RecipeList.OnLoadRecipesListener listener) {
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
                        //Array on guardarem els objectes Ingredient
                        ArrayList<Ingrediente> ingredientes = new ArrayList<>();
                        //Array on guardem els elements de la DB
                        ArrayList<String> p = (ArrayList<String>) document.getData().get("Ingredientes");

                        Iterator itr = p.iterator();
                        while (itr.hasNext()){ //Per cada un dels elements del array creem un nom ingredient
                            String ing = (String) itr.next();
                            Ingrediente ingrediente = new Ingrediente(ing);
                            ingredientes.add(ingrediente);
                        }

                        // TODO: Potser s'ha d'afegir el paràmetre de les al·lèrgies, en tal cas tb canviar constructor de Recipe
                        Recipe recipe = new Recipe(document.getId().split("@")[0], //El nom de la recepte es troba en la primera part del ID
                                new IngredientesList(ingredientes), //Creem la llista d'ingredients
                                document.getString("pictureURL"),
                                document.getString("Description"),
                                ((Long) document.get("likes")).intValue(), //Agafem el valor number de la DB i la passem a Integer
                                (ArrayList<String>) document.getData().get("pasos")); //Agafem el array dels passos de la DB

                        recipes.add(recipe); //Afegim la recepte
                    }

                    for (RecipeList.OnLoadRecipesListener l: mOnLoadRecipesListeners) {
                        l.OnLoadRecipes(recipes);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    /*
    public void add(Recipe recipe){
        this.recipes.add(recipe);
    }

    public void remove(Recipe recipe){
        this.recipes.remove(recipe);
    }

    public void remove(int pos){
        this.recipes.remove(pos);
    }*/

}
