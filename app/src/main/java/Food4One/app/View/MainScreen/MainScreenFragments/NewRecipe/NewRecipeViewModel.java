package Food4One.app.View.MainScreen.MainScreenFragments.NewRecipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import Food4One.app.Model.Recipe.Ingredients.IngredientesList;
import Food4One.app.Model.User.UserRepository;

public class NewRecipeViewModel extends ViewModel {

    private final MutableLiveData<IngredientesList> ingredientesList;
    public NewRecipeViewModel() {
        this.ingredientesList = new MutableLiveData<>();

    }
}