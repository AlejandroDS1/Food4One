package Food4One.app.Model.Recipie.Recipie;


import java.util.ArrayList;

import Food4One.app.Model.Alergias;
import Food4One.app.Model.Recipie.Ingredients.IngredientesList;

public class Recipe {

    private String nombre;
    private IngredientesList ingredientes;
    private ArrayList<Alergias> alergias; // TODO: HASMAP CON NOMBRES -> POSIBLE OPTIMIZACION EN UN FUTURO.
    private String imagen; //MAYBE UN ARRAY PORQUE HAY VARIAS IMAGENES
    private String descripcio;
    private int likes;
    private ArrayList<String> steps;

    // TODO: COMO IMPLEMENTAR LOS PASOS. NOTES
    // ATRIBUTOS OPCIONALES E IDEAS A IMPLEMETNTAR
    // Nivel

    //Constructor
    public Recipe(final String nombre, final IngredientesList ingredientes, final ArrayList<Alergias> alergias, final String imagen) {
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.alergias = alergias;
        this.imagen = imagen;
    }


    /**
     * Constructor Dummy
     */

    public Recipe(String nombre, IngredientesList ingredientes, String imagen, String descripcio, int likes, ArrayList<String> steps) {
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.imagen = imagen;
        this.descripcio = descripcio;
        this.likes = likes;
        this.steps = steps;
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

    public String getPictureURL() { return this.imagen; }
}
