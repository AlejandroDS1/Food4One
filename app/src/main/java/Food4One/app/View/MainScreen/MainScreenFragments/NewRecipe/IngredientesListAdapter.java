package Food4One.app.View.MainScreen.MainScreenFragments.NewRecipe;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        LayoutInflater infalter = LayoutInflater.from(parent.getContext());

        View view = infalter.inflate(R.layout.item_ingrediente_list_newrecipie, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientesListAdapter.ViewHolder holder, int position) {
        Ingrediente _ingrediente = this.ingredientesList.getIngredientes().get(position);
        holder.onBind(_ingrediente.getName(), _ingrediente.getCantidadStr(), this.listener);
    }

    @Override
    public int getItemCount() {
        return this.ingredientesList.getSize();
    }

    public void deleteRow(int position){
        this.ingredientesList.getIngredientes().remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreIngrediente, cantidadIngrediente, trashCan;
        private CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.cardView = itemView.findViewById(R.id.cardView_newRecipe);
            this.nombreIngrediente = itemView.findViewById(R.id.ingredienteList_name_newRecipe);
            this.cantidadIngrediente = itemView.findViewById(R.id.cantidadymagnitud_newRecipe);
            this.trashCan = itemView.findViewById(R.id.trashCan_newRecipe);
        }


        public void onBind(final String _nameIngrediente, final String _cantidad, OnTrashCanClickListener listener){

            this.nombreIngrediente.setText(_nameIngrediente);
            this.cantidadIngrediente.setText(_cantidad);

            trashCan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onTrashCanClickListener(getAdapterPosition());
                }
            });
        }
    }
}
