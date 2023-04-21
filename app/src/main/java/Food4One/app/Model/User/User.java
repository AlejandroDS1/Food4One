package Food4One.app.Model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User implements Serializable {

    public final static String TAG = "User";
    private static User user;
    public String userName;

    private String descripcion;
    private String email;
    private String profilePictureURL;
    private ArrayList<String> idRecetas;
    private ArrayList<String> alergias;

    // ATRIBUTOS OPCIONALES *******
    // PREMIUM BOOLEAN IDEA PARA EL FINAL
    // LISTAS (compra)

    public User(){}
    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
        this.alergias = null; // Dejamos alegias a null
    }

    public User(String userName, String email, ArrayList<String> idRecetas) {
        this.userName = userName;
        this.email = email;
        this.idRecetas = idRecetas;
    }


    public static User getInstance(String userName, String email){
        if (user == null) user = new User(userName, email);
        return user;
    }
    public static User getInstance(){
        if (user == null) user = new User();
        return user;
    }

    public void addIdReceta(String idreceta){
        this.idRecetas.add(idreceta);
    }

    public ArrayList<String> getIdRecetas(){return idRecetas; }

    //Getters

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
