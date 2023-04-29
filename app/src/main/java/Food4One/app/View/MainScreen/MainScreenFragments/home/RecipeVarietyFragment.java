package Food4One.app.View.MainScreen.MainScreenFragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.databinding.FragmentRecipeVarietyBinding;

public class RecipeVarietyFragment extends Fragment {
    private FragmentRecipeVarietyBinding binding;
    private RecyclerView recyclerViewTypes;
    private HomeViewModel homeViewModel;
    private RecipeTypeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=  FragmentRecipeVarietyBinding.inflate(inflater, container, false);
        homeViewModel = HomeViewModel.getInstance();
        String selection = requireArguments().getString("HomeSelection");
        homeViewModel.loadRecetasApp(selection);

        //Le ponemos al Título del Fragment el Nombre que corresponde
        binding.titleTypes.setText("Sección de "+ selection);

        instanciaRecycleView();
        observerAdapterToChange();

        binding.backButtonTypeFragment.setOnClickListener(view-> {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        });

        return binding.getRoot();

    }

    private void observerAdapterToChange() {
        // Observer a Perfil per veure si la llista de Receta (observable MutableLiveData)
        // a PerfilViewModel ha canviat.
        final Observer<ArrayList<Recipe>> observerRecetes= new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recetas) {
                adapter.notifyDataSetChanged();
            }
        };
        homeViewModel.getRecetes().observe(this.getViewLifecycleOwner(), observerRecetes);
    }

    private void instanciaRecycleView() {

        recyclerViewTypes = binding.recycleViewVariety;
        //Ahora le definimos un Manager Linear Vertical
        LinearLayoutManager manager = new LinearLayoutManager(
                this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewTypes.setLayoutManager(manager);

        //Luego instanciamos el Adapter de los tipos de recetas
        adapter = new RecipeTypeAdapter(homeViewModel.getRecetes().getValue());

        adapter.setmOnClickListenerHomeSelection(new RecipeTypeAdapter.OnClickListenerTypeSelection() {
            @Override
            public void onClickTypeSelection(Recipe recipe) {
                homeViewModel.loadRecipeToMake(recipe);
                startActivity(new Intent(getContext(), DoRecipeActivity.class));
            }
        });

        recyclerViewTypes.setAdapter(adapter);

    }
}