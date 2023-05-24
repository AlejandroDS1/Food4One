package Food4One.app.View.MainScreen.MainScreenFragments.Perfil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.User.UserRepository;
import Food4One.app.R;

public class ScrollPerfilAdapter extends RecyclerView.Adapter<ScrollPerfilAdapter.ViewHolder> {

/**
 * Definició de listener (interficie)
 * per a quan algú vulgui escoltar un event de OnClickHide, és a dir,
 * quan l'usuari faci clic en la creu (amagar) algún dels items de la RecyclerView
 */
public interface OnClickDoRecipeUser {
    void OnClickDoRecipe(Recipe position);
}

    private ArrayList<Recipe> mRecetes; // Referència a la llista de recetes
    private ScrollPerfilAdapter.OnClickDoRecipeUser mOnClickDoRecipeListener; // Qui hagi de repintar la ReciclerView
    private PerfilViewModel viewModel;

    // quan s'amagui
    // Constructor
    public ScrollPerfilAdapter(ArrayList<Recipe> recetaList, PerfilViewModel viewModel) {
        this.mRecetes = recetaList; // no fa new (La llista la manté el ViewModel)
        this.viewModel = viewModel;
    }

    public void setOnClickDetailListener(ScrollPerfilAdapter.OnClickDoRecipeUser listener) {
        this.mOnClickDoRecipeListener = listener;
    }


    @NonNull
    @Override
    public ScrollPerfilAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate crea una view genèrica definida pel layout que l'hi passem (l'user_card_layout)
        View view = inflater.inflate(R.layout.scrollperfilview_card, parent, false);

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
        if(position == 0)
            viewModel.getmText().setValue("RecycleView Cargarda");
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mrecipeName = itemView.findViewById(R.id.nombreDetailReceta);
            this.mCardNumberLikes = itemView.findViewById(R.id.likesDetailRecipe);
            this.mCardDescription = itemView.findViewById(R.id.decriptionDetailReceta);
            this.mCardUserPictureURL = itemView.findViewById(R.id.pictureUserReceta);
            this.mCardRecetaPictureUrl = itemView.findViewById(R.id.pictureDetailReceta);
        }

        public void bind(final Recipe recetaUser, ScrollPerfilAdapter.OnClickDoRecipeUser listener) {

            mrecipeName.setText(recetaUser.getNombre());
            mCardNumberLikes.setText( Integer.toString(recetaUser.getLikes()) );
            mCardDescription.setText(UserRepository.getUser().getUserName() +"  "+ recetaUser.getDescription());

            cargarPhotoUserAndRecipe(recetaUser);
            mrecipeName.setOnClickListener( view->{
                    listener.OnClickDoRecipe(recetaUser);
            });

        }

        private void cargarPhotoUserAndRecipe(Recipe recetaUser) {
            //Es carrega l'imatge de la receta i del User d'internet
            Picasso.get().load(recetaUser.getPictureURL())
                    .resize(980, 800)
                    .centerCrop().into(mCardRecetaPictureUrl);

            String url = UserRepository.getUser().getProfilePictureURL();

            if (url != null) {
                if (!url.equals(" "))
                    Picasso.get().load(url)
                            .resize(200, 200)
                            .centerCrop().into(mCardUserPictureURL);
                else
                    mCardUserPictureURL.setImageResource(R.mipmap.ic_launcher_foreground);
            } else
                mCardUserPictureURL.setImageResource(R.mipmap.ic_launcher_foreground);
        }
    }
}
