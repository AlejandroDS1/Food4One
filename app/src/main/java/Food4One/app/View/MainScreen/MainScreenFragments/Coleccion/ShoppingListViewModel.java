package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Food4One.app.Model.Recipe.Ingredients.Ingrediente;
import Food4One.app.Model.Recipe.Ingredients.IngredientesList;
import Food4One.app.Model.User.UserRepository;

public class ShoppingListViewModel extends ViewModel {

    private final MutableLiveData<Map<String, Map<String, Boolean>>> allLists;
//    private final MutableLiveData<List<IngredientesList>> allLists;
    private final MutableLiveData<IngredientesList> unCheckedItems;
    private final MutableLiveData<IngredientesList> checkedItems;
    public OnChangedListsListener onChangedListsListener;

    public interface OnChangedListsListener{
        void onChangedListListener();
    }

    private static ShoppingListViewModel instance;

    public static ShoppingListViewModel getInstance(){
        if (instance == null) instance = new ShoppingListViewModel();
        return instance;
    }

    private ShoppingListViewModel(){
        allLists = new MutableLiveData<>(new HashMap<>());

        this.checkedItems = new MutableLiveData<>(new IngredientesList());
        this.unCheckedItems = new MutableLiveData<>(new IngredientesList());

        loadIngredientesList_fromDDBB();
        initListeners();
    }

    public void loadIngredientesList_fromDDBB() {
        UserRepository.getInstance().loadUserIngredientesList(allLists);
    }

    public void setOnChangedListsListener(OnChangedListsListener listener){ this.onChangedListsListener = listener; }

    private void initListeners() {
        UserRepository.getInstance().setOnLoadListIngredientesListener(new UserRepository.OnLoadListIngredientesListener() {
            @Override
            public void onLoadListIngredientes() {
                if (allLists.getValue().isEmpty()) { // Si es igual a null es porque no tiene ingerdientes guardados

                }else // Si no es null es porque hay una lista.
                    if (onChangedListsListener != null) onChangedListsListener.onChangedListListener();

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

    public void setChoosedList(@NonNull final String listName){
        final IngredientesList ingredientesList = IngredientesList.map_toSingleList(allLists.getValue(), listName);
        setIngredientesList(ingredientesList);
    }

    private void setIngredientesList(@NonNull final IngredientesList ingredientesList){

        this.checkedItems.getValue().clear();
        this.unCheckedItems.getValue().clear();

        this.checkedItems.getValue().setListName(ingredientesList.getListName());
        this.unCheckedItems.getValue().setListName(ingredientesList.getListName());



        for (Ingrediente in : ingredientesList.getIngredientes()){
            if (in.checked) this.checkedItems.getValue().add(in);
            else this.unCheckedItems.getValue().add(in);
        }
    }

    public List<String> getAllListsNames(){
        return new ArrayList<>(this.allLists.getValue().keySet());
    }

    // ViewModel
    public LiveData<IngredientesList> getCheckedItemsList(){
        return this.checkedItems;
    }
    public LiveData<IngredientesList> getUnCheckedItemsList(){
        return this.unCheckedItems;
    }

    public Map<String, Map<String, Boolean>> getMapAllLists_toDDBB(){
        return this.allLists.getValue();
    }

    public void addIngredientesList_toDDBB(@NonNull final List<String> ingredientes){

        final String listName = "PRUEBA2";

        Map<String, Boolean> newList = new HashMap<>();

        for(final String in: ingredientes)
            newList.put(in, false);

        // TODO de momento no compruebo si hay otra lista que se llama igual a si que se sobreescribe
        this.allLists.getValue().put(listName, newList);

        UserRepository.getInstance().setUserIngredientesListDDBB(this);
    }
    public void addIngredientesList_toDDBB(@NonNull final IngredientesList ingredientes){

        final String listName = "PRUEBA2";

        Map<String, Boolean> newList = new HashMap<>();

        for(final Ingrediente in: ingredientes.getIngredientes())
            newList.put(in.getId(), in.checked);

        // TODO de momento no compruebo si hay otra lista que se llama igual a si que se sobreescribe
        this.allLists.getValue().put(listName, newList);

        UserRepository.getInstance().setUserIngredientesListDDBB(this);
    }

}
