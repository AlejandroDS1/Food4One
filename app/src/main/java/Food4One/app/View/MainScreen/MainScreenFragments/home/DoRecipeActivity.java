package Food4One.app.View.MainScreen.MainScreenFragments.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
    private IngredienteAdapter ingredientsAdapter;
    private HomeViewModel homeViewModel;
    private StepsAdapter stepsAdapter;
    ActivityDoRecipeBinding binding;
    private CardView addToListView;
    private Recipe recipeToMake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        homeViewModel = HomeViewModel.getInstance();

        recipeToMake = homeViewModel.getDoRecipe().getValue();
        addToListView = binding.addToList;
        addToListView.setVisibility(View.GONE);

        cargarDatosRecipeToMake();
        initListeners();
    }

    private void initListeners() {

        binding.addIngredientePlus.setOnClickListener( v->{
            //No podemos ver la carta, así que la ponemos visible y animamos para su aparición
            if(addToListView.getVisibility()== View.GONE) {
                addToListView.setVisibility(View.VISIBLE);
                cargarAddToLis(R.anim.recycle_view_right_fadein);
                binding.addIngredientePlus.setText("  -  ");
            }else {
                //La carta es visible, la movemos y esperamos la animación para quita la visibilidad
                cargarAddToLis(R.anim.recycle_view_right_fideout);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addToListView.setVisibility(View.GONE);
                        binding.addIngredientePlus.setText("  +  ");
                    }
                }, 600);
            }
        });

        binding.addToList.setOnClickListener(v ->{
            addIngredientesToListCreator();
        });
    }

    void cargarAddToLis(int idAnim){
        Animation anim = AnimationUtils.loadAnimation(this,idAnim);
        anim.setDuration(500);
        addToListView.setAnimation(anim);
    }

    private void addIngredientesToListCreator(){

        AlertDialog.Builder builder = new AlertDialog.Builder(DoRecipeActivity.this);

        View dialog = View.inflate(getApplicationContext(), R.layout.alert_dialog_add_ingerdientes_tolist, null);

        builder.setView(dialog);
        final ListView listNames = dialog.findViewById(R.id.listNames_addIngredientes_listview);

        final EditText listaName = dialog.findViewById(R.id.texto_listaName_alertdialog);

        final List<String> userListNames = ShoppingListViewModel.getInstance().getAllListsNames();

        listNames.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_ingredientes_list_layout, userListNames));

        ShoppingListViewModel.getInstance().setOnChangedListsListener(new ShoppingListViewModel.OnChangedListsListener() {
            @Override
            public void onChangedListListener() {
                userListNames.clear();
                userListNames.addAll(ShoppingListViewModel.getInstance().getAllListsNames());
                ((ArrayAdapter)listNames.getAdapter()).notifyDataSetChanged();
            }
        });
        // Declaracion de listeners para las diferentes acciones de la view
        listNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listaName.setText(userListNames.get(i)); // El texto del edit text sera el de la lista
            }
        });

        AlertDialog alert = builder.create();

        // Boton para guardar en base de datos
        dialog.findViewById(R.id.BtnGuardar_addToList).setOnClickListener(guardarDDBB -> {

            // Creamos un nuevo hilo para subir las recetas, de esta manera si la conexion es lenta da la ilusion de que va mas rapido
            new Thread(new Runnable(){
                @Override
                public void run() {
                    ShoppingListViewModel.getInstance().addIngredientesList_toDDBB(ingredientsAdapter.selectedIngredientes, listaName.getText().toString());
                }
            }).start();
            alert.dismiss();
        });

        dialog.findViewById(R.id.BtnCancelar_addToList).setOnClickListener(cancelar -> { alert.dismiss(); });

        alert.show();
    }

    private void cargarDatosRecipeToMake() {
        //Se cargan los ingredientes y los pasos de la receta que se ha seleccionado antes

        // Inicio de los componentes para la lista de ingredientes
        ingredientsAdapter = new IngredienteAdapter(recipeToMake.getIngredientes().toArrayStringId());
        // Inicio de los componentes de la lista de pasos
        stepsAdapter = new StepsAdapter(recipeToMake.getPasos());

        binding.ingredientsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.pasosList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        binding.ingredientsList.setAdapter(ingredientsAdapter);
        binding.pasosList.setAdapter(stepsAdapter);

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
            private final TextView cantidad;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.ingredienteName_doRecipe);
                cantidad = itemView.findViewById(R.id.cantidadymagnitud_doRecipe);
            }

            public void onBind(final String ingrediente){

                // Creamos un objeto ingrediente que decodifica el ingrediente por la id
                final Ingrediente _ingrediente = new Ingrediente(ingrediente);

                checkBox.setText(_ingrediente.getName()); // Ponemos el nombre del ingrediente en el checkbox
                cantidad.setText(_ingrediente.getCantidadStr());

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

    private static class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder>{
        ArrayList<String> pasos;
        public StepsAdapter(ArrayList<String> pasos) {
            this.pasos = pasos;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_steps_list_new_recipe, parent, false);
            return new StepsAdapter.ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.onBind(this.pasos.get(position), position);
        }
        @Override
        public int getItemCount() {
            return pasos.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder{
            TextView pasoString, numSteps;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                pasoString = itemView.findViewById(R.id.stepTxt_newRecipe);
                numSteps = itemView.findViewById(R.id.numStep_newRecipe);
            }
            public void onBind(String step, Integer position){
                pasoString.setText(step);
                numSteps.setText(Integer.toString(position+1) );
            }
        }
    }
}