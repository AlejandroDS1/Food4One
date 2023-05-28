package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;
import Food4One.app.Model.Recipe.Recipe.RecipesUserApp;
import Food4One.app.Model.User.UserRepository;
import Food4One.app.View.MainScreen.MainScreenFragments.home.DoRecipeActivity;
import Food4One.app.View.MainScreen.MainScreenFragments.home.HomeViewModel;
import Food4One.app.databinding.FragmentSavedBinding;

public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;
    private ColeccionViewModel coleccionViewModel;
    private RecyclerViewAdapter mRecipeCardAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        coleccionViewModel = ColeccionViewModel.getInstance();

        binding = FragmentSavedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setViewVisibility();

        cargarRecycleView();
        observerObjectsView();

        return root;
    }



    private void observerObjectsView() {
        // Observer a coleccionFragment per veure si la llista de receptes (observable MutableLiveData)
        // a coleccionViewModel ha canviat.
        final Observer<ArrayList<Recipe>> observerRecipes = new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> users) {
                if(! users.isEmpty())
                    mRecipeCardAdapter.notifyDataSetChanged();
            }
        };
        coleccionViewModel.getmRecipes().observe(this.getViewLifecycleOwner(), observerRecipes);

        RecipeRepository.getInstance().addOnLoadRecetaCollectionListener(new RecipeRepository.OnLoadRecipeCollection() {
            @Override
            public void onLoadRecipeCollection(ArrayList<Recipe> recetas) {
                coleccionViewModel.setRecipes(recetas);

                setViewVisibility();
            }
        });

    }

    private void setViewVisibility(){
        final ArrayList<Recipe> recetas = coleccionViewModel.getmRecipes().getValue();
        if (recetas.isEmpty())
            binding.emptymessage.setVisibility(View.VISIBLE);
        else
            binding.emptymessage.setVisibility(View.GONE);
    }

    private void cargarRecycleView() {
        binding.recipeRV.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        mRecipeCardAdapter = new RecyclerViewAdapter(coleccionViewModel.getmRecipes().getValue());
        mRecipeCardAdapter.setRecipeToMakeListener(new RecipeRepository.OnLoadRecipeToMake() {
            @Override
            public void OnLoadRecipe(Recipe recipeToDo) {
                HomeViewModel.getInstance().loadRecipeToMake(recipeToDo);
                startActivity(new Intent(getContext(), DoRecipeActivity.class));
                setViewVisibility();
            }
        });
        binding.recipeRV.setAdapter(mRecipeCardAdapter);
    }

}