package Food4One.app.View.MainScreen.MainScreenFragments.NewRecipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Ingredients.Ingrediente;
import Food4One.app.R;
import Food4One.app.databinding.FragmentNewRecipeBinding;

public class NewRecipeFragment extends Fragment {
    private FragmentNewRecipeBinding binding;
    private NewRecipeViewModel newRecipeViewModel;
    private IngredientesListAdapter ingredientesListAdapter;
    private RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        newRecipeViewModel = new ViewModelProvider(this).get(NewRecipeViewModel.class);

        binding = FragmentNewRecipeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // TODO: ELIMINAR BOTON CUANDO ACABE LAS PRUEBAS
        binding.botonPRUEBA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRecipeViewModel.addIngrediente(new Ingrediente("PRUEBA DE TEXTO|200|D|1"));
                ingredientesListAdapter.notifyDataSetChanged();
            }
        });

        initLists();
        initListeners();
        initPrueba();

        return root;
    }



    void initPrueba(){
        newRecipeViewModel.addIngrediente(new Ingrediente("PRUEBA|12|Gr|1"));
        newRecipeViewModel.addIngrediente(new Ingrediente("PRUEBA|12|Gr|1"));
        newRecipeViewModel.addIngrediente(new Ingrediente("PRUEBA|12|Gr|1"));
    }

    private void initLists(){

        ingredientesListAdapter = new IngredientesListAdapter(newRecipeViewModel.getIngredientesList().getValue());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());

        binding.ingredienteListNewRecipe.setLayoutManager(manager);
        binding.ingredienteListNewRecipe.setAdapter(ingredientesListAdapter);

        ingredientesListAdapter.setOnTrashCanClickListener(new IngredientesListAdapter.OnTrashCanClickListener() {
            @Override
            public void onTrashCanClickListener(int position) {
                ingredientesListAdapter.deleteRow(position);
            }
        });


        // Iniciamos el spinner con sus opciones

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Magnitudes, android.R.layout.simple_spinner_item);
        binding.spinnerMagnitudNewRecipe.setAdapter(spinnerAdapter);


    }
    private void initListeners(){

        // Listener para añadir imagen

        // Listener para publicar la receta.

        // Listener para añadir ingrediente
//        binding.addIngredienteBtnNewRecipe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }
}