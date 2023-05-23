package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;

public class ColeccionViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Recipe>> mRecipes;
    private final MutableLiveData<String> recetasVacias;
    private RecipeRepository mRecetaRepository;
    private FirebaseStorage mStorage;

private static ColeccionViewModel coleccionViewModel;
    public static ColeccionViewModel getInstance(){
        if (coleccionViewModel == null) coleccionViewModel = new ColeccionViewModel();
        return coleccionViewModel;
    }

    public ColeccionViewModel() {
        mRecipes = new MutableLiveData<>(new ArrayList<>());
        recetasVacias = new MutableLiveData<>();
        mStorage = FirebaseStorage.getInstance();
        mRecetaRepository = RecipeRepository.getInstance();

        receptesListener();

    }

    private void receptesListener() {
        mRecetaRepository.setOnLoadRecetaCollectionListener(new RecipeRepository.OnLoadRecipeCollection() {
            @Override
            public void onLoadRecipeCollection(ArrayList<Recipe> recetas) {
                ColeccionViewModel.this.setRecipes(recetas);
                if(recetas.isEmpty())
                    recetasVacias.setValue("VISIBLE");
                else
                    recetasVacias.setValue("GONE");
            }
        } );
    }

    public void loadRecetasOfUserFromRepository(ArrayList<String> idRecetasUser){
        mRecetaRepository.loadRecetasUser(mRecipes.getValue(), idRecetasUser, "COLLECTION");
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        mRecipes.setValue(recipes);
    }

    public MutableLiveData<ArrayList<Recipe>> getmRecipes() {
        return mRecipes;
    }

    public MutableLiveData<String> getEmptySignal(){return recetasVacias;}

    public FirebaseStorage getmStorage() {
        return mStorage;
    }
}