package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
    private UserRepository userRepository;

    public ShoppingListViewModel(){
        userRepository = UserRepository.getInstance();

        this.unCheckedIngredientes = new MutableLiveData<>();
        this.checkedIngredientes = new MutableLiveData<>(new IngredientesList());

        this.dao = new MutableLiveData<>();
        setIngredientesListeners();
    }

    public void setDao(@NonNull final Context context){
        this.dao.setValue(Room.databaseBuilder(context, ShoppingListDataBase.class, ShoppingList.TAG_DB).allowMainThreadQueries().build().shoppingListDao());

        fillLists();
    }

    private void fillLists(){

        final List<ShoppingList> unCheckedItems = getDao().getUnCheckedItems();

        final List<ShoppingList> checkedItems = getDao().getCheckedItems();

        this.unCheckedIngredientes.setValue(new IngredientesList((ArrayList<ShoppingList>) unCheckedItems, unCheckedItems.get(0).listName));
        this.checkedIngredientes.setValue(new IngredientesList((ArrayList<ShoppingList>) checkedItems, unCheckedItems.get(0).listName));

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
            this.unCheckedIngredientes.getValue().add(ingrediente);
            this.checkedIngredientes.getValue().remove(ingrediente);

            // Ahora actualizamos la base de datos
            getDao().checkIngrediente("PRUEBA", ingrediente.getId(), check);
        }else{
            this.checkedIngredientes.getValue().add(ingrediente);
            this.unCheckedIngredientes.getValue().remove(ingrediente);

            // Ahora actualizamos la base de datos
            getDao().checkIngrediente("PRUEBA", ingrediente.getId(), check);
        }

    }

    public void setIngredientesListeners(){
        userRepository.setmOnLoadIngredientesListListener(new UserRepository.OnLoadIngredientesListListener() {
            @Override
            public void onLoadIngredientesList(IngredientesList ingredientesList) {
                setUnCheckedIngredientes(ingredientesList);
            }
        });
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
}
