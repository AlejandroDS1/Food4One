package Food4One.app.View.MainScreen.MainScreenFragments.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.R;

public class RecipeTypeAdapter extends RecyclerView.Adapter<RecipeTypeAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Recipe> mData;
    private OnClickListenerTypeSelection mOnClickListenerTypeSelection;


    public RecipeTypeAdapter(Context context, ArrayList<Recipe> mData) {
        this.context = context;
        this.mData = mData;
    }

    public interface OnClickListenerTypeSelection{
        void onClickTypeSelection(Recipe position);
    }

    public void setmOnClickListenerHomeSelection(RecipeTypeAdapter.OnClickListenerTypeSelection mOnClickListenerHomeSelection) {
        this.mOnClickListenerTypeSelection = mOnClickListenerHomeSelection;
    }

    @NonNull
    @Override
    public RecipeTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_homerecetes_card, null);
        return new RecipeTypeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeTypeAdapter.ViewHolder holder, int position) {
        holder.bindData(mData.get(position), this.mOnClickListenerTypeSelection);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

public static class ViewHolder extends RecyclerView.ViewHolder{
    private ImageView imageViewRecipe;
    private TextView nameViewRecipe;
    private CardView totalCard;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.imageViewRecipe = itemView.findViewById(R.id.imageOfRecipes);
        this.nameViewRecipe = itemView.findViewById(R.id.nameOfRecipes);
        this.totalCard = itemView.findViewById(R.id.homeSelection);
    }

    public void bindData(Recipe recipe, RecipeTypeAdapter.OnClickListenerTypeSelection listenerTypeSelection){

        nameViewRecipe.setText(recipe.getNombre());

        Picasso.get().load(recipe.getPictureURL())
                .resize(200, 200)
                .centerCrop().into(imageViewRecipe);

        totalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pasamos la receta que se ha seleccionado
                listenerTypeSelection.onClickTypeSelection(recipe);
            }
        });

    }
}
}