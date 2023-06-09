package Food4One.app.Model.Recipe.Recipe;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import Food4One.app.Model.Recipe.Ingredients.IngredientesList;


public class Recipe {
    public static final String TAG = "Recetas";
    public static final String PICTURE_APP_TAG = "PictureURL";
    public static final String PICTURE_URL_USER_TAG = "PictureURLuser";
    public static final String USER_ID_TAG = "idUser";
    public static final String LIKES_TAG = "likes";
    public static final String DESCRIPTION_APP_TAG = "Description";
    public static final String PASOS_APP_TAG = "Pasos";
    public static final String ALERGIAS_APP_TAG = "Alergias";
    public static final String INGREDIENTES_APP_TAG = "Ingredientes";
    private String idUser;
    private String nombre;
    private String Description;
    private ArrayList<String> Pasos;
    private IngredientesList ingredientes;
    private ArrayList<String> alergias;
    private String PictureURL;
    private int likes;
    private boolean likeFromUser;
    private String userPhoto; // Foto del usuario

    public Recipe(){
        this.alergias = new ArrayList<>();
        this.Pasos = new ArrayList<>();
    } //Para la base de Datos se necesita un constructor sin argumentos...
    //Constructor
    public Recipe(  final String nombre,
                    final String description,
                    final IngredientesList ingredientes,
                    final String pictureURL,
                    final int likes,
                    final ArrayList<String> pasos,
                    final ArrayList<String> alergias
    ){
        this.nombre = nombre;
        this.Description = description;
        this.PictureURL = pictureURL;
        this.likes = likes;
        this.ingredientes = ingredientes;
        this.Pasos = pasos;
        this.alergias = alergias;
    }

    public void setLikeFromUser(boolean like){
        this.likeFromUser = like;
    }
    public boolean getLikeFromUser(){
        return likeFromUser;
    }

    public Recipe(final String nombre,
                  final IngredientesList ingredientes,
                  final String pictureURL,
                  final String Description,
                  final  int likes,
                  final ArrayList<String> pasos) {

        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.PictureURL = pictureURL;
        this.Description = Description;
        this.likes = likes;
        this.Pasos = pasos;
        this.alergias = new ArrayList<>();
    }

    //Getters

    //------------..-GETTERS-----------------------------
    public String getNombre() { return nombre;}
    public ArrayList<String> getPasos() { return Pasos; }
    public IngredientesList getIngredientes() { return ingredientes; }
    public String getPictureURL() {  return PictureURL; }
    public int getLikes() { return likes; }
    public String getIdUser() { return idUser; }
    public ArrayList<String> getAlergias() { return this.alergias; }
    public String getDescription(){ return Description; }
    public String getUserPhoto(){ return userPhoto; }

    //---------------SETTERS----------------------------------------
    public void setNombre(String nombre) { this.nombre = nombre;  }
    public void setPasos(ArrayList<String> pasos) { this.Pasos = pasos; }
    public void setIngredientes(IngredientesList ingredientes){ this.ingredientes = ingredientes; }
    public void setPictureURL(String pictureURL) { this.PictureURL = pictureURL; }
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

    public void setAlergias(@NonNull final List<String> _alergias) {
        this.alergias = (ArrayList<String>) _alergias;
    }
}
