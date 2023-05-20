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
    private IngredientesList ingredientesList;
    private final ShoppingListViewModel viewModel;
    private ShoppingListAdapter secondList; // Este atributo permite tener los dos adapters vinculados, para hacer los cambios

    public ShoppingListAdapter(@NonNull final IngredientesList ingredientesList,
                               @NonNull final ShoppingListViewModel viewModel) {
        this.ingredientesList = ingredientesList;
        this.viewModel = viewModel;
    }

    public final ShoppingListAdapter setSecondList(@NonNull final ShoppingListAdapter otherList){
        this.secondList = otherList;
        otherList.secondList = this;
        return this;
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
        holder.bind(ingredientesList.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredientesList.getSize();
    }

    public final class ViewHolder extends RecyclerView.ViewHolder{

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


        public void bind(Ingrediente ingrediente){

            // Llenamos los datos del CardView con la informacion del ingrediente
            this.ingredienteCB.setText(ingrediente.getName());
            this.ingredienteCB.setChecked(ingrediente.checked);

            this.cantidadTxt.setText(ingrediente.getCantidadStr());

            this.multiplicadorTxt.setText(Integer.toString(ingrediente.getMultiplicador()));

            this.cardView.setClickable(true);

            // Este listener permite que se active el ceckbox al presionar en cualquier parte
            this.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final boolean checkState = !ingredienteCB.isChecked();
                    ingredienteCB.setChecked(checkState); // Invertimos el checked

                    notifyItemRemoved(ingredientesList.getIngredientes().indexOf(ingrediente));

                    viewModel.swapCheckedItem(ingrediente, checkState);
                    // Ahora notificamos al adapter el cambio de ingredientes
                    secondList.notifyItemInserted(secondList.getItemCount() - 1);

                }
            });

        }
    }
}
