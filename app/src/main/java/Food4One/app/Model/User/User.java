package Food4One.app.Model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class User implements Serializable {

    // TAGS para acceder a los atributos de la base de datos.
    public final static String TAG = "Users";
    public final static String NAME_TAG = "Name";
    public final static String ALERGIAS_TAG = "Alergias";
    public final static String DESCRIPCION_TAG = "Description";
    public final static String PICTUREURL_TAG = "PictureURL";
    public final static String IDCOLLECTIONS_TAG = "idCollections";
    public final static String LIKESRECIPES_TAG= "likesRecipe";
    public final static String IDRECETAS_TAG = "idRecetas";
    public final static String IDINGREDIENTES_LIST_TAG = "IngredientesList";

    private static User user;
    public String userName;
    private String descripcion;
    private String email;
    private String profilePictureURL;
    private ArrayList<String> idRecetas;
    private ArrayList<String> alergias;
    private HashMap<String, Boolean>  idCollections;
    private HashMap<String, Boolean> likesRecipes;

    public User(){}
    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
        this.alergias = new ArrayList<>(); // Dejamos alegias a null
        this.idCollections = new HashMap<>();
        this.likesRecipes = new HashMap<>();
    }

    public User(String userName, String email, ArrayList<String> idRecetas) {
        this.userName = userName;
        this.email = email;
        this.idRecetas = idRecetas;
        this.alergias = new ArrayList<>(); // Dejamos alegias a null
        this.idCollections = new HashMap<>();
        this.likesRecipes = new HashMap<>();
    }

    public void addIdReceta(String idreceta){
        this.idRecetas.add(idreceta);
    }

    public ArrayList<String> getIdRecetas(){ return idRecetas; }
    //Getters

    public String getUserName() {
        return userName;
    }

    public String getEmail() { return email; }
    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public ArrayList<String> getAlergias() {
        return alergias;
    }

    // Setters

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public void setIdRecetas(ArrayList<String> idRecetas) {
        this.idRecetas = idRecetas;
    }

    public void setAlergias(ArrayList<String> alergias) {
        this.alergias = alergias;
    }

    /**
     * Retorna un array de booleanos con true donde se cumpla que se tiene esa alergia.
     * El resultado tiene el orden del array alergias introducido
     * @param alergias String[] que contiene las alergias en un orden.
     * @return boolean[]
     */
    public boolean[] getBooleanArrayAlergias(String[] alergias){

        boolean arrayBool[] = new boolean[alergias.length];
        if(this.alergias==null) return arrayBool;

        if (this.alergias.isEmpty()) return new boolean[alergias.length];

        List<String> _alergias = Arrays.asList(alergias);

        // Recorremos el array, cuando son iguales marcamos como true la casilla.
        int pos;
        for (String alergia : this.alergias)
            if ((pos = _alergias.indexOf(alergia)) != -1)
                arrayBool[pos] = true;

        return arrayBool;
    }

    //Getter
    public HashMap<String, Boolean>  getIdCollections() {
        return idCollections;
    }
    public HashMap<String, Boolean> getLikesRecipes() {
        return likesRecipes;
    }
    public String getDescripcion() {
        return descripcion;
    }

    // Setter
    public void setIdCollections(HashMap<String, Boolean> idCollections) {
        this.idCollections = idCollections;
    }
    public void setLikesRecipes(HashMap<String, Boolean> likesRecipes) {
        this.likesRecipes = likesRecipes;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}