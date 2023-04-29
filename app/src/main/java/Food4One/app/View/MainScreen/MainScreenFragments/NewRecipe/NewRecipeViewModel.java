package Food4One.app.View.MainScreen.MainScreenFragments.NewRecipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Ingredients.Ingrediente;
import Food4One.app.Model.Recipe.Ingredients.IngredientesList;

public class NewRecipeViewModel extends ViewModel {

    private final MutableLiveData<IngredientesList> ingredientesList;

    private final MutableLiveData<ArrayList<String>> alergiasList;
    public NewRecipeViewModel() {
        this.ingredientesList = new MutableLiveData<>();
        this.ingredientesList.setValue(new IngredientesList());

        this.alergiasList = new MutableLiveData<>();
    }

    public LiveData<IngredientesList> getIngredientesList() {
        return ingredientesList;
    }

    public LiveData<ArrayList<String>> getAlergiasList() {
        return alergiasList;
    }
    public void addIngrediente(Ingrediente _ingrediente){
        this.ingredientesList.getValue().add(_ingrediente);
    }
}