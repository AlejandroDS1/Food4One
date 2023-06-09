package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;
import Food4One.app.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Recipe> mRecipes; //Referència a la llista de receptes

    private RecipeRepository.OnLoadRecipeToMake recipeToMakeListener;

    public void setRecipeToMakeListener(RecipeRepository.OnLoadRecipeToMake listener) {
        this.recipeToMakeListener = listener;
    }


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

    /* Mètode cridat per cada ViewHolder de la RecyclerView */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mRecipes.get(position), recipeToMakeListener);
    }

    /**
     * Retorna el número d'elements a la llista.
     * @return
     */
    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public void setRecipe(Recipe recipe){this.mRecipes.add(recipe); this.notifyDataSetChanged();}

    /**
     * Classe ViewHolder. No és més que un placeholder de la vista (user_card_list.xml)
     * dels items de la RecyclerView. Podem implementar-ho fora de RecyclerViewAdapter,
     * però es pot fer dins.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView mCardPictureUrl;
        private final TextView mCardNameRecipe;
        private final LinearLayout mCardRecipe;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mCardPictureUrl = itemView.findViewById(R.id.imgRecipe);
            mCardNameRecipe = itemView.findViewById(R.id.txtRecipe);
            mCardRecipe = itemView.findViewById(R.id.collectionCard);
        }

        public void bind(final Recipe recipe, RecipeRepository.OnLoadRecipeToMake listener) {
            mCardNameRecipe.setText(recipe.getNombre().split("@")[0]);

            // Carrega foto de l'usuari de la llista directament des d'una Url
            // d'Internet.
            Picasso.get().load(recipe.getPictureURL()).resize(200, 900).into(mCardPictureUrl);

            mCardRecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnLoadRecipe(recipe);
                }
            });

        }
    }
}
