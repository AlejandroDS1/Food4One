package Food4One.app.View.MainScreen.MainScreenFragments.Explore;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.User.UserRepository;
import Food4One.app.R;

public class ExplorerScrollAdapter extends RecyclerView.Adapter<ExplorerScrollAdapter.ViewHolder> {
    private static Context context;
    /**
     * Definició de listener (interficie)
     * per a quan algú vulgui escoltar un event de OnClickDoRecipe, és a dir,
     * quan l'usuari faci clic en algún dels items de la RecyclerView
     */
    public interface OnClickDoRecipeUser {
        void OnClickDoRecipe(Recipe recipe);
    }
    public interface OnClickSaveRecipe{
        void OnClickSave(Recipe recipe, boolean guardado);
    }
    public interface OnLikeRecipeUser{
        void OnLikeRecipe(Recipe recipe, boolean like);
    }
    private ArrayList<Recipe> mRecetes; // Referència a la llista de recetes
    private OnClickDoRecipeUser mOnClickDoRecipeListener; // Qui hagi de repintar la ReciclerView
    private OnLikeRecipeUser mOnLikeRecipeListener;
    private OnClickSaveRecipe mOnSaveRecipeListener;
    public ExplorerScrollAdapter(ArrayList<Recipe> recetaList) {
        this.mRecetes = recetaList; // no fa new (La llista la manté el ViewModel)
    }
    public void setOnClickDetailListener(ExplorerScrollAdapter.OnClickDoRecipeUser listener) {
        this.mOnClickDoRecipeListener = listener;
    }
    public void setOnLikeRecipeListener(OnLikeRecipeUser listener){
        this.mOnLikeRecipeListener = listener;
    }
    public void setOnClickSaveListener(OnClickSaveRecipe listener){
        this.mOnSaveRecipeListener = listener;
    }
    public void setContext(Context context){
        this.context = context;
    }
    @NonNull
    @Override
    public ExplorerScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate crea una view genèrica definida pel layout que l'hi passem (l'user_card_layout)
        View view = inflater.inflate(R.layout.recetadetails_view_card, parent, false);

        // La classe ViewHolder farà de pont entre la classe User del model i la view (UserCard).
        return new ExplorerScrollAdapter.ViewHolder(view);
    }

    /* Mètode cridat per cada ViewHolder de la RecyclerView */
    @Override
    public void onBindViewHolder(@NonNull ExplorerScrollAdapter.ViewHolder holder, int position) {
        // El ViewHolder té el mètode que s'encarrega de llegir els atributs del User (1r parametre),
        // i assignar-los a les variables del ViewHolder.
        // Qualsevol listener que volguem posar a un item, ha d'entrar com a paràmetre extra (2n).
        holder.bind(mRecetes.get(position), this.mOnClickDoRecipeListener, this.mOnLikeRecipeListener,
                this.mOnSaveRecipeListener);
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
        private final LottieAnimationView mCardCorazon;
        private boolean likeAnim;
        private final LottieAnimationView mCardSaved;
        private boolean savedAnim= false;
        private long  time=0;
        boolean firstTouch= false;

        private GestureDetector taps ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mCardCorazon = itemView.findViewById(R.id.lottieLike);
            this.mCardSaved = itemView.findViewById(R.id.lottieSaved);
            this.mrecipeName = itemView.findViewById(R.id.nombreDetailReceta);
            this.mCardNumberLikes = itemView.findViewById(R.id.likesDetailRecipe);
            this.mCardDescription = itemView.findViewById(R.id.decriptionDetailReceta);
            this.mCardUserPictureURL = itemView.findViewById(R.id.pictureUserReceta);
            this.mCardRecetaPictureUrl = itemView.findViewById(R.id.pictureDetailReceta);
        }

        public void likeAnimMotion(OnLikeRecipeUser listener, Recipe recipe){
            int like= Integer.parseInt(mCardNumberLikes.getText().toString());
            if(likeAnim){
                mCardCorazon.setMinAndMaxProgress(0.5f, 1.0f);
                mCardCorazon.playAnimation();
                likeAnim = ! likeAnim;
                like-=1;
            }else{
                mCardCorazon.setMinAndMaxProgress(0.0f, 0.5f);
                mCardCorazon.playAnimation();
                likeAnim = !likeAnim;
                like+=1;
            }
            mCardNumberLikes.setText(String.valueOf(like));
            listener.OnLikeRecipe(recipe,  likeAnim);
        }

        public void saveAnimMotion(OnClickSaveRecipe listener, Recipe recipe){
            if(savedAnim){
                mCardSaved.setMinAndMaxProgress(0.0f,1.0f);
                mCardSaved.reverseAnimationSpeed();
                mCardSaved.playAnimation();
                savedAnim = !savedAnim;
                Toast.makeText(context, "Se ha borrado de colecciones", Toast.LENGTH_SHORT).show();
            }else{
                mCardSaved.setMinAndMaxProgress(0.0f, 1.0f);
                mCardSaved.reverseAnimationSpeed();
                mCardSaved.playAnimation();
                savedAnim = !savedAnim;
                Toast.makeText(context, "Se ha guardado la receta", Toast.LENGTH_SHORT).show();
            }
            listener.OnClickSave(recipe, savedAnim);

        }

        public void bind(final Recipe recetaUser, ExplorerScrollAdapter.OnClickDoRecipeUser listener,
                         OnLikeRecipeUser listenerLikeRecipe, OnClickSaveRecipe saveListener) {

            likeAnim = recetaUser.getLikeFromUser();
            //Si esta receta esta en la colección de guardados del usuario, hay que cargar la animación
            if( UserRepository.getUser().getIdCollections().get(recetaUser.getNombre()) != null)
                savedAnim=true;

            if(likeAnim) {
                mCardCorazon.setMinAndMaxProgress(0.0f, 0.5f);
                mCardCorazon.playAnimation();
            }
            if(savedAnim) {
                mCardSaved.setMinAndMaxProgress(0.0f, 1.0f);
                mCardSaved.playAnimation();
            }
            mCardCorazon.setOnClickListener(v->{
                likeAnimMotion(listenerLikeRecipe, recetaUser);
            });
            mCardSaved.setOnClickListener(v->{
                saveAnimMotion(saveListener, recetaUser);
            });

            mrecipeName.setText( recetaUser.getNombre());
            mCardNumberLikes.setText(Integer.toString( recetaUser.getLikes()));
            mCardDescription.setText(UserRepository.getUser().getUserName() +"  "+ recetaUser.getDescription());

            cargarPhotoUserAndRecipe(recetaUser);
            mrecipeName.setOnClickListener(view-> { listener.OnClickDoRecipe(recetaUser); });

            imageLikeAnimation(listenerLikeRecipe, recetaUser);
        }

        public void imageLikeAnimation(ExplorerScrollAdapter.OnLikeRecipeUser listener, Recipe recetaUser){
            mCardRecetaPictureUrl.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == event.ACTION_DOWN){
                        if(firstTouch && ( System.currentTimeMillis() - time) <= 300) {
                            //do stuff here for double tap
                            Log.e("** DOUBLE TAP**"," second tap ");
                            firstTouch = false;
                            likeAnimMotion(listener, recetaUser);
                        } else {
                            firstTouch = true;
                            time = System.currentTimeMillis();
                            Log.e("** SINGLE  TAP**"," First Tap time  "+time);
                            return false;
                        }
                    }
                    return true;
                }
            });
        }

        private void cargarPhotoUserAndRecipe(Recipe recetaUser) {

            //Es carrega l'imatge de la receta i del User d'internet
            Picasso.get().load(recetaUser.getPictureURL())
                    .resize(980, 800)
                    .centerCrop().into(mCardRecetaPictureUrl);

            if( ! recetaUser.getUserPhoto().equals(" "))
            Picasso.get().load(recetaUser.getUserPhoto())
                    .resize(200, 200)
                    .centerCrop().into(mCardUserPictureURL);
            else
                mCardUserPictureURL.setImageResource(R.mipmap.ic_launcher_foreground);

        }

    }
    public abstract class DoubleClickListener implements View.OnClickListener {

        private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

        long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                onDoubleClick(v);
                lastClickTime = 0;
            } else {
                onSingleClick(v);
            }
            lastClickTime = clickTime;
        }

        public abstract void onSingleClick(View v);
        public abstract void onDoubleClick(View v);
    }
}