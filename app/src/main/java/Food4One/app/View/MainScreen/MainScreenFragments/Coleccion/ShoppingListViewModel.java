package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import Food4One.app.Model.Recipe.Ingredients.IngredientesList;
import Food4One.app.Model.User.UserRepository;

public class ShoppingListViewModel extends ViewModel {
    private final MutableLiveData<IngredientesList> mIngredients;

    private UserRepository userRepository;

    public ShoppingListViewModel(MutableLiveData<IngredientesList> mIngredients) {
        this.mIngredients = mIngredients;
    }

    public ShoppingListViewModel(){
        mIngredients = new MutableLiveData<>();
        mIngredients.setValue(new IngredientesList());
        userRepository = UserRepository.getInstance();
        setIngredientesListeners();
    }

    public void setIngredientesListeners(){
        userRepository.setmOnLoadIngredientesListListener(new UserRepository.OnLoadIngredientesListListener() {
            @Override
            public void onLoadIngredientesList(IngredientesList ingredientesList) {
                setmIngredients(ingredientesList);
            }
        });

    }

    public void setmIngredients(IngredientesList obj){
        this.mIngredients.setValue(obj);
    }
    public LiveData<IngredientesList> getIngredientesList(){
        return this.mIngredients;
    }
}
