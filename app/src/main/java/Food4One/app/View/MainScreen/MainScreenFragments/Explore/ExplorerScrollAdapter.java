package Food4One.app.View.MainScreen.MainScreenFragments.Explore;

import android.content.Context;
import android.content.Intent;
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
import Food4One.app.View.MainScreen.MainScreenFragments.home.DoRecipeActivity;

public class ExplorerScrollAdapter extends RecyclerView.Adapter<ExplorerScrollAdapter.ViewHolder> {

    private ExploreViewModel explorerViewModel;

    private ArrayList<Recipe> mRecetes; // Referència a la llista de recetes


    public ExplorerScrollAdapter(ArrayList<Recipe> recetaList, ExploreViewModel viewModel) {
        this.mRecetes = recetaList; // no fa new (La llista la manté el ViewModel)
        this.explorerViewModel = viewModel;
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
        holder.bind(mRecetes.get(position));
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mrecipeName;
        private final ImageView mCardUserPictureURL;
        protected final ImageView mCardRecetaPictureUrl;
        private final TextView mCardNumberLikes;
        private final TextView mCardDescription;
        protected final LottieAnimationView mCardCorazon;
        private boolean likeAnim;
        private final LottieAnimationView mCardSaved;
        private boolean savedAnim;
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

        public void likeAnimMotion(Recipe recipe){
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

            //Debemos cambiar el like en la base de datos...
            explorerViewModel.onClickLikeRecipe(recipe, likeAnim);
        }

        public void saveAnimMotion( Recipe recipe){
            if(savedAnim){
                if(mCardSaved.getSpeed() > 0)
                    mCardSaved.reverseAnimationSpeed();
                mCardSaved.setProgress(1.0f);
                mCardSaved.playAnimation();
                savedAnim = ! savedAnim;
                Toast.makeText(itemView.getContext(), "Se ha borrado de colecciones", Toast.LENGTH_SHORT).show();
            }else{
                if(mCardSaved.getSpeed() < 0)
                    mCardSaved.reverseAnimationSpeed();
                mCardSaved.setProgress(1.0f);
                mCardSaved.playAnimation();
                savedAnim = ! savedAnim;
                Toast.makeText(itemView.getContext(), "Se ha guardado la receta", Toast.LENGTH_SHORT).show();
            }

            //Se notifica el cambio de guardado de la receta
            explorerViewModel.onClickSaveRecipe(recipe, savedAnim);

        }

        public void bind(final Recipe recetaUser) {

            likeAnim = recetaUser.getLikeFromUser();
            //Si esta receta esta en la colección de guardados del usuario, hay que cargar la animación

            if(UserRepository.getUser().getIdCollections().get(recetaUser.getNombre()) != null)
                savedAnim = UserRepository.getUser().getIdCollections().get(recetaUser.getNombre());
            else
                savedAnim = false;

            if(likeAnim) {
                mCardCorazon.setMinAndMaxProgress(0.0f, 0.5f);
                mCardCorazon.playAnimation();
            }
            if(savedAnim) {
                if(mCardSaved.getSpeed() < 0)
                    mCardSaved.reverseAnimationSpeed();
                mCardSaved.setProgress(1.0f);
                mCardSaved.playAnimation();
            }else {
                if(mCardSaved.getSpeed() > 0)
                    mCardSaved.reverseAnimationSpeed();
                mCardSaved.setProgress(1.0f);
                mCardSaved.playAnimation();
            }

            mCardCorazon.setOnClickListener(v->{
                likeAnimMotion(recetaUser);
            });
            mCardSaved.setOnClickListener(v->{
                saveAnimMotion(recetaUser);
            });

            mrecipeName.setOnClickListener(view-> {
                //Cargamos la receta que queremos ver y luego empezamos la actividad.
                loadDoRecipe(recetaUser);
            });

            mrecipeName.setText( recetaUser.getNombre());
            mCardNumberLikes.setText(Integer.toString( recetaUser.getLikes()));
            mCardDescription.setText("Description  "+ recetaUser.getDescription());

            cargarPhotoUserAndRecipe(recetaUser);

            pictureLikeAndDoRecipe(recetaUser);

        }

        private void pictureLikeAndDoRecipe(Recipe recipe) {
            mCardRecetaPictureUrl.setOnTouchListener(new View.OnTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(@NonNull MotionEvent e) {
                        Toast.makeText(itemView.getContext(), "Damos LIke", Toast.LENGTH_SHORT).show();
                        likeAnimMotion(recipe);
                        return super.onDoubleTap(e);
                    }
                    @Override
                    public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
                        Toast.makeText(itemView.getContext(), "Single Tap Abrimos", Toast.LENGTH_SHORT).show();
                        loadDoRecipe(recipe);
                        return super.onSingleTapConfirmed(e);
                    }
                });
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    gestureDetector.onTouchEvent(motionEvent);
                    return true;
                }
            });
        }

        public void imageLikeAnimation(Recipe recetaUser){
            mCardRecetaPictureUrl.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == event.ACTION_DOWN){
                        if(firstTouch && ( System.currentTimeMillis() - time) <= 300) {
                            //do stuff here for double tap
                            Log.e("** DOUBLE TAP**"," second tap ");
                            firstTouch = false;
                            likeAnimMotion(recetaUser);
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
                    .resize(980, 1000)
                    .centerCrop().into(mCardRecetaPictureUrl);

            if(recetaUser.getUserPhoto() != null) {
                if (!recetaUser.getUserPhoto().equals(" "))
                    Picasso.get().load(recetaUser.getUserPhoto())
                            .resize(200, 200)
                            .centerCrop().into(mCardUserPictureURL);
                else
                    mCardUserPictureURL.setImageResource(R.mipmap.ic_launcher_foreground);
            } else
                mCardUserPictureURL.setImageResource(R.mipmap.ic_launcher_foreground);

        }

        void loadDoRecipe(Recipe recetaUser){
            explorerViewModel.onClikDoRecipe(recetaUser);
            itemView.getContext().startActivity(new Intent(itemView.getContext(), DoRecipeActivity.class));}

    }
}