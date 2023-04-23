package Food4One.app.Model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User implements Serializable {

    // TAGS para acceder a los atributos de la base de datos.
    public final static String TAG = "Users";
    public final static String NAME_TAG = "Name";
    public final static String ALERGIAS_TAG = "Alergias";
    public final static String DESCRIPCION_TAG = "Description";
    public final static String PICTUREURL_TAG = "PictureURL";
    public final static String IDCOLLECTIONS_TAG = "idCollections";
    public final static String IDRECETAS_TAG = "idRecetas";

    private static User user;
    public String userName;
    private String descripcion;
    private String email;
    private String profilePictureURL;
    private ArrayList<String> idRecetas;
    private ArrayList<String> alergias;
    private ArrayList<String> idCollections;

    // ATRIBUTOS OPCIONALES *******
    // PREMIUM BOOLEAN IDEA PARA EL FINAL
    // LISTAS (compra)

    public User(){}
    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
        this.alergias = new ArrayList<>(); // Dejamos alegias a null
        this.idCollections = new ArrayList<>();
    }

    public User(String userName, String email, ArrayList<String> idRecetas) {
        this.userName = userName;
        this.email = email;
        this.idRecetas = idRecetas;
        this.alergias = new ArrayList<>(); // Dejamos alegias a null
        this.idCollections = new ArrayList<>();
    }


    public static User getInstance(String userName, String email){
        if (user == null) user = new User(userName, email);
        return user;
    }
    public static User getInstance(){
        if (user == null) user = new User();
        return user;
    }

    // Este metodo es utiliza para vaciar la clase si cerramos sesion.
    public static void logOutUser(){ user = null; }

    public void addIdReceta(String idreceta){
        this.idRecetas.add(idreceta);
    }

    public ArrayList<String> getIdRecetas(){return idRecetas; }
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

        if (this.alergias.isEmpty()) return new boolean[alergias.length];

        List<String> _alergias = Arrays.asList(alergias);

        boolean arrayBool[] = new boolean[alergias.length];

        for (String alergia : this.alergias)
            arrayBool[_alergias.indexOf(alergia)] = true;

        return arrayBool;
    }

    //Getter
    public ArrayList<String> getIdCollections() {
        return idCollections;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Setter
    public void setIdCollections(ArrayList<String> idCollections) {
        this.idCollections = idCollections;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}