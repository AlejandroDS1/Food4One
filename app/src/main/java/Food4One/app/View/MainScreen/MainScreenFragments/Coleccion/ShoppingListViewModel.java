package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import Food4One.app.Model.Recipe.Ingredients.Ingrediente;
import Food4One.app.Model.Recipe.Ingredients.IngredientesList;
import Food4One.app.Model.User.UserRepository;

public class ShoppingListViewModel extends ViewModel {
    private final MutableLiveData<List<IngredientesList>> allLists;
    private final MutableLiveData<IngredientesList> unCheckedItems;
    private final MutableLiveData<IngredientesList> checkedItems;

    public ArrayList<OnChangedListsListener> onChangedListsListeners = new ArrayList<>();

    public interface OnChangedListsListener{
        void onChangedListListener();
    }

    public ShoppingListViewModel(){
        allLists = new MutableLiveData<>(new ArrayList<>());

        this.checkedItems = new MutableLiveData<>(new IngredientesList());
        this.unCheckedItems = new MutableLiveData<>(new IngredientesList());

        UserRepository.getInstance().loadUserIngredientesList(allLists);
        initListeners();
    }

    public void setOnChangedListsListener(OnChangedListsListener listener){ this.onChangedListsListeners.add(listener); }

    private void initListeners() {
        UserRepository.getInstance().setOnLoadListIngredientesListener(new UserRepository.OnLoadListIngredientesListener() {
            @Override
            public void onLoadListIngredientes() {
                if (allLists.getValue().isEmpty()) { // Si es igual a null es porque no tiene ingerdientes guardados

                }else{ // Si no es null es porque hay una lista.

                    // TODO HAY QUE CAMBIAR MUCHAS COSAS AQUI SI SON VARIAS LISAS.

                    for (IngredientesList _in : allLists.getValue()){

                        ArrayList<Ingrediente> list = _in.getIngredientes();

                        for (Ingrediente in : list){

                            if (in.checked) checkedItems.getValue().add(in);
                            else unCheckedItems.getValue().add(in);

                        }
                    }
                    for (OnChangedListsListener l : onChangedListsListeners)
                        l.onChangedListListener();
                }
            }
        });
    }


    public void swapCheckedItem(final Ingrediente ingrediente, final boolean checked) {

        ingrediente.checked = checked;

        if (checked){
            this.unCheckedItems.getValue().remove(ingrediente);
            this.checkedItems.getValue().add(ingrediente);
        }else{
            this.unCheckedItems.getValue().add(ingrediente);
            this.checkedItems.getValue().remove(ingrediente);
        }
    }

    // ViewModel
    public LiveData<IngredientesList> getCheckedItemsList(){
        return this.checkedItems;
    }
    public LiveData<IngredientesList> getUnCheckedItemsList(){
        return this.unCheckedItems;
    }


}
