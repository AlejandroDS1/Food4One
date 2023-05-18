package Food4One.app.DataBase.ShoppingList;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;

@Dao
public interface ShoppingListDao {

    @Upsert
    void upsertList(ShoppingList shoppingList);

    @Upsert
    void upsertList(List<ShoppingList> shoppingLists);
    @Delete
    void deleteList(ShoppingList shoppingList);

    @Query("DELETE FROM ShoppingList")
    void deleteAll();

    @Query("UPDATE ShoppingList SET checked = :checked WHERE listName = :listName AND ingrediente = :ingrediente ")
    void checkIngrediente(final String listName, final String ingrediente, final boolean checked);

    @Query("SELECT id FROM ShoppingList WHERE listName = :listName AND ingrediente = :ingrediente")
    int getId(final String listName, final String ingrediente);

    @Query("SELECT checked FROM ShoppingList WHERE listName = :listName AND ingrediente = :ingrediente")
    boolean getCheckState(final String listName, final String  ingrediente);

    /**
     * @return Una list que contiene todas las listas almacenadas.
     */
    @Query("SELECT DISTINCT listName FROM shoppinglist")
    List<String> getLists();
    @Insert
    void addIngrediente(ShoppingList ingrediente);

    @Query("SELECT * FROM ShoppingList WHERE id = :id")
    ShoppingList getShoppingListById(final int id);

    @Query("SELECT * FROM ShoppingList")
    List<ShoppingList> getList();

    // Conseguimos solo los que estan checked
    @Query("SELECT * FROM ShoppingList WHERE checked = 1")
    List<ShoppingList> getCheckedItems();

    // Conseguimos solo los que NO estan checked
    @Query("SELECT * FROM ShoppingList WHERE checked = 0")
    List<ShoppingList> getUnCheckedItems();


}
