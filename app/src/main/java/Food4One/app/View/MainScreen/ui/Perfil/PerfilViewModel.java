package Food4One.app.View.MainScreen.ui.Perfil;
/*
import androidx.lifecycle.ViewModel;

public class PerfilViewModel extends ViewModel {
    // TODO: Implement the ViewModel
}
*/


import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import Food4One.app.View.Authentification.LoginActivity;
import Food4One.app.databinding.FragmentPerfilBinding;

public class PerfilViewModel extends ViewModel {

    private final String TAG = "FotosmeActivityViewModel";

    private FirebaseStorage mstore;
    private final MutableLiveData<String> mText;

    public PerfilViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fra1gment");
        mstore = FirebaseStorage.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void logoutButton(FragmentPerfilBinding binding){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        binding.getRoot().getContext().startActivity(new Intent(binding.getRoot().getContext(), LoginActivity.class));
    }

    public void editarButton(FragmentPerfilBinding binding){
        Intent editarperfil = new Intent(binding.getRoot().getContext(), UserSettingsActivity.class);
        binding.getRoot().getContext().startActivity(editarperfil);
    }

}