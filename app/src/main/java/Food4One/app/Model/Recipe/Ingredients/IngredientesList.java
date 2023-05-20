package Food4One.app.Model.Recipe.Ingredients;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IngredientesList implements Serializable {

    private String listName; // ID para receta o nombre.
    private ArrayList<Ingrediente> ingredientes;


    // Constructores

    /**
     * Crea una lista de ingredientes con su nombre y los id de los ingredientes.
     * @param listName nombre de la lista
     * @param ingredientesId todos los id de los ingredientes que componen la lista
     */
    public IngredientesList(@NonNull final String listName,
                            @NonNull final Map<String, Boolean> ingredientesId) {
        this.listName = listName;

        this.ingredientes = new ArrayList<>();

        for(final String id: ingredientesId.keySet())
            ingredientes.add(new Ingrediente(id, ingredientesId.get(id)));
    }

    public IngredientesList(@NonNull final List<Ingrediente> ingredientes){
        this.ingredientes = (ArrayList<Ingrediente>) ingredientes;
    }

    public IngredientesList() { this.ingredientes = new ArrayList<>(); }
    public final ArrayList<Ingrediente> IngredienteId_toIngredienteList(@NonNull final Set<String> _ingredientesId){

        ArrayList<Ingrediente> arr = new ArrayList<>();

        for (final String s: _ingredientesId)
            arr.add(new Ingrediente(s));
        return arr;
    }
    // METODOS
    public void remove(Ingrediente ingrediente) {
        this.ingredientes.remove(ingrediente);
    }

    public void removeAt(final int pos){ this.ingredientes.remove(pos); }
    public void add(Ingrediente ingrediente){
        ingredientes.add(ingrediente);
    }

    public ArrayList<Ingrediente> toArrayList() {
        return ingredientes;
    }

    //Getter
    public int getSize(){
        return this.ingredientes.size();
    }

    public String getListName() {
        return listName;
    }

    //Setter
    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setIngredientes(ArrayList<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    /**
     * Dados los id de los ingredientes, se crean los ingredientes y se llena el
     * array de ingredientes
     * @param ingredientes_id array que contiene los id de ingredientes ( ver formato de id en Ingredientes.java)
     */
    public IngredientesList setIngredientes(@NonNull List<String> ingredientes_id){
        for (String _ingrediente : ingredientes_id){
            this.ingredientes.add(new Ingrediente(_ingrediente));
        }
        return this;
    }

    public Ingrediente get(final int pos){
        return this.ingredientes.get(pos);
    }
    public Ingrediente getIngrediente(Ingrediente ingrediente){

        for (Ingrediente _ingrediente: this.ingredientes){
            if (_ingrediente.equals(ingrediente)) return _ingrediente;
        }
        return null;
    }

    public boolean contains(Ingrediente ingrediente){
        return this.ingredientes.contains(ingrediente);
    }

    public IngredientesList mergeLists(IngredientesList _list){

        Ingrediente iterator;

        // Iteramos sobre la lista para identificar elementos repetidos.
        for (Ingrediente ingrediente: _list.toArrayList()){
            iterator = this.getIngrediente(ingrediente); //Buscamos si ya existe el elemento

            if (iterator != null) // Si existe, cambiamos el multiplicador.
                iterator.setMultiplicador(iterator.getMultiplicador() + ingrediente.getMultiplicador());
            else // Si no existe lo añadimos.
                this.add(ingrediente);
        }
        return this;
    }

    public ArrayList<String> getArrayString(){
        ArrayList<String> stringIngredientes = new ArrayList<>();

        for (Ingrediente ing: ingredientes)
            stringIngredientes.add(ing.toString());
        return stringIngredientes;
    }

    public ArrayList<String> toArrayStringId(){
        ArrayList<String> stringIngredientes = new ArrayList<>();

        for (Ingrediente ing: ingredientes)
            stringIngredientes.add(ing.getId());
        return stringIngredientes;
    }

    public ArrayList<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    @Override
    public String toString(){
        return this.ingredientes.toString();
    }
}
