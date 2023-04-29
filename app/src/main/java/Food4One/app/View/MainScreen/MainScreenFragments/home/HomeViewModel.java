package Food4One.app.View.MainScreen.MainScreenFragments.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.storage.FirebaseStorage;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Recipe>> mRecetasApp;
    private final MutableLiveData<Recipe> mdoRecipe;
    private RecipeRepository mRecetasRepository;

    private FirebaseStorage mStorage;
    private static HomeViewModel homeViewModel;

    public static HomeViewModel getInstance(){
        if (homeViewModel == null)
            homeViewModel = new HomeViewModel();
        return homeViewModel;
    }

    public HomeViewModel() {
        mdoRecipe = new MutableLiveData<>();
        mRecetasApp = new MutableLiveData<>(new ArrayList<>());
        mRecetasRepository = RecipeRepository.getInstance();

        recetasAppListener();
        recetaMakeListener();
    }

    private void recetaMakeListener() {
        mRecetasRepository.addOnLoadRecipeToMake(new RecipeRepository.OnLoadRecipeToMake() {
            @Override
            public void OnLoadRecipe(Recipe recipeToDo) {
                setNewDoingRecipe(recipeToDo);
            }
        });
    }

    private void recetasAppListener() {
        //Al cargar las recetas el observador será notificado y se acutalizará la lista
        mRecetasRepository.addOnLoadRecetaAppListener(new RecipeRepository.OnLoadRecetaAppListener() {
            @Override
            public void OnLoadRecetaApp(ArrayList<Recipe> recetas) {
                setRecetesApp(recetas);
            }
        });
    }
    //------------------GETTERS--------------------------------------------------------
    public LiveData<ArrayList<Recipe>> getRecetes() {  return mRecetasApp; }
    public MutableLiveData<Recipe> getDoRecipe() { return mdoRecipe; }
    //-------------------SETTERS-------------------------------------------------------
    public void setRecetesApp(ArrayList<Recipe> recetas) { mRecetasApp.setValue(recetas); }
    public void setNewDoingRecipe(Recipe receta){ mdoRecipe.setValue(receta); }
    //---------------------LOAD FROM BBD-----------------------------------------------
    public void loadRecetasApp(String selection){
        mRecetasRepository.loadRecipesApp( mRecetasApp.getValue(), selection);
    }
    public void loadRecipeToMake(Recipe recipe){
        mRecetasRepository.loadRecipeToMake(recipe);
    }


}