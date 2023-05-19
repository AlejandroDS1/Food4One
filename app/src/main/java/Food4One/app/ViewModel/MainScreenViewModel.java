package Food4One.app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import Food4One.app.Model.User.User;

/**
 * Esta clase esta pensada para ser ViewModel tanto del MainScreen como de todos fragmentos que soporta
 * de tal manera que los datos se conservaran desde este propio ViewModel
 */
public class MainScreenViewModel extends ViewModel{
    private MutableLiveData<User> user;
    private MutableLiveData<ArrayList<User>> allUsers;

    public MainScreenViewModel(){

        this.user = new MutableLiveData<>();
        this.allUsers = new MutableLiveData<>();

    }

    public MainScreenViewModel(User user) {
        this.user = new MutableLiveData<>();
        this.user.setValue(user);

        // Lista de todos los usuarios
        this.allUsers = new MutableLiveData<>();
        this.allUsers.setValue(new ArrayList<User>());

    }


    //Getters
    public LiveData<User> getUser(){
        return this.user;
    }

    public LiveData<ArrayList<User>> getAllUsers() {
        return allUsers;
    }

    // Setters
    public void setUser(User user){
        this.user.setValue(user);
    }
    public void setUser(MutableLiveData<User> user) {
        this.user = user;
    }

    public void setAllUsers(MutableLiveData<ArrayList<User>> allUsers) {
        this.allUsers = allUsers;
    }

}