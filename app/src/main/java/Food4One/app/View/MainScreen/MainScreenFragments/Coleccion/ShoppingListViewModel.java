package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import Food4One.app.DataBase.ShoppingList.ShoppingList;
import Food4One.app.DataBase.ShoppingList.ShoppingListDao;
import Food4One.app.DataBase.ShoppingList.ShoppingListDataBase;
import Food4One.app.Model.Recipe.Ingredients.Ingrediente;
import Food4One.app.Model.Recipe.Ingredients.IngredientesList;
import Food4One.app.Model.User.UserRepository;

public class ShoppingListViewModel extends ViewModel {
    private final MutableLiveData<IngredientesList> unCheckedIngredientes;
    private final MutableLiveData<IngredientesList> checkedIngredientes;
    private final MutableLiveData<ShoppingListDao> dao;
    private final MutableLiveData<List<ShoppingList>> allLists;
    private final MutableLiveData<Boolean> loadCompleted;
    private UserRepository userRepository;

    private ArrayList<OnListChangedListener> mOnListChangedListeners = new ArrayList<>();

    // Interfaces

    public interface OnListChangedListener {
        void onChangedListener();
    }

    public void setOnChangedListListener(OnListChangedListener listener){
        mOnListChangedListeners.add(listener);
    }

    public ShoppingListViewModel(){
        userRepository = UserRepository.getInstance();

        this.unCheckedIngredientes = new MutableLiveData<>();
        this.checkedIngredientes = new MutableLiveData<>();
        this.allLists = new MutableLiveData<>(new ArrayList<>());

        this.loadCompleted = new MutableLiveData<>(false);

        this.dao = new MutableLiveData<>();
    }

    public void setDao(@NonNull ShoppingListFragment fragment){
        this.dao.setValue(Room.databaseBuilder(fragment.getContext(), ShoppingListDataBase.class, ShoppingList.TAG_DB).allowMainThreadQueries().build().shoppingListDao());

        checkedIngredientes.setValue(new IngredientesList(getDao().getCheckedItems(), "PRUEBA"));
        unCheckedIngredientes.setValue(new IngredientesList(getDao().getUnCheckedItems(), "PRUEBA"));


        initObservers(fragment);

        UserRepository.getInstance().loadUserIngredientesList(loadCompleted, allLists);
    }

    private void initObservers(@NonNull ShoppingListFragment fragment){
        final Observer<Boolean> ShoppingListLoadCompleted = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){ // Si aBoolean es true es porque se ha completado la carga de base de datos firebase.
                    // Entonces cargamos la nueva base de datos en la DB local

                    final ArrayList<ShoppingList> checkedList = new ArrayList<>();
                    final ArrayList<ShoppingList> unCheckedList = new ArrayList<>();

                    getDao().deleteAll();
                    getDao().upsertList(allLists.getValue());

                    // Separamos las listas segun sean elementos marcados o no.
                    for(ShoppingList shoppingList: allLists.getValue()){
                        if (shoppingList.checked){
                            checkedList.add(shoppingList);
                        }else unCheckedList.add(shoppingList);
                    }

                    // Guardamos las listas en los mutableLiveData del viewModel
                    checkedIngredientes.getValue().setIngredientesShopList(checkedList);
                    unCheckedIngredientes.getValue().setIngredientesShopList(unCheckedList);
//                    checkedIngredientes.setValue(new IngredientesList(checkedList, "PRUEBA"));
//                    unCheckedIngredientes.setValue(new IngredientesList(unCheckedList, "PRUEBA"));


                }else{ // Si aBoolean es false significa que el usuario no tiene internet o no puede cargar de base de datos, por lo que cargamos directamente de DB local
                    checkedIngredientes.setValue(new IngredientesList(getDao().getCheckedItems(), "PRUEBA"));
                    unCheckedIngredientes.setValue(new IngredientesList(getDao().getUnCheckedItems(), "PRUEBA"));

                    // TODO BORRAR
                }
                // Notifica que se ha cambiado la lista.
                for (OnListChangedListener listener : mOnListChangedListeners){
                    listener.onChangedListener();
                }

                Toast.makeText(fragment.getContext(), "Acaba", Toast.LENGTH_SHORT).show();
            }
        };
        this.loadCompleted.observe(fragment.getViewLifecycleOwner(), ShoppingListLoadCompleted);
    }

    public final ShoppingListDao getDao(){
        return this.dao.getValue();
    }

    /**
     * Este metodo cambia un ingrediente de lista segun si esta check o no.
     * @param ingrediente ingrediente que hay que cambiar
     * @param check esta seleccionado o no.
     */
    public void updateIngredienteCheckState(final Ingrediente ingrediente, final boolean check){

        if(check){
            this.checkedIngredientes.getValue().add(ingrediente);
            this.unCheckedIngredientes.getValue().remove(ingrediente);

        }else{
            this.unCheckedIngredientes.getValue().add(ingrediente);
            this.checkedIngredientes.getValue().remove(ingrediente);
        }
        // Ahora actualizamos la base de datos
        getDao().checkIngrediente("PRUEBA", ingrediente.getId(), check);
    }

    public void setUnCheckedIngredientes(IngredientesList obj){
        this.unCheckedIngredientes.setValue(obj);
    }
    public LiveData<IngredientesList> getUnCheckedIngredientes(){
        return this.unCheckedIngredientes;
    }

    public LiveData<IngredientesList> getCheckedIngredientes(){
        return this.checkedIngredientes;
    }

    public LiveData<Boolean> getCompleted() {
        return this.loadCompleted;
    }

    public void setListStateFireBase() {
        UserRepository.getInstance().setIngredientesListDDBB(allLists.getValue());
    }
}
