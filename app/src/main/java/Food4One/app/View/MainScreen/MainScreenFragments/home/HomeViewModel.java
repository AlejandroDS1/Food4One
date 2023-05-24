package Food4One.app.View.MainScreen.MainScreenFragments.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Recipe>> mRecetasApp;
    private final MutableLiveData<Recipe> mdoRecipe;
    private HashMap<String, ArrayList<Recipe>> recetasApp;
    private ArrayList<Recipe> pastaRecipes, riceRecipes, pastelRecipes, bebidasRecipes, bocatasRecipes, barbacoaRecipes;
    private RecipeRepository mRecetasRepository;
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

        initHashMapRecetasApp();
        recetasAppListener();
        recetaMakeListener();
    }

    private void initHashMapRecetasApp() {
        recetasApp = new HashMap<String, ArrayList<Recipe>>();
        pastaRecipes = new ArrayList<>(); riceRecipes = new ArrayList<>();
        pastelRecipes = new ArrayList<>(); bebidasRecipes = new ArrayList<>();
        barbacoaRecipes = new ArrayList<>(); bocatasRecipes = new ArrayList<>();
        recetasApp.put("Pasta", pastaRecipes); recetasApp.put("Arroz", riceRecipes);
        recetasApp.put("Bebidas", bebidasRecipes); recetasApp.put("Pastel", pastelRecipes);
        recetasApp.put("Barbacoa", barbacoaRecipes); recetasApp.put("Bocatas", bocatasRecipes);
    }

    public HashMap<String, ArrayList<Recipe>> getRecetasApp() {
        return recetasApp;
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
            public void OnLoadRecetaApp(ArrayList<Recipe> recetas, String type) {
                setRecetesApp(recetas);
                //Luego guardamos esas recetas en su Array correspondiente para no volverlos a cargar
                if(type.equals("Pasta")) pastaRecipes = recetas;
                else if (type.equals("Arroz")) riceRecipes = recetas;
                else if (type.equals("Bebidas")) bebidasRecipes =recetas;
                else if(type.equals("Barbacoa")) barbacoaRecipes = recetas;
                else if(type.equals("Bocatas")) bocatasRecipes = recetas;
                else pastelRecipes = recetas;
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
        ArrayList<Recipe> recetas = recetasApp.get(selection);
        //Si no se ha cargado ya de la base de datos, entonces lo cargamos.
        if(recetas.size()==0)
            mRecetasRepository.loadRecipesApp( mRecetasApp.getValue(), selection, "HOME");
        else //En el caso contrario, ya se han cargado las recetas y ya las tenemos almacenadas.
        {
            mRecetasApp.getValue().clear();
            setRecetesApp(recetas);
        }
    }
    public void loadRecipeToMake(Recipe recipe){
        mRecetasRepository.loadRecipeToMake(recipe);
    }

}