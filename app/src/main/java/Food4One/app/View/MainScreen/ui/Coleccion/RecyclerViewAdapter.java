package Food4One.app.View.MainScreen.ui.Coleccion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Food4One.app.Model.Recipie.Recipie.Recipe;
import Food4One.app.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Recipe> mRecipes; //Referència a la llista de receptes


    public RecyclerViewAdapter(ArrayList<Recipe> mRecipes) {
        this.mRecipes = mRecipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate crea una view genèrica definida pel layout que l'hi passem (recipe_card_layout)
        View view = inflater.inflate(R.layout.recipe_card_layout, parent, false);
        return new RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mRecipes.get(position)); //Haure de passar l'usuari per entrar en la seva col·lecció i agafar les IDs
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView mCardPictureUrl;
        private final TextView mCardNameRecipe;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mCardPictureUrl = itemView.findViewById(R.id.imgRecipe);
            mCardNameRecipe = itemView.findViewById(R.id.txtRecipe);
        }

        public void bind(final Recipe recipe) {
            mCardNameRecipe.setText(recipe.getNombre());

            Picasso.get().load(recipe.getPictureURL()).into(mCardPictureUrl);

            //mCardNameRecipe.setText("Paellita ");
        }
    }
}
