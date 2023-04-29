package Food4One.app.View.MainScreen.MainScreenFragments.NewRecipe;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import Food4One.app.Model.Recipe.Ingredients.Ingrediente;
import Food4One.app.R;
import Food4One.app.databinding.FragmentNewRecipeBinding;

public class NewRecipeFragment extends Fragment {
    private FragmentNewRecipeBinding binding;
    private NewRecipeViewModel newRecipeViewModel;
    private IngredientesListAdapter ingredientesListAdapter;
    private MPopUpWindow _popUpWindow;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        newRecipeViewModel = new ViewModelProvider(this).get(NewRecipeViewModel.class);

        binding = FragmentNewRecipeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initLists();
        initListeners();

        return root;
    }

    private void initLists(){

        ingredientesListAdapter = new IngredientesListAdapter(newRecipeViewModel.getIngredientesList().getValue());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());

        binding.ingredienteListNewRecipe.setLayoutManager(manager);
        binding.ingredienteListNewRecipe.setAdapter(ingredientesListAdapter);

        // Iniciamos la lista de Alergias
        List<String> alergias = new ArrayList<>();

        alergias.addAll(Arrays.asList(getResources().getStringArray(R.array.Alergias)));

        //Ahora le definimos un Manager Grid

        AlergiasListAdapter alergiasAdapter = new AlergiasListAdapter(alergias);

        GridLayoutManager managerAlergias = new GridLayoutManager(
                this.getContext(), GridLayoutManager.VERTICAL);
        managerAlergias.setSpanCount(2); //Number of columns.

        binding.alergiasListNewRecipe.setAdapter(alergiasAdapter);
        binding.alergiasListNewRecipe.setLayoutManager(managerAlergias);


        // Ahora vamos a crear el listener para nuestro PopUpWindow personalizado.
        _popUpWindow = new MPopUpWindow(getContext(), binding.spinnerMagnitudNewRecipe); // Creamos el objeto PopUpWindow

        binding.spinnerMagnitudNewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListView listViewSpinner = new ListView(getContext());
                final List<String> lista = Arrays.asList(getResources().getStringArray(R.array.MagnitudesLong));

                listViewSpinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_ingredientes_list_layout, lista));

                listViewSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        _popUpWindow.dismiss();
                        binding.spinnerMagnitudNewRecipe.setText(getResources().getStringArray(R.array.Magnitudes)[i]);
                    }
                });

                _popUpWindow.setConfiguration(view, listViewSpinner);
            }
        });

    }

    private void initListeners(){

        // Listener para añadir imagen

        binding.imagenRecetaNewRecpie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Aqui crearemos un alertDialog que nos dara a elegir entre
                // Subir la foto desde galeria o desde la camara.

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                final View _view = LayoutInflater.from(getContext()).inflate(R.layout.addpicture_customdialog_new_recipe, null);

                _view.findViewById(R.id.dialog_camera_newRecipe).setOnClickListener(v -> cameraImage());

                _view.findViewById(R.id.dialog_galeria_newRecipe).setOnClickListener(v -> selectGaleria() );

                builder.setView(_view);

                AlertDialog alert = builder.create(); alert.show(); // Mostramoos el alertDialog
            }

            // TODO: Acabar metodos para consguir imagenes.
            private void cameraImage(){

                Toast.makeText(getContext(), "Elegido Camara", Toast.LENGTH_SHORT).show();
            }

            private void selectGaleria(){
                Toast.makeText(getContext(), "Elegido Galeria", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener para publicar la receta.

        // Listener para la eliminar un objeto de la lista de Ingredientes
        ingredientesListAdapter.setOnTrashCanClickListener(new IngredientesListAdapter.OnTrashCanClickListener() {
            @Override
            public void onTrashCanClickListener(int position) {
                ingredientesListAdapter.deleteRow(position);
            }
        });


        // Listener para añadir ingrediente
        binding.addIngredienteBtnNewRecipe.setOnClickListener(v -> addIngrediente());

        // Listener para los cambios del texto de cantidadNewRecipe para que cumplan con el regex que queremos
        binding.cantidadNewRecipe.addTextChangedListener(new MyTextWatcher());
    }

    private void addIngrediente(){

        String nombreIngrediente, cantidad, magnitud;

        nombreIngrediente = binding.ingredienteNameNewRecipe.getText().toString();
        magnitud = binding.spinnerMagnitudNewRecipe.getText().toString();
        cantidad = binding.cantidadNewRecipe.getText().toString();

        // Primero comprovamos que no faltan campos por completar
        if (nombreIngrediente.isEmpty() || magnitud.isEmpty() || cantidad.isEmpty()) {
            Toast.makeText(getContext(), "Faltan campos por completar", Toast.LENGTH_SHORT).show();
            return; // Salimos del metodo sin hacer ninguna opcion
        }

        // Si todos los campos estan llenos empezamos a agregar el item a la lista.
        newRecipeViewModel.addIngrediente(new Ingrediente(nombreIngrediente, Float.parseFloat(cantidad), magnitud));
        ingredientesListAdapter.notifyItemInserted(ingredientesListAdapter.getItemCount());

        // Por ultimo limpiamos los textos y los dejamos en blanco

        binding.ingredienteNameNewRecipe.setText("");
        binding.cantidadNewRecipe.setText("");

    }

    // Esta clase hereda de PopUpWindow y su funcion es la de construir la ventana y aplicar cambios de configuracion
    public static class MPopUpWindow extends PopupWindow {
        private final TextView textView;

        public MPopUpWindow(Context context, TextView textView){
            super(context);
            this.textView = textView;
        }

        public void setConfiguration(final View view, final ListView listViewSpinner){

            this.setContentView(listViewSpinner);
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            this.setWidth(textView.getWidth()*3);
            this.setTouchable(true);
            this.setFocusable(true);
            this.setOutsideTouchable(true);
            this.showAsDropDown(view);
        }
    }

    public static class MyTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            // En este metodo comprovaremos si de mometno el regex se cumple
            // Queremos que el texto sea algo como "250.65" no queremos tener mas de 1000
            // Ni tener mas de 2 decimales, tampoco que empieze por punto.
            // Por lo que cuando se modifica el texto comprovaremos que el patron continua correcto
            if (!Pattern.matches("^[0-9]{1,3}(\\.[0-9]{0,2})?$", editable.toString())){

                // Si el patron no es correcto eliminaremos el ultimo caracter.
                // O lo que es lo mismo, no se escribira en el EditText
                if (editable.length() == 0) editable.clear();
                else
                    editable.delete(editable.length() - 1, editable.length());
            }
            // Si el texto esta bien no hacemos nada y se escribirá en el EditText.
        }
    }

    public class AlergiasListAdapter extends RecyclerView.Adapter<AlergiasListAdapter.ViewHolder> {

        private final List<String> alergias;

        protected final ArrayList<String> selectedAlerigas;

        public AlergiasListAdapter(List<String> alergias) {
            this.alergias = alergias;
            this.selectedAlerigas = new ArrayList<>();
        }

        @NonNull
        @Override
        public AlergiasListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View view = inflater.inflate(R.layout.item_alergias_newrecipe, parent, false);

            return new AlergiasListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AlergiasListAdapter.ViewHolder holder, int position) {
            holder.onBind(alergias.get(position));
        }

        @Override
        public int getItemCount() {
            return this.alergias.size();
        }

        // Getter
        // Conseguimos la lista de Alergias para crear la receta.
        public final ArrayList<String> getSelectedAlerigas(){ return this.selectedAlerigas; }

        public final class ViewHolder extends RecyclerView.ViewHolder {

            private final CheckBox alergiaCV;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.alergiaCV = itemView.findViewById(R.id.alergiasCB_newRecipe);
            }

            public void onBind(String _alergia) {

                // Le ponemos el nombre que pertoque.
                this.alergiaCV.setText(_alergia);

                // Añadimos un listener al checkbox de alergias para conseguir un array que contenga
                // las Alergias que tiene la receta.
                this.alergiaCV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        String alergia = ViewHolder.this.alergiaCV.getText().toString();
                        if (checked)
                            selectedAlerigas.add(alergia);
                        else
                            selectedAlerigas.remove(alergia);
                    }
                });
            }
        }
    }
}