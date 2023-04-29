package Food4One.app.View.MainScreen.MainScreenFragments.NewRecipe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import Food4One.app.Model.Recipe.Ingredients.Ingrediente;
import Food4One.app.Model.Recipe.Ingredients.IngredientesList;
import Food4One.app.R;

public class IngredientesListAdapter extends RecyclerView.Adapter<IngredientesListAdapter.ViewHolder> {
    private IngredientesList ingredientesList;
    private OnTrashCanClickListener listener;
    public interface OnTrashCanClickListener{
        void onTrashCanClickListener(int position);
    }

    public IngredientesListAdapter(IngredientesList _list){
        this.ingredientesList = _list;
    }

    public void setOnTrashCanClickListener(OnTrashCanClickListener listener){ this.listener = listener; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_ingrediente_list_newrecipie, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientesListAdapter.ViewHolder holder, int position) {
        Ingrediente _ingrediente = this.ingredientesList.getIngredientes().get(position);
        holder.onBind(_ingrediente.getName(), _ingrediente.getCantidadStr(), this.listener, this.ingredientesList);
    }

    @Override
    public int getItemCount() {
        return this.ingredientesList.getSize();
    }

    public void deleteRow(int position){

        try{
            this.ingredientesList.getIngredientes().remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException e){}
            // Si le das al boton de la papelera dos veces muy rapido, mientras se esta borrando
            // position vale -1 y la aplicacion crashea, por el remove(-1) por lo tanto
            // hacemos un try catch que sirve para si le han dado dos veces muy rapido no hacer nada.
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nombreIngrediente, cantidadIngrediente, trashCan;
        private final CardView cardView;
        private final LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.cardView = itemView.findViewById(R.id.cardView_newRecipe);
            this.nombreIngrediente = itemView.findViewById(R.id.ingredienteList_name_newRecipe);
            this.cantidadIngrediente = itemView.findViewById(R.id.cantidadymagnitud_newRecipe);
            this.trashCan = itemView.findViewById(R.id.trashCan_newRecipe);
            this.linearLayout = itemView.findViewById(R.id.layout_ingrediente_newRecipe);
        }


        public void onBind(final String _nameIngrediente, final String _cantidad, OnTrashCanClickListener listener, IngredientesList ingredientesList){

            Animation animation = AnimationUtils.loadAnimation(this.itemView.getContext(), R.anim.desplazar_abajo);
            animation.setDuration(300);

            cardView.setAnimation(animation);

            this.nombreIngrediente.setText(_nameIngrediente);
            this.cantidadIngrediente.setText(_cantidad);

            trashCan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onTrashCanClickListener(getAdapterPosition());

                }
            });



            this.linearLayout.setOnClickListener(listen -> modifyIngredienteListener(ingredientesList));
        }

        private void modifyIngredienteListener(IngredientesList ingredientesList) {

            Ingrediente ingrediente = ingredientesList.get(getAdapterPosition());
            Context context = itemView.getContext();

            // Creamos el objeto constructor de AlertsDialogs
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Modifica el ingrediente");

            // Lo inflamos con el AlertDialog customizado
            final View view = LayoutInflater.from(context).inflate(R.layout.mod_ingrediente_alertdialog_new_recipe, null);

            // Creamos los views y los conseguimos del layout
            EditText _nombreIngrediente = view.findViewById(R.id.nombreIngrediente_customDialog),
                    cantidad = view.findViewById(R.id.cantidad_customDialog);

            TextView spinner = view.findViewById(R.id.magnitud_customDialog);

            // Asignamos los valores a los respectivos layouts
            cantidad.addTextChangedListener(new NewRecipeFragment.MyTextWatcher());
            _nombreIngrediente.setText(ingrediente.getName());
            cantidad.setText(String.valueOf(ingrediente.getCantidad()));

            spinner.setText(String.valueOf(ingrediente.getMagnitud()));

            spinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NewRecipeFragment.MPopUpWindow _popUpWindow = new NewRecipeFragment.MPopUpWindow(context, spinner);
                    ListView listViewSpinner = new ListView(context);
                    final List<String> lista = Arrays.asList(context.getResources().getStringArray(R.array.MagnitudesLong));

                    listViewSpinner.setAdapter(new ArrayAdapter<String>(context, R.layout.spinner_ingredientes_list_layout, lista));

                    listViewSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            _popUpWindow.dismiss();
                            spinner.setText(context.getResources().getStringArray(R.array.Magnitudes)[i]);
                        }
                    });
                    _popUpWindow.setConfiguration(view, listViewSpinner);
                }
            });

            // Añadimos el adapter del Spinner

            builder.setView(view);
            // Añadimos botones de cancelar y guardar
            builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ingrediente.setCantidad(Float.parseFloat(cantidadIngrediente.getText().toString()));
                    ingrediente.setMagnitud(spinner.getText().toString());
                    ingrediente.setName(_nombreIngrediente.getText().toString());
                }
            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}