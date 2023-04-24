package Food4One.app.View.MainScreen.ui.Coleccion;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Ingredients.Ingrediente;

public class ShoppingListViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Ingrediente>> mIngredients;

    public ShoppingListViewModel(MutableLiveData<ArrayList<Ingrediente>> mIngredients) {
        this.mIngredients = mIngredients;


    }
}
