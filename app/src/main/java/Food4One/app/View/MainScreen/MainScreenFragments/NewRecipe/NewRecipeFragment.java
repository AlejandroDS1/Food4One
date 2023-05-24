package Food4One.app.View.MainScreen.MainScreenFragments.NewRecipe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import Food4One.app.Model.Recipe.Ingredients.Ingrediente;
import Food4One.app.Model.Recipe.Recipe.RecipesUserApp;
import Food4One.app.Model.User.UserRepository;
import Food4One.app.R;
import Food4One.app.View.MainScreen.MainScreenFragments.Perfil.PerfilViewModel;
import Food4One.app.databinding.FragmentNewRecipeBinding;

public class NewRecipeFragment extends Fragment {
    private FragmentNewRecipeBinding binding;
    private NewRecipeViewModel newRecipeViewModel;
    private IngredientesListAdapter ingredientesListAdapter;
    private MPopUpWindow _popUpWindow;
    private Uri recipePhotoUri;
    private ActivityResultLauncher<Intent> takePictureLauncher, galeryLauncher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        newRecipeViewModel = new ViewModelProvider(this).get(NewRecipeViewModel.class);

        binding = FragmentNewRecipeBinding.inflate(inflater, container, false);

        initViews();
        initUploadButton(); // Dejamos el upload button como un metodo aparte
        initLists();
        initListeners();

        return binding.getRoot();
    }

    /**
     * Añade la logica del clickListener para subir la nueva receta a la base de datos
     */
    private void initUploadButton() {

        // Creamos un observer para la variable del viewModel que nos dira si se ha completado correctamente el upload a base de datos o no.
        final Observer<Byte> completedUpload_Observer = new Observer<Byte>() {
            @Override
            public void onChanged(Byte flag) {
                // Comprovamos que codigo tenemos
                switch (flag){

                    case 1: // En este caso t0do ha salido bien, asi que nos movemos al perfil para ver las recetas.
                        uploadSucces();
                        break;

                    case 2: // En este caso ha habido algun error, lo mostramos por pantalla.
                        uploadFailed();
                        break;

                    default: // Este caso no deberia ocurrir, no hacemos nada.
                        break;

                }
            }
            // TODO MEJORAR ESTOS METODOS
            private void uploadSucces() {
                Toast.makeText(getContext(), "Receta subida correctamente", Toast.LENGTH_SHORT).show();
                UserRepository.getUser().getIdRecetas().add(binding.newRecipieName.getText().toString() + "@" + UserRepository.getUser().getUserName());
                UserRepository.getInstance().setUserIdRecipe();
                PerfilViewModel.getInstance().setRecetes(RecipesUserApp.getRecetasUser());

                Navigation.findNavController(getView()).navigateUp();
                Navigation.findNavController(getView()).navigate(R.id.navigation_perfil);
            }

            private void uploadFailed() {
                Toast.makeText(getContext(), "Error al publicar la receta", Toast.LENGTH_SHORT).show();
            }
        };
        newRecipeViewModel.getUpdatedFlag().observe(this.getViewLifecycleOwner(), completedUpload_Observer);

        binding.uploadRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkAllFields()){ // Si falta algun campo por rellenar se informa con un toast.
                    Toast.makeText(getActivity().getApplicationContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                newRecipeViewModel.uploadRecipe(binding.newRecipieName.getText().toString(), binding.newRecipieDescription.getText().toString());
            }
        });
    }


    private boolean checkAllFields(){
        return newRecipeViewModel.getRecipePhotoUri().getValue() != null           // Comprovamos si hay imagen
                 && !binding.newRecipieName.getText().toString().isEmpty()         // Comprovamos si hay titulo
                 && !binding.newRecipieDescription.getText().toString().isEmpty()  // Comprovamos si hay descripcion
                 && ingredientesListAdapter.getItemCount() != 0                    // Comprovamos que haya al menos un ingrediente
                 && binding.stepsListNewRecipe.getAdapter().getItemCount() != 0;   // Comrpovamos que hay al menos un paso.
    }

    private void initViews() {
        // Si ya hay una foto cargada, la cargamos en la imagen
        if(newRecipeViewModel.getRecipePhotoUri().getValue() != null) {
            Picasso.get().load(newRecipeViewModel.getRecipePhotoUri().getValue())
                    .resize(1000, 1000)
                    .into(binding.imagenRecetaNewRecpie);
        }
    }


    private void initLists() {

        // Lista de ingredientes ---------------------------
        ingredientesListAdapter = new IngredientesListAdapter(newRecipeViewModel.getIngredientesList().getValue());

        LinearLayoutManager manager = new LinearLayoutManager(getContext());

        binding.ingredienteListNewRecipe.setLayoutManager(manager);
        binding.ingredienteListNewRecipe.setAdapter(ingredientesListAdapter);

        // Iniciamos la lista de Alergias -------------------------------
        List<String> alergias = new ArrayList<>();

        alergias.addAll(Arrays.asList(getResources().getStringArray(R.array.Alergias)));

        //Ahora le definimos un Manager Grid

        AlergiasListAdapter alergiasAdapter = new AlergiasListAdapter(alergias);

        GridLayoutManager managerAlergias = new GridLayoutManager(
                this.getContext(), GridLayoutManager.VERTICAL);
        managerAlergias.setSpanCount(2); //Number of columns.

        binding.alergiasListNewRecipe.setAdapter(alergiasAdapter);
        binding.alergiasListNewRecipe.setLayoutManager(managerAlergias);


        // Spinner para la magnitud de los ingredientes ------------------------------
        // Ahora vamos a crear el listener para nuestro PopUpWindow personalizado.
        _popUpWindow = new MPopUpWindow(getContext(), binding.spinnerMagnitudNewRecipe); // Creamos el objeto PopUpWindow

        binding.spinnerMagnitudNewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
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


        // Inicio de la lista de Pasos ---------------------------------
        StepListAdapter adapter = new StepListAdapter();

        LinearLayoutManager StepManager = new LinearLayoutManager(getContext());

        binding.stepsListNewRecipe.setLayoutManager(StepManager);
        binding.stepsListNewRecipe.setAdapter(adapter);

        setTouchHelpersStepList(); // Acabamos de configurar la lista de pasos.
    }

    private void initListeners() {

        initCamera_GaleryIntents();
        // Listener para añadir imagen

        // ClickListener para la imagen de receta que tiene que cambiar con la imagen seleccionada.
        binding.imagenRecetaNewRecpie.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Aqui crearemos un alertDialog que nos dara a elegir entre
                // Subir la foto desde galeria o desde la camara.

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                final View _view = LayoutInflater.from(getContext()).inflate(R.layout.addpicture_customdialog_new_recipe, null);

                builder.setView(_view);
                AlertDialog alert = builder.create();
                _view.findViewById(R.id.dialog_camera_newRecipe).setOnClickListener(v -> cameraImage(alert));

                _view.findViewById(R.id.dialog_galeria_newRecipe).setOnClickListener(v -> selectGaleria(alert));


                alert.show(); // Mostramoos el alertDialog
            }

            private void cameraImage(final AlertDialog alert) {

                // Creamos archivo temporal
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                // Anem a buscar el directori extern (del sistema) especificat per la variable
                // d'entorn Environment.DIRECTORY_PICTURES (pren per valor "Pictures").
                // Se li afageix, com a sufix, el directori del sistema on es guarden els fitxers.

                File storageDir = NewRecipeFragment.this.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                // Creem el fitxer
                File image = null;
                try {
                    image = File.createTempFile(
                            imageFileName,  /* Prefix */
                            ".jpg",         /* Sufix */
                            storageDir      /* Directori on es guarda la imatge */
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Recuperem la Uri definitiva del fitxer amb FileProvider (obligatori per seguretat)
                // Per a fer-ho:
                // 1. Especifiquem a res>xml>paths.xml el directori on es guardarà la imatge
                //    de manera definitiva.
                // 2. Afegir al manifest un provider que apunti a paths.xml del pas 1
                recipePhotoUri = FileProvider.getUriForFile(NewRecipeFragment.this.getContext(),
                        "Food4One.app.fileprovider",
                        image);


                // Llancem l'intent amb el launcher declarat al començament d'aquest mateix mètode
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, recipePhotoUri);
                takePictureLauncher.launch(intent);

                alert.dismiss();
            }

            private void selectGaleria(final AlertDialog alert) {
                // Listener del botó de seleccionar imatge, que llençarà l'intent amb l'ActivityResultLauncher.
                    Intent data = new Intent(Intent.ACTION_GET_CONTENT);
                    data.addCategory(Intent.CATEGORY_OPENABLE);
                    data.setType("image/*");
                    Intent intent = Intent.createChooser(data, "Choose a file");

                    galeryLauncher.launch(intent);
                    alert.dismiss();
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

        // Listener para añadir step.
        binding.addStepBtnNewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String step = binding.stepTextNewRecipe.getText().toString();

                if (step.isEmpty()) {
                    Toast.makeText(NewRecipeFragment.this.getContext(), "El paso esta vacío", Toast.LENGTH_SHORT).show();
                    return;
                }
                newRecipeViewModel.addSteptoList(step); // Añadimos el step a la lista del viewModel.
                NewRecipeFragment.StepListAdapter stepAdapter = (NewRecipeFragment.StepListAdapter) binding.stepsListNewRecipe.getAdapter();
                stepAdapter.notifyItemInserted(stepAdapter.getItemCount());
                binding.stepTextNewRecipe.setText("");

                hideKeyboard();
            }
        });

    }

    private void hideKeyboard(){
        // Esta linea oculta el teclado despues de añadir el paso
        ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
    }

    private void initCamera_GaleryIntents() {

        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            newRecipeViewModel.setRecipePhotoUri(recipePhotoUri);
                            final int size = binding.imagenRecetaNewRecpie.getHeight();
                            Picasso.get().load(recipePhotoUri).
                                    resize(size, size).into(binding.imagenRecetaNewRecpie);
                        }
                    }
                }
        );

        // ActivityResultLauncher para foto de galeria
        galeryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        newRecipeViewModel.setRecipePhotoUri(data.getData()); // En aquest intent, sí que hi arriba la URI
                        final int size = binding.imagenRecetaNewRecpie.getHeight();
                        Picasso.get().load(data.getData())
                                .resize(size, size).into(binding.imagenRecetaNewRecpie);
                    }
                });
    }

    private void setTouchHelpersStepList(){
        // TouchHelper para eliminar elementos
        final ItemTouchHelper.SimpleCallback touchHelperDelete = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                ((StepListAdapter) binding.stepsListNewRecipe.getAdapter()).deleteRow(viewHolder.getAdapterPosition());
            }
        };
        final ItemTouchHelper itemTouchHelperDelete = new ItemTouchHelper(touchHelperDelete);
        itemTouchHelperDelete.attachToRecyclerView(binding.stepsListNewRecipe);

        // ItemTouchHelper para mover los elementos de orden
        final ItemTouchHelper.SimpleCallback touchHelperMoveRows = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                ((StepListAdapter)recyclerView.getAdapter()).switchRows(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Log.d("CHECK MOVED position", Integer.toString(viewHolder.getAdapterPosition()));
                return true;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}
        };

        final ItemTouchHelper itemTouchHelperSwapPos = new ItemTouchHelper(touchHelperMoveRows);
        itemTouchHelperSwapPos.attachToRecyclerView(binding.stepsListNewRecipe);
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

        hideKeyboard();
    }

    /**
     * Esta clase hereda de PopUpWindow y su funcion es la de construir la ventana y aplicar cambios de configuracion
     * De esta manera no tenemos que instanciarla constantemente, se instancia una vez y solo tenemos que aplicar la configuracion
     */
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

    /**
     * Clase interna TextWatcher para tener un listener de correccion de texto al introducir
     * la cantidad de un ingrediente. Que cumpla el patron que queremos
     */
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

    /**
     * RecyclerViewAdapter para mostrar la lista de ingredientes en NewRecipe
     */
    public class AlergiasListAdapter extends RecyclerView.Adapter<AlergiasListAdapter.ViewHolder> {

        private final List<String> alergias;
        private final List<String> selectedAlergias;

        public AlergiasListAdapter(final List<String> alergias) {
            this.alergias = alergias;
            selectedAlergias = newRecipeViewModel.getAlergiasList().getValue();
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

        public class ViewHolder extends RecyclerView.ViewHolder {

            private final CheckBox alergiaCV;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.alergiaCV = itemView.findViewById(R.id.alergiasCB_newRecipe);
            }

            public void onBind(String _alergia) {

                // Le ponemos el nombre que pertoque.
                this.alergiaCV.setText(_alergia);

                if (selectedAlergias.contains(_alergia))
                    this.alergiaCV.setChecked(true); // Si ya esta marcado marcamos el checkbox.

                // Añadimos un listener al checkbox de alergias para conseguir un array que contenga
                // las Alergias que tiene la receta.
                this.alergiaCV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        String alergia = ViewHolder.this.alergiaCV.getText().toString();
                        if (checked)
                            selectedAlergias.add(alergia);
                        else
                            selectedAlergias.remove(alergia);
                    }
                });
            }
        }
    }

    /**
     * RecyclerViewAdapter para mostrar los pasos de la aplicación
     */
    public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.ViewHolder>{

        private final List<String> stepsList;

        public StepListAdapter(){
            this.stepsList = newRecipeViewModel.getStepsList().getValue();
        }

        @NonNull
        @Override
        public StepListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View view = inflater.inflate(R.layout.item_steps_list_new_recipe, parent, false);

            return new StepListAdapter.ViewHolder(view);
        }

        // Delete row
        public void deleteRow(int position){
            stepsList.remove(position);
            notifyDataSetChanged();
        }

        public void switchRows(int draggedVH, int targedVH){

            Collections.swap(stepsList, draggedVH, targedVH);
            notifyItemChanged(draggedVH);
            notifyItemChanged(targedVH);
            notifyItemMoved(draggedVH, targedVH);
        }

        @Override
        public void onBindViewHolder(@NonNull StepListAdapter.ViewHolder holder, int position) {
            holder.bind();
        }
        @Override
        public int getItemCount() {
            return this.stepsList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView stepTxt, numStep;
            private final LinearLayout layout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.steps_layout_newRecipe);
                this.stepTxt = itemView.findViewById(R.id.stepTxt_newRecipe);
                numStep = itemView.findViewById(R.id.numStep_newRecipe);
            }

            public void bind(){

                final int position = getAdapterPosition();

                final String stepText = stepsList.get(position);

                this.stepTxt.setText(stepText);
                this.numStep.setText((position+1) + ".-");

            }
        }
    }
}