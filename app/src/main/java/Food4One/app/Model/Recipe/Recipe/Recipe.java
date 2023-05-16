package Food4One.app.Model.Recipe.Recipe;


import java.util.ArrayList;

import Food4One.app.Model.Recipe.Ingredients.IngredientesList;

public class Recipe {

    public static final String TAG = "Recetas";
    public static final String PICTURE_APP_TAG = "PictureURL";
    public static final String DESCRIPTION_APP_TAG = "Description";
    public static final String PASOS_APP_TAG = "Pasos";
    public static final String INGREDIENTES_APP_TAG = "Ingredientes";
    public static final String LIKES_TAG = "likes";
    private String idUser;
    private String nombre;
    private String Description;
    private ArrayList<String> pasos;
    private IngredientesList ingredientes;
    private ArrayList<String> alergias;
    private String pictureURL;
    private int likes;
    private boolean likeFromUser;
    private String userPhoto; // Foto del usuario

    public Recipe(){} //Para la base de Datos se necesita un constructor sin argumentos...
    //Constructor
    public Recipe(  final String nombre,
                    final String pictureURL,
                    int likes,
                    final IngredientesList ingredientes,
                    final ArrayList<String> pasos
                  ){
        this.nombre = nombre;
        this.pictureURL = pictureURL;
        this.likes = likes;
        this.ingredientes = ingredientes;
        this.pasos = pasos;
    }

    public void setLikeFromUser(boolean like){
        this.likeFromUser = like;
    }
    public boolean getLikeFromUser(){
        return likeFromUser;
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
    public String getIdUser() { return idUser; }

    public String getDescription(){ return Description; }
    public String getUserPhoto(){ return userPhoto; }

    //---------------SETTERS----------------------------------------
    public void setNombre(String nombre) { this.nombre = nombre;  }
    public void setPasos(ArrayList<String> pasos) { this.pasos = pasos; }
    public void setIngredientes(IngredientesList ingredientes){ this.ingredientes = ingredientes; }
    public void setPictureURL(String pictureURL) { this.pictureURL = pictureURL; }
    public void setLikes(int likes) { this.likes += likes; }
    public void setIdUser(String idUser){ this.idUser = idUser; }
    public void setDescription(String description){ Description = description; }

    //REMOVE
    public boolean removeAlergias(String alergia){
        return this.alergias.remove(alergia);
    }

    public void setPhotoUser(String string) {
        this.userPhoto = string;
    }
}
