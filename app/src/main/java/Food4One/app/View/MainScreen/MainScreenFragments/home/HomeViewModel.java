package Food4One.app.View.MainScreen.MainScreenFragments.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import Food4One.app.DataBase.ShoppingList.ShoppingList;
import Food4One.app.DataBase.ShoppingList.ShoppingListDao;
import Food4One.app.DataBase.ShoppingList.ShoppingListDataBase;
import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Recipe>> mRecetasApp;
    private final MutableLiveData<Recipe> mdoRecipe;
    private RecipeRepository mRecetasRepository;
    private final MutableLiveData<ShoppingListDao> shoppingListDao;
    private final MutableLiveData<List<String>> selectedIngredients;

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

        this.shoppingListDao = new MutableLiveData<>();
        this.selectedIngredients = new MutableLiveData<>(new ArrayList<>());

        recetasAppListener();
        recetaMakeListener();
    }

    public void setDao(final Context context){
        this.shoppingListDao.setValue(Room.databaseBuilder(context, ShoppingListDataBase.class, ShoppingList.TAG_DB)
                            .allowMainThreadQueries().build().shoppingListDao());
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

    public boolean saveIngredientesDB(List<String> ingredientes){

        // TODO ACABAR

        final String listName = "PRUEBA";

        List<ShoppingList> shoppingList = new ArrayList<>();

        for (String s: ingredientes){

            // Añadimos los ingredientes a la base de datos
            shoppingList.add(new ShoppingList(listName, s));
        }

        shoppingListDao.getValue().upsertList(shoppingList);

        return true;
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


    public void deleteDB() {
        this.shoppingListDao.getValue().deleteAll();
    }
}