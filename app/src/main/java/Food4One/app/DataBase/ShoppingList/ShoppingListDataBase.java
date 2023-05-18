package Food4One.app.DataBase.ShoppingList;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {ShoppingList.class},
        version = 1
)
public abstract class ShoppingListDataBase extends RoomDatabase {

    public abstract ShoppingListDao shoppingListDao();
}
