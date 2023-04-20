package Food4One.app.View.MainScreen.ui.Coleccion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import Food4One.app.Model.Recipie.Recipie.Recipe;
import Food4One.app.Model.Recipie.Recipie.RecipeListAdapter;

public class ColeccionViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Recipe>> mText;
    private RecipeListAdapter mRecipeAdapter;

    private FirebaseStorage mStorage;

    public ColeccionViewModel() {
        mText = new MutableLiveData<>(new ArrayList<>());
        mStorage = FirebaseStorage.getInstance();
        mRecipeAdapter = RecipeListAdapter.getInstance();

        mRecipeAdapter.addOnLoadRecipesListener(new RecipeListAdapter.OnLoadRecipesListener() {
            @Override
            public void OnLoadRecipes(ArrayList<Recipe> recipes) {
                ColeccionViewModel.this.setRecipes(recipes);
            }
        });
    }

    public void loadRecipesFromRepository() {
         mRecipeAdapter.loadRecipes(mText.getValue());
    }

    private void setRecipes(ArrayList<Recipe> recipes) {
        mText.setValue(recipes);
    }

    public LiveData<ArrayList<Recipe>> getText() {
        return mText;
    }


}