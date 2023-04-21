package Food4One.app.Model.User;

import java.io.Serializable;
import java.util.ArrayList;

import Food4One.app.Model.Alergias;

public class User implements Serializable {

    public final static String TAG = "User";
    public String userName;
    private String email;

    private String profilePictureURL;
    private ArrayList<String> idRecetas;

    private ArrayList<Alergias> alergias;

    // ATRIBUTOS OPCIONALES *******
    // PREMIUM BOOLEAN IDEA PARA EL FINAL
    // LISTAS (compra)


    private static User user;

    public static User getInstance(){
        if(user== null){
            user = new User();
        }
        return user;
    }

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

    public ArrayList<Alergias> getAlergias() {
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

    public void setAlergias(ArrayList<Alergias> alergias) {
        this.alergias = alergias;
    }
}
