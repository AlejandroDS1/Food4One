package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipesUserApp;
import Food4One.app.Model.User.User;
import Food4One.app.R;
import Food4One.app.databinding.FragmentColeccionBinding;

public class ColeccionFragment extends Fragment {

    private FragmentColeccionBinding binding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private RecyclerViewAdapter mRecipeCardAdapter;
    private ColeccionViewModel coleccionViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        coleccionViewModel =
                new ViewModelProvider(this).get(ColeccionViewModel.class);

        binding = FragmentColeccionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cargarReceptesUsuari();

        clickListenerObjectsView();

        cargarRecycleView();
        observerObjectsView();

        return root;
    }

    private void cargarReceptesUsuari() {
        // A partir d'aquí, en cas que es faci cap canvi a la llista de receptes, ColeccionFragment ho sabrá
        if(RecipesUserApp.getInstance().size() == 0) //Si aún no se cargaron las recetas del usuario
            coleccionViewModel.loadRecetasOfUserFromRepository(User.getInstance().getIdRecetas()); // Internament pobla les receptes de la BBDD
    }

    private void clickListenerObjectsView() {

        binding.BtnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.BtnSaved.setElevation(0);
                Drawable myDrawable1 = ContextCompat.getDrawable(getContext(), R.drawable.greybutton);
                binding.BtnSaved.setBackground(myDrawable1);

                binding.BtnList.setElevation(15);
                Drawable myDrawable2 = ContextCompat.getDrawable(getContext(), R.drawable.botonback);
                binding.BtnList.setBackground(myDrawable2);

                //Nova instància del fragment a iniciar
                FragmentManager fM = getActivity().getSupportFragmentManager();
                FragmentTransaction fT = fM.beginTransaction();
                fT.setReorderingAllowed(true).addToBackStack("ShoppingListFragment"); //Permet tirar enrere
                fT.replace(R.id.coleccionFragment, new ShoppingListFragment());
                fT.commit();
            }
        });
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
    }

    private void cargarRecycleView() {
        binding.recipeRV.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        mRecipeCardAdapter = new RecyclerViewAdapter(coleccionViewModel.getmRecipes().getValue());
        binding.recipeRV.setAdapter(mRecipeCardAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}