package Food4One.app.View.MainScreen.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import Food4One.app.Model.Recipie.Recipie.Recipe;
import Food4One.app.Model.Recipie.Recipie.RecipeRepository;
import Food4One.app.Model.Recipie.Recipie.RecipesUserApp;
import Food4One.app.View.MainScreen.ui.Perfil.PerfilViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<ArrayList<Recipe>> mRecetasApp;

    private RecipeRepository mRecetasRepository;

    private FirebaseStorage mStorage;


    private static HomeViewModel perfilViewModel;

    public static HomeViewModel getInstance(){
        if (perfilViewModel == null) perfilViewModel = new HomeViewModel();
        return perfilViewModel;
    }

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        mRecetasApp = new MutableLiveData<>();
        mRecetasRepository = RecipeRepository.getInstance();
    }


    private void recetasPictureListeners() {
        //Al cargar las recetas el observador será notificado y se acutalizará la lista
        mRecetasRepository.addOnLoadRecetaListener(new RecipeRepository.OnLoadRecetaListener() {
            @Override
            public void onLoadRecetas(ArrayList<Recipe> recetas) {
                HomeViewModel.this.setRecetes(recetas);
            }
        });
    }
    public void loadRecetasApp(String selection){
        mRecetasRepository.loadRecipesApp(mRecetasApp.getValue(), selection);
    }

    public void setRecetes(ArrayList<Recipe> recetas) {
        mRecetasApp.setValue(recetas);
    }

    public LiveData<String> getText() {
        return mText;
    }
}