package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import Food4One.app.Model.Recipe.Ingredients.Ingrediente;
import Food4One.app.Model.Recipe.Ingredients.IngredientesList;
import Food4One.app.R;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>{

    private final IngredientesList ingredientesList;
    private ShoppingListViewModel viewModel;
    private boolean isCheckList = false;

    private ShoppingListAdapter secondList; // Si este adapter corresponde a la lista de Checked, este es el adapter UnChecked

    public ShoppingListAdapter(IngredientesList ingredientesList,
                               ShoppingListViewModel viewModel) {
        this.ingredientesList = ingredientesList;
        this.viewModel = viewModel;
    }

    public ShoppingListAdapter setAsCheckList(){ this.isCheckList = true; return this; }

    public void setSecondList(final ShoppingListAdapter shoppingListAdapter){
        this.secondList = shoppingListAdapter;
        shoppingListAdapter.secondList = this;
    }

    /**
     * Notifica que se ha cambiado un elemento de esta lista a la otra.
     * @param pos
     */
    public void notifyItemChangedList(final int pos){
        notifyItemRemoved(pos); // Notificamos que se ha eliminado el elemento en esta lista
        secondList.notifyItemInserted(secondList.getItemCount() - 1); // Notificamos que se ha añadido el elemento ** Sera el ultimo elemento **
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.shopping_list_card_view, parent, false);

        return new ShoppingListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ViewHolder holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.recycler_view_left_fadein);

        animation.setDuration(200);

        holder.cardView.setAnimation(animation);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return ingredientesList.getSize();
    }


    public final class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView cantidadTxt, multiplicadorTxt;
        private final CheckBox ingredienteCB;
        private final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ingredienteCB = itemView.findViewById(R.id.ingrediente_CB);
            this.cantidadTxt = itemView.findViewById(R.id.cantidad_txt);
            this.multiplicadorTxt = itemView.findViewById(R.id.multiplicador_txt);
            this.cardView = itemView.findViewById(R.id.cardView_shoppingList);
        }

        public void bind(final int pos) {

            // Llenamos los datos del CardView con la informacion del ingrediente

            final Ingrediente ingrediente = ingredientesList.get(pos);

            this.ingredienteCB.setText(ingrediente.getName());
            this.ingredienteCB.setChecked(isCheckList);

            this.cantidadTxt.setText(ingrediente.getCantidadStr());

            this.multiplicadorTxt.setText(Integer.toString(ingrediente.getMultiplicador()));

            this.cardView.setClickable(true);

            this.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final boolean checked = !ingredienteCB.isChecked();

                    // Invertimos el sentido de checked del checkBox
                    ingredienteCB.setChecked(checked);


                    final int _pos = ingredientesList.getIngredientes().indexOf(ingrediente);
                    // Cambiamos el ingrediente de Lista al que corresponda
                    // Tambien se guarda en base de datos
                    viewModel.updateIngredienteCheckState(ingrediente, checked);
                    // notificamos al adapter que se ha cambiado el data set.
                    notifyItemChangedList(_pos);
                }
            });
        }
    }
}
