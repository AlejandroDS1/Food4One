package Food4One.app.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import Food4One.app.DataBase.ShoppingList.ShoppingListDao;

public class RoomViewModel extends ViewModel {


    private final MutableLiveData<ShoppingListDao> dao;

    public RoomViewModel(){
        this.dao = new MutableLiveData<>();
    }

    public void setDao(ShoppingListDao dao) {
        this.dao.setValue(dao);
    }

    public void addEntity() {
        //this.dao.getValue().upsertList(new ShoppingList("Prueba"));
    }
}
