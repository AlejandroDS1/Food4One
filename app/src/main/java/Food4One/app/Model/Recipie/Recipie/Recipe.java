package Food4One.app.Model.Recipie.Recipie;


import java.util.ArrayList;

import Food4One.app.Model.Alergias;
import Food4One.app.Model.Recipie.Ingredients.IngredientesList;

public class Recipe {
    private String idUser;
    private String nombre;
    private ArrayList<String> pasos;
    private IngredientesList ingredientes;
    private ArrayList<Alergias> alergias; // TODO: HASMAP CON NOMBRES -> POSIBLE OPTIMIZACION EN UN FUTURO.
    private String pictureURL;
    private int likes;
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

    //------------..-GETTERS-----------------------------
    public String getNombre() { return nombre;}
    public ArrayList<String> getPasos() { return pasos; }
    public IngredientesList getIngredientes() { return ingredientes; }
    public String getPictureURL() {  return pictureURL; }
    public int getLikes() { return likes; }
    public String getIdUser(){return idUser;}

    //---------------SETTERS----------------------------------------
    public void setNombre(String nombre) { this.nombre = nombre;  }
    public void setPasos(ArrayList<String> pasos) { this.pasos = pasos; }
    public void setIngredientes(IngredientesList ingredientes){ this.ingredientes = ingredientes; }
    public void setPictureURL(String pictureURL) { this.pictureURL = pictureURL; }
    public void setLikes(int likes) { this.likes = likes; }

    //REMOVE
    public boolean removeAlergias(Alergias alergia){
        return this.alergias.remove(alergia);
    }
}