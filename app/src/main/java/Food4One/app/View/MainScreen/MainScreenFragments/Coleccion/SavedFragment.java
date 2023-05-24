package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;
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

        coleccionViewModel =
                new ViewModelProvider(this).get(ColeccionViewModel.class);

        binding = FragmentSavedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cargarReceptesUsuari();
        binding.emptymessage.setVisibility(View.GONE);

        cargarRecycleView();
        observerObjectsView();

        return root;
    }


    private void cargarReceptesUsuari() {
        // A partir d'aquí, en cas que es faci cap canvi a la llista de receptes, ColeccionFragment ho sabrà

       // if(RecipeList.getInstance().size() == 0) //Si aún no se cargaron las recetas del usuario
        // Internament pobla les receptes de la BBDD
        coleccionViewModel.loadRecetasOfUserFromRepository(new ArrayList<>(UserRepository.getUser().getIdCollections().keySet()));
    }

    private void observerObjectsView() {
        // Observer a coleccionFragment per veure si la llista de receptes (observable MutableLiveData)
        // a coleccionViewModel ha canviat.
        final Observer<ArrayList<Recipe>> observerRecipes = new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> users) {
                mRecipeCardAdapter.notifyDataSetChanged();
            }
        };

        coleccionViewModel.getmRecipes().observe(this.getViewLifecycleOwner(), observerRecipes);

        final Observer<String> observerEmptys = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("VISIBLE"))
                    binding.emptymessage.setVisibility(View.VISIBLE);
                else if (s.equals("GONE"))
                    binding.emptymessage.setVisibility(View.GONE);
            }
        };
        coleccionViewModel.getEmptySignal().observe(this.getViewLifecycleOwner(), observerEmptys);
    }

    private void cargarRecycleView() {
        binding.recipeRV.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        mRecipeCardAdapter = new RecyclerViewAdapter(coleccionViewModel.getmRecipes().getValue());
        mRecipeCardAdapter.setRecipeToMakeListener(new RecipeRepository.OnLoadRecipeToMake() {
            @Override
            public void OnLoadRecipe(Recipe recipeToDo) {
                HomeViewModel.getInstance().loadRecipeToMake(recipeToDo);
                startActivity(new Intent(getContext(), DoRecipeActivity.class));
            }
        });
        binding.recipeRV.setAdapter(mRecipeCardAdapter);
    }
}