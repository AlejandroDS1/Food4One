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
    // Listener atributes
    public List<OnChangedListsListener> onChangedListsListener;
    private OnListIsEmptyListener onListIsEmptyListener;

    // Definicion de las interficies para listeners
    public interface OnChangedListsListener{
        void onChangedListListener();
    }

    public interface OnListIsEmptyListener{
        void onListIsEmptyListener();
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

        this.onChangedListsListener = new ArrayList<>();
    }

    // Metodos para asignar los listeners
    public void setOnChangedListsListener(OnChangedListsListener listener){
        this.onChangedListsListener.add(listener);
    }

    public void setOnListIsEmptyListener(OnListIsEmptyListener listener) {this.onListIsEmptyListener = listener; }

    // Metodos ViewModel
    public void loadIngredientesList_fromDDBB() {
        UserRepository.getInstance().loadUserIngredientesList(allLists);
    }

    private void initListeners() {
        UserRepository.getInstance().setOnLoadListIngredientesListener(new UserRepository.OnLoadListIngredientesListener() {
            @Override
            public void onLoadListIngredientes() {
                if (allLists.getValue() == null) allLists.setValue(new HashMap<>());
                if (allLists.getValue().isEmpty()) { // Si es igual a null es porque no tiene ingerdientes guardados
                    if(onListIsEmptyListener != null)
                        onListIsEmptyListener.onListIsEmptyListener();
                }else // Si no es null es porque hay una lista.
                    if (onChangedListsListener != null)
                        for (OnChangedListsListener listener : onChangedListsListener)
                            listener.onChangedListListener();
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

    public void addIngredientesList_toDDBB(@NonNull final List<String> ingredientes, @NonNull final String listName){

        Map<String, Boolean> newList = new HashMap<>();
        //Habrá el caso en que el usuario cree una nueva lista, así que hay que ver si existe en la App
        Map<String, Boolean>  actualMap =  allLists.getValue().get(listName);

        //Si no es una nueva entonces hay que guardar los nuevos ingredientes con la actual.
        if(actualMap!= null)
            newList.putAll(actualMap);

        for(final String in: ingredientes)
            newList.put(in, false);

        this.allLists.getValue().put(listName, newList);
        updateIngredientesList();
    }

    public void addIngredientesList_toDDBB(@NonNull final IngredientesList ingredientes){

        Map<String, Boolean> newList = new HashMap<>();

        for(final Ingrediente in: ingredientes.getIngredientes())
            newList.put(in.getId(), in.checked);

        this.allLists.getValue().put(ingredientes.getListName(), newList);

        updateIngredientesList();
    }

    public void updateIngredientesList(){
        UserRepository.getInstance().setUserIngredientesListDDBB(this);
    }

    public void changeListName(@NonNull final String newListaName, @NonNull final String prevListName) {

        Map<String, Boolean> ingredientesList = this.allLists.getValue().get(prevListName);

        allLists.getValue().remove(prevListName);
        allLists.getValue().put(newListaName, ingredientesList);

        this.deleteList(prevListName);
    }


    /**
     * Elimina 'listaName' de la lista de listas. Lo guarda en base de datos
     * @param listaName Nombre de la lista de listas
     */
    public void deleteList(@NonNull final String listaName) {

        // Si no se encuentra la lista no hacemos nada
        if (this.allLists.getValue().get(listaName) == null) return;

        // Si se ha encontrado la lista, eliminamos la lista, y actualizamos la base de datos.
        allLists.getValue().remove(listaName);

        UserRepository.getInstance().deleteListUser(listaName);
    }


}
