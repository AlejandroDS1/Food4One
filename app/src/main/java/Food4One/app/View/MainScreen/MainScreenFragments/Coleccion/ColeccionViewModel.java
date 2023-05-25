package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;
import Food4One.app.Model.User.UserRepository;

public class ColeccionViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Recipe>> mRecipes;
    private static ColeccionViewModel coleccionViewModel;

    public static ColeccionViewModel getInstance(){
        if (coleccionViewModel == null) coleccionViewModel = new ColeccionViewModel();
        return coleccionViewModel;
    }

    public ColeccionViewModel() {
        mRecipes = new MutableLiveData<>(new ArrayList<>());
    }

    public void loadRecetasOfUserFromRepository(ArrayList<String> idRecetasUser){
        RecipeRepository.getInstance().loadRecetasUser(mRecipes.getValue(), idRecetasUser, "COLLECTION");
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        mRecipes.setValue(recipes);
    }

    public MutableLiveData<ArrayList<Recipe>> getmRecipes() {
        return mRecipes;
    }

}