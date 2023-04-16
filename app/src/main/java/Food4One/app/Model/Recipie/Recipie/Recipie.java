package Food4One.app.Model.Recipie.Recipie;


import java.util.ArrayList;

import Food4One.app.Model.Alergias;
import Food4One.app.Model.Recipie.Ingredients.IngredientesList;

public class Recipie {

    private String nombre;
    private IngredientesList ingredientes;
    private ArrayList<Alergias> alergias; // TODO: HASMAP CON NOMBRES -> POSIBLE OPTIMIZACION EN UN FUTURO.
    private int imagen; //MAYBE UN ARRAY PORQUE HAY VARIAS IMAGENES

    // TODO: COMO IMPLEMENTAR LOS PASOS. NOTES
    // ATRIBUTOS OPCIONALES E IDEAS A IMPLEMETNTAR
    // Nivel

    //Constructor
    public Recipie(final String nombre, final IngredientesList ingredientes, final ArrayList<Alergias> alergias, final int imagen) {
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.alergias = alergias;
        this.imagen = imagen;
    }

    //Getters

    public String getNombre() {
        return nombre;
    }

    public IngredientesList getIngredientes() {
        return ingredientes;
    }

    public ArrayList<Alergias> getAlergias() {
        return alergias;
    }

    public boolean addAlergia(Alergias alergia){
        return this.alergias.add(alergia);
    }

    public boolean removeAlergias(Alergias alergia){
        return this.alergias.remove(alergia);
    }
}
