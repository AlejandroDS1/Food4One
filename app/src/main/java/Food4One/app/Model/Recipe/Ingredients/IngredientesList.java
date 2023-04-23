package Food4One.app.Model.Recipe.Ingredients;

import java.io.Serializable;
import java.util.ArrayList;

public class IngredientesList implements Serializable {

    private String id; // ID para receta o nombre.
    private ArrayList<Ingrediente> ingredientes;

    public IngredientesList(final ArrayList<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public IngredientesList(final ArrayList<Ingrediente> ingredientes, final String id){
        this.ingredientes = new ArrayList<>(ingredientes);
        this.id = id;
    }

    public IngredientesList() {
        this.ingredientes = new ArrayList<Ingrediente>();
    }

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

    public String getId() {
        return id;
    }

    //Setter
    public void setId(String id) {
        this.id = id;
    }

    public void setIngredientes(ArrayList<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
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

    @Override
    public String toString(){
        return this.ingredientes.toString();
    }
}
