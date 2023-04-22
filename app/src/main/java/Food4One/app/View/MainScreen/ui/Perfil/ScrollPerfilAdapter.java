package Food4One.app.View.MainScreen.ui.Perfil;

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
import Food4One.app.Model.User.User;
import Food4One.app.R;

public class ScrollPerfilAdapter extends RecyclerView.Adapter<ScrollPerfilAdapter.ViewHolder> {

/**
 * Definició de listener (interficie)
 * per a quan algú vulgui escoltar un event de OnClickHide, és a dir,
 * quan l'usuari faci clic en la creu (amagar) algún dels items de la RecyclerView
 */
public interface OnClickDoRecipeUser {
    void OnClickDoRecipe(int position);
}

    private ArrayList<Recipe> mRecetes; // Referència a la llista de recetes
    private ScrollPerfilAdapter.OnClickDoRecipeUser mOnClickDoRecipeListener; // Qui hagi de repintar la ReciclerView

    // quan s'amagui
    // Constructor
    public ScrollPerfilAdapter(ArrayList<Recipe> recetaList) {
        this.mRecetes = recetaList; // no fa new (La llista la manté el ViewModel)

    }

    public void setOnClickDetailListener(ScrollPerfilAdapter.OnClickDoRecipeUser listener) {
        this.mOnClickDoRecipeListener = listener;
    }

    @NonNull
    @Override
    public ScrollPerfilAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate crea una view genèrica definida pel layout que l'hi passem (l'user_card_layout)
        View view = inflater.inflate(R.layout.recetadetails_view_card, parent, false);

        // La classe ViewHolder farà de pont entre la classe User del model i la view (UserCard).
        return new ScrollPerfilAdapter.ViewHolder(view);
    }


    /* Mètode cridat per cada ViewHolder de la RecyclerView */
    @Override
    public void onBindViewHolder(@NonNull ScrollPerfilAdapter.ViewHolder holder, int position) {
        // El ViewHolder té el mètode que s'encarrega de llegir els atributs del User (1r parametre),
        // i assignar-los a les variables del ViewHolder.
        // Qualsevol listener que volguem posar a un item, ha d'entrar com a paràmetre extra (2n).
        holder.bind(mRecetes.get(position), this.mOnClickDoRecipeListener);
    }

    /**
     * Retorna el número d'elements a la llista.
     *
     * @return size of Recipes at the profile
     */
    @Override
    public int getItemCount() {
        return mRecetes.size();
    }

    /**
     * Mètode que seteja de nou la llista d'usuaris si s'hi han fet canvis de manera externa.
     *
     * @param recetes
     */
    public void setRecetes(ArrayList<Recipe> recetes) {
        this.mRecetes = recetes; // no recicla/repinta res
    }

/**
 * Mètode que repinta la RecyclerView sencera.

 public void updateUsers() {------------------------------------------------------------------
 notifyDataSetChanged();
 }
 */

/**
 * Mètode que repinta només posició indicada
 * @param position

public void hideUser(int position) {-------------------------------------------------
notifyItemRemoved(position);
}*/

/**
 * Classe ViewHolder. No és més que un placeholder de la vista (user_card_list.xml)
 * dels items de la RecyclerView. Podem implementar-ho fora de RecyclerViewAdapter,
 * però es pot fer dins.
 */
public static class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView mrecipeName;
    private final ImageView mCardUserPictureURL;
    private final ImageView mCardRecetaPictureUrl;
    private final TextView mCardNumberLikes;
    private final TextView mCardDescription;
    private final ImageView mCorazon;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.mCorazon = itemView.findViewById(R.id.imageHeartDetail);
        this.mrecipeName = itemView.findViewById(R.id.nombreDetailReceta);
        this.mCardNumberLikes = itemView.findViewById(R.id.likesDetailRecipe);
        this.mCardDescription = itemView.findViewById(R.id.decriptionDetailReceta);
        this.mCardUserPictureURL = itemView.findViewById(R.id.pictureUserReceta);
        this.mCardRecetaPictureUrl = itemView.findViewById(R.id.pictureDetailReceta);

    }

    public void bind(final Recipe recetaUser, ScrollPerfilAdapter.OnClickDoRecipeUser listener) {

        mCorazon.setImageResource(R.drawable.heart_24);
        mrecipeName.setText(recetaUser.getNombre());
        mCardNumberLikes.setText( Integer.toString(recetaUser.getLikes()) );
        mCardDescription.setText(User.getInstance().getUserName() +"  "+ recetaUser.getDescription());

        cargarPhotoUserAndRecipe(recetaUser);
        mrecipeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnClickDoRecipe(getAdapterPosition());
            }
        });
    }

    private void cargarPhotoUserAndRecipe(Recipe recetaUser) {
        //Es carrega l'imatge de la receta i del User d'internet
        Picasso.get().load(recetaUser.getPictureURL())
                .resize(450, 250)
                .centerCrop().into(mCardRecetaPictureUrl);

        Picasso.get().load(User.getInstance().getProfilePictureURL())
                .resize(200, 200)
                .centerCrop().into(mCardUserPictureURL);

    }
}

    }
