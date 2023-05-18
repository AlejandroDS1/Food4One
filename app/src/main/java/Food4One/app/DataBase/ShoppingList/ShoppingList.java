package Food4One.app.DataBase.ShoppingList;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ShoppingList")
public class ShoppingList {

    public final static String TAG_DB = "ShoppingList.db";

    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    @ColumnInfo(name = "listName")
    public String listName;

    @ColumnInfo(name = "ingrediente")
    public String ingrediente;
    @ColumnInfo(name = "checked")
    public boolean checked = false;

    // Constructor
    public ShoppingList(final String listName, final String ingrediente){
        this.ingrediente = ingrediente;
        this.listName = listName;
    }
}
