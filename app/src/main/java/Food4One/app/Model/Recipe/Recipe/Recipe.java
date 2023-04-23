package Food4One.app.Model.Recipe.Recipe;


import java.util.ArrayList;

import Food4One.app.Model.Alergias;
import Food4One.app.Model.Recipe.Ingredients.IngredientesList;

public class Recipe {

    private String idUser;
    private String nombre;
    private IngredientesList ingredientes;
    private ArrayList<Alergias> alergias; // TODO: HASMAP CON NOMBRES -> POSIBLE OPTIMIZACION EN UN FUTURO.
    private String pictureURL; //MAYBE UN ARRAY PORQUE HAY VARIAS IMAGENES
    private String Description;
    private int likes;
    private ArrayList<String> pasos;

    // TODO: COMO IMPLEMENTAR LOS PASOS. NOTES
    // ATRIBUTOS OPCIONALES E IDEAS A IMPLEMETNTAR
    // Nivel

    public Recipe(){} //Para la base de Datos se necesita un constructor sin argumentos...
    //Constructor
    public Recipe(
            final String nombre,
            final String pictureURL,
            int likes,
            final IngredientesList ingredientes,
            final ArrayList<String> pasos
    )
    {
        this.nombre = nombre;
        this.pictureURL = pictureURL;
        this.likes = likes;
        this.ingredientes = ingredientes;
        this.pasos = pasos;
    }


    /**
     * Constructor Dummy
     */

    public Recipe(String nombre, IngredientesList ingredientes, String pictureURL, String Description, int likes, ArrayList<String> pasos) {
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.pictureURL = pictureURL;
        this.Description = Description;
        this.likes = likes;
        this.pasos = pasos;
    }

    //Getters

    //------------..-GETTERS-----------------------------
    public String getNombre() { return nombre;}
    public ArrayList<String> getPasos() { return pasos; }
    public IngredientesList getIngredientes() { return ingredientes; }
    public String getPictureURL() {  return pictureURL; }
    public int getLikes() { return likes; }
    public String getIdUser(){return idUser;}
    public String getDescription(){ return Description; }

    //---------------SETTERS----------------------------------------
    public void setNombre(String nombre) { this.nombre = nombre;  }
    public void setPasos(ArrayList<String> pasos) { this.pasos = pasos; }
    public void setIngredientes(IngredientesList ingredientes){ this.ingredientes = ingredientes; }
    public void setPictureURL(String pictureURL) { this.pictureURL = pictureURL; }
    public void setLikes(int likes) { this.likes = likes; }
    public void setIdUser(String idUser){ this.idUser = idUser; }
    public void setDescription(String description){ Description = description; }

    //REMOVE
    public boolean removeAlergias(Alergias alergia){
        return this.alergias.remove(alergia);
    }
}
