package Food4One.app.DataBase.ShoppingList;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

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
    public ShoppingList(final String listName, final String ingrediente, final boolean checked){
        this.ingrediente = ingrediente;
        this.listName = listName;
        this.checked = checked;
    }

    public static List<ShoppingList> convertToShoppingListFromDDBB(final List<String> listDB){

        List<ShoppingList> mList = new ArrayList<>();

        for(final String s: listDB){
            final String [] elems = s.split("[#]");

                                     // ListName  Ingrediente ID  checked
            mList.add(new ShoppingList(elems[0], elems[1], elems[2].equals("true")));
        }
        return mList;
    }

    public static String convertToIdDDBB(final ShoppingList shoppingList){
        return shoppingList.listName + "#" + shoppingList.ingrediente + "#" + shoppingList.checked;
    }
}
