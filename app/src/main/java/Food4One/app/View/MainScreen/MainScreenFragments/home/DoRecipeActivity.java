package Food4One.app.View.MainScreen.MainScreenFragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Food4One.app.Model.Recipe.Ingredients.Ingrediente;
import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.R;
import Food4One.app.View.MainScreen.MainScreenFragments.Coleccion.ShoppingListViewModel;
import Food4One.app.databinding.ActivityDoRecipeBinding;


public class DoRecipeActivity extends AppCompatActivity {

    ActivityDoRecipeBinding binding;
    private HomeViewModel homeViewModel;
    private Recipe recipeToMake;
    private IngredienteAdapter ingredientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();


        homeViewModel = HomeViewModel.getInstance();

        recipeToMake = homeViewModel.getDoRecipe().getValue();
        cargarDatosRecipeToMake();
        initListeners();
    }

    private void initListeners() {
        binding.addIngredientsToListDoRecipe.setOnClickListener(clickListener -> {
            ShoppingListViewModel.getInstance().addIngredientesList_toDDBB(ingredientsAdapter.selectedIngredientes);
        });

        binding.removeDB.setOnClickListener(clickListener -> {
            Toast.makeText(getApplicationContext(), "Not immplemented yet", Toast.LENGTH_SHORT).show();
        });
    }


    private void cargarDatosRecipeToMake() {
        //Se cargan los ingredientes y los pasos de la receta que se ha seleccionado antes

        // Inicio de los componentes para la lista de ingredientes
        ingredientsAdapter = new IngredienteAdapter(recipeToMake.getIngredientes().toArrayStringId());

        binding.ingredientsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        binding.ingredientsList.setAdapter(ingredientsAdapter);

        // Inicio de los componentes de la lista de pasos
        binding.pasosList.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.item_step_do_recipe, recipeToMake.getPasos()));

        //Imagen, Descripción y Nombre de la Receta
        binding.descriptionRecipeToDo.setText(recipeToMake.getDescription());

        // Configuracion de la foto de la receta
        ImageView recipePhoto = binding.recipeToDoImage;

        Picasso.get().load(recipeToMake.getPictureURL()).resize(790, 600).centerCrop().into(recipePhoto);

        binding.recetToDoName.setText(recipeToMake.getNombre().split("@")[0]);
    }
    private static class IngredienteAdapter extends RecyclerView.Adapter<IngredienteAdapter.ViewHolder>{

        private final List<String> ingredientes;
        private final List<String> selectedIngredientes;

        public IngredienteAdapter(List<String> ingredientes){
            this.ingredientes = ingredientes;
            this.selectedIngredientes = new ArrayList<>();
        }

        @NonNull
        @Override
        public IngredienteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View view = inflater.inflate(R.layout.item_ingredientes_do_recipe, parent, false);

            return new IngredienteAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull IngredienteAdapter.ViewHolder holder, int position) {
            holder.onBind(this.ingredientes.get(position));
        }

        @Override
        public int getItemCount() {
            return ingredientes.size();
        }

        private final class ViewHolder extends RecyclerView.ViewHolder {

            private final CheckBox checkBox;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.mIngrediente_doRecipe);
            }

            public void onBind(final String ingrediente){
                checkBox.setText(Ingrediente.Id_toString(ingrediente));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        if(checked)
                            selectedIngredientes.add(ingrediente);
                        else
                            selectedIngredientes.remove(ingrediente);
                    }
                });
            }
        }
    }


}