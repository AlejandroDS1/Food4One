package Food4One.app.View.MainScreen.ui.Coleccion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import Food4One.app.Model.Recipie.Recipie.Recipe;
import Food4One.app.databinding.FragmentColeccionBinding;

public class ColeccionFragment extends Fragment {

    private FragmentColeccionBinding binding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private RecyclerViewAdapter mRecipeCardAdapter;

    private ShoppingListFragment shoppingListFragment;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ColeccionViewModel coleccionViewModel =
                new ViewModelProvider(this).get(ColeccionViewModel.class);

        binding = FragmentColeccionBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        binding.recipeRV.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        mRecipeCardAdapter = new RecyclerViewAdapter(coleccionViewModel.getText().getValue());

        binding.recipeRV.setAdapter(mRecipeCardAdapter);

        // Observer a coleccionFragment per veure si la llista de receptes (observable MutableLiveData)
        // a coleccionViewModel ha canviat.
        final Observer<ArrayList<Recipe>> observerRecipes = new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> users) {
                mRecipeCardAdapter.notifyDataSetChanged();
            }
        };

        coleccionViewModel.getText().observe(this.getViewLifecycleOwner(), observerRecipes);
                //getUsers().observe(this, observerRecipes);

        // A partir d'aquí, en cas que es faci cap canvi a la llista d'usuaris, ColeccionFragment ho sabrá
        coleccionViewModel.loadRecipesFromRepository();  // Internament pobla les receptes de la BBDD
        binding.BtnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Nova instància del fragment a iniciar
                shoppingListFragment = new ShoppingListFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(binding.childFragmentContainer.getId(), shoppingListFragment);
                transaction.addToBackStack(null); //Permet tirar enrere
                transaction.commit();
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}