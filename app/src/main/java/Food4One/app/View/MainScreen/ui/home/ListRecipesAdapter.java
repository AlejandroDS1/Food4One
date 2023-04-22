package Food4One.app.View.MainScreen.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Food4One.app.R;

public class ListRecipesAdapter extends RecyclerView.Adapter<ListRecipesAdapter.ViewHolder> {
    Context context;
    private ArrayList<ListRecipes> mData;

    public ListRecipesAdapter(Context context, ArrayList<ListRecipes> mData) {
        this.context = context;
        this.mData = mData;
    }
    public interface OnClickListenerHomeSelection{
        void onClickHomeSelection(String position);
    }

    private OnClickListenerHomeSelection mOnClickListenerHomeSelection;

    public void setmOnClickListenerHomeSelection(OnClickListenerHomeSelection mOnClickListenerHomeSelection) {
        this.mOnClickListenerHomeSelection = mOnClickListenerHomeSelection;
    }

    @NonNull
    @Override
    public ListRecipesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_homerecetes_card, null);
        return new ListRecipesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecipesAdapter.ViewHolder holder, int position) {
        holder.bindData(mData.get(position), this.mOnClickListenerHomeSelection);
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

        public void bindData(ListRecipes item, OnClickListenerHomeSelection listenerHomeSelection){

            imageViewRecipe.setImageResource(item.getImagen());
            nameViewRecipe.setText(item.getNameRecipeCard());
            totalCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenerHomeSelection.onClickHomeSelection(item.getNameRecipeCard());
                }
            });

        }
    }
}
