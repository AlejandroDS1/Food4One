package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;

public class ColeccionViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Recipe>> mRecipes;
    private RecipeRepository mRecetaRepository;
    private FirebaseStorage mStorage;

    public ColeccionViewModel() {
        mRecipes = new MutableLiveData<>(new ArrayList<>());
        mStorage = FirebaseStorage.getInstance();
        mRecetaRepository = RecipeRepository.getInstance();

        receptesListener();

    }

    private void receptesListener() {
        mRecetaRepository.addOnLoadRecetaListener(new RecipeRepository.OnLoadRecetaListener() {
            @Override
            public void onLoadRecetas(ArrayList<Recipe> recetas) {
                ColeccionViewModel.this.setRecipes(recetas);
            }
        });
    }

    public void loadRecetasOfUserFromRepository(ArrayList<String> idRecetasUser){
        receptesListener();
        mRecetaRepository.loadRecetasUser(mRecipes.getValue(), idRecetasUser); }

    private void setRecipes(ArrayList<Recipe> recipes) {
        mRecipes.setValue(recipes);
    }

    public MutableLiveData<ArrayList<Recipe>> getmRecipes() {
        return mRecipes;
    }

    public FirebaseStorage getmStorage() {
        return mStorage;
    }
}