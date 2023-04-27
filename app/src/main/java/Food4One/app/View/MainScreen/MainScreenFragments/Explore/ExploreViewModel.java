package Food4One.app.View.MainScreen.MainScreenFragments.Explore;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.checkerframework.checker.units.qual.A;

import java.net.HttpCookie;
import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;
import Food4One.app.Model.Recipe.Recipe.RecipesUserApp;
import Food4One.app.View.MainScreen.MainScreenFragments.Perfil.PerfilViewModel;


public class ExploreViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Recipe>> mRecetas;
    static ExploreViewModel exploreViewModel;
    private final RecipeRepository mRecipeRepository;


    public static ExploreViewModel getInstance(){
        if (exploreViewModel == null) exploreViewModel = new ExploreViewModel();
        return exploreViewModel;
    }

    public ExploreViewModel() {
        mRecetas = new MutableLiveData<>(new ArrayList<>());
        mRecipeRepository = RecipeRepository.getInstance();

        recetasListeners();
    }

    private void recetasListeners() {
        //Al cargar las recetas el observador será notificado y se acutalizará la lista
        mRecipeRepository.addOnLoadRecetaListener(new RecipeRepository.OnLoadRecetaListener() {
            @Override
            public void onLoadRecetas(ArrayList<Recipe> recetas) {
                ExploreViewModel.this.setmRecetas(recetas);
            }
        });
    }

    public void loadRecetasExplorer(){ mRecipeRepository.loadRecetas(mRecetas.getValue());}

    public void setmRecetas(ArrayList<Recipe> recetas){ this.mRecetas.setValue(recetas);    }

    public MutableLiveData<ArrayList<Recipe>> getRecetas() { return mRecetas;  }
}