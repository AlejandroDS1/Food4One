package Food4One.app.View.MainScreen.MainScreenFragments.Explore;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;
import Food4One.app.Model.Recipe.Recipe.RecipesUserApp;
import Food4One.app.Model.User.UserRepository;
import Food4One.app.View.MainScreen.MainScreenFragments.home.HomeViewModel;


public class ExploreViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Recipe>> mRecetas;
    private final MutableLiveData<String> userURLFromRecipe;
    static ExploreViewModel exploreViewModel;
    private final RecipeRepository mRecipeRepository;

    public static ExploreViewModel getInstance(){
        if (exploreViewModel == null) exploreViewModel = new ExploreViewModel();
        return exploreViewModel;
    }

    public ExploreViewModel() {
        mRecetas = new MutableLiveData<>(new ArrayList<>());
        userURLFromRecipe = new MutableLiveData<>();
        mRecipeRepository = RecipeRepository.getInstance();
        recetasListeners();
    }

    private void recetasListeners() {
        //Al cargar las recetas el observador será notificado y se acutalizará la lista

        mRecipeRepository.setOnLoadRecetasExplorer(new RecipeRepository.OnLoadRecipeExplorer() {
            @Override
            public void onLoadRecipeExplorer(ArrayList<Recipe> recetas) {
                RecipesUserApp.setRecetasExplorer(recetas);
                ExploreViewModel.this.setmRecetas(recetas);
            }
        });
        mRecipeRepository.setmOnLoadURLfromRecipe(new RecipeRepository.OnLoadURLUserFromRecipe() {
            @Override
            public void OnLoadURLUserRecipe(String URL) {
                userURLFromRecipe.setValue(URL);
            }
        });
    }

    public void loadRecetasExplorer(){
            mRecipeRepository.loadRecetas(mRecetas.getValue(), "EXPLORER");
    }

    public void setmRecetas(ArrayList<Recipe> recetas){ this.mRecetas.setValue(recetas);    }

    public MutableLiveData<ArrayList<Recipe>> getRecetas() { return mRecetas;  }

    public MutableLiveData<String> getUserURLFromRecipe(){return userURLFromRecipe;}


    public void onClikDoRecipe(Recipe recipe) {
        HomeViewModel.getInstance().loadRecipeToMake(recipe);
    }
    public void onClickLikeRecipe(Recipe recipe, boolean like) {
        UserRepository.getInstance().setUserLikeDDB(recipe,like );
    }

    public void onClickSaveRecipe(Recipe recipe, boolean saved) {
        UserRepository.getInstance().setUserRecetaCollectionDDB(recipe, saved);
    }
}