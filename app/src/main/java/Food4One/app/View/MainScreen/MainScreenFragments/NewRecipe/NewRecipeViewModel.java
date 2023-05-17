package Food4One.app.View.MainScreen.MainScreenFragments.NewRecipe;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Ingredients.Ingrediente;
import Food4One.app.Model.Recipe.Ingredients.IngredientesList;
import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;
import Food4One.app.Model.Recipe.Recipe.RecipesUserApp;

public class NewRecipeViewModel extends ViewModel {

    private final MutableLiveData<Byte> succesUpdated_flag; // 0-> No subido, 1- Subido correctamente, 3.- Ha habido un error.
    private final MutableLiveData<IngredientesList> ingredientesList;
    private final MutableLiveData<ArrayList<String>> alergiasList;
    private final MutableLiveData<ArrayList<String>> stepsList;
    private final MutableLiveData<Uri> recipePhotoUri;

    public NewRecipeViewModel() {
        this.ingredientesList = new MutableLiveData<>();
        this.ingredientesList.setValue(new IngredientesList());

        this.alergiasList = new MutableLiveData<>();
        this.alergiasList.setValue(new ArrayList<>());

        this.stepsList = new MutableLiveData<>();
        this.stepsList.setValue(new ArrayList<>());

        this.recipePhotoUri = new MutableLiveData<>();

        this.succesUpdated_flag = new MutableLiveData<>();
        this.succesUpdated_flag.setValue((byte) 0);
    }

    public final void uploadRecipe(final String name, final String description) {
        // Creamos el objeto receta que vamos a subir.
        Recipe newRecipe = new Recipe(name,
                description,
                ingredientesList.getValue(),
                recipePhotoUri.getValue().toString(),
                0,
                stepsList.getValue(),
                alergiasList.getValue());

        RecipeRepository.getInstance().uploadRecipe(newRecipe, this.succesUpdated_flag);
    }

    public LiveData<IngredientesList> getIngredientesList() {
        return ingredientesList;
    }

    public LiveData<ArrayList<String>> getAlergiasList() {
        return alergiasList;
    }

    public void addIngrediente(Ingrediente _ingrediente) {
        this.ingredientesList.getValue().add(_ingrediente);
    }

    public LiveData<ArrayList<String>> getStepsList() {
        return stepsList;
    }

    public void addAlergiatoList(String a) {
        this.alergiasList.getValue().add(a);
    }

    public void addSteptoList(String s) {
        this.stepsList.getValue().add(s);
    }

    public final LiveData<Uri> getRecipePhotoUri() {
        return recipePhotoUri;
    }

    public void setRecipePhotoUri(Uri foto) {
        this.recipePhotoUri.setValue(foto);

    }

    public final LiveData<Byte> getUpdatedFlag(){ return this.succesUpdated_flag; }

    public final void setUpdateFlag(final byte flag){ this.succesUpdated_flag.setValue(flag); }
}
