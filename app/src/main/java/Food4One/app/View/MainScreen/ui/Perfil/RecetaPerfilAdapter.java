package com.example.apptry.ui.perfil.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptry.R;
import com.example.apptry.ui.perfil.model.RecetaUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecetaPerfilAdapter extends RecyclerView.Adapter<RecetaPerfilAdapter.ViewHolder> {

    /**
     * Definició de listener (interficie)
     * per a quan algú vulgui escoltar un event de OnClickHide, és a dir,
     * quan l'usuari faci clic en la creu (amagar) algún dels items de la RecyclerView
     */
    public interface OnClickDetailListener {
        void OnClickDetail(int position);
    }

    private ArrayList<RecetaUser> mRecetes; // Referència a la llista de recetes
    private OnClickDetailListener mOnClickHideListener; // Qui hagi de repintar la ReciclerView

    // quan s'amagui
    // Constructor
    public RecetaPerfilAdapter(ArrayList<RecetaUser> recetaList) {
        this.mRecetes = recetaList; // no fa new (La llista la manté el ViewModel)

    }

    public void setOnClickDetailListener(OnClickDetailListener listener) {
        this.mOnClickHideListener = listener;
    }

    @NonNull
    @Override
    public RecetaPerfilAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate crea una view genèrica definida pel layout que l'hi passem (l'user_card_layout)
        View view = inflater.inflate(R.layout.recetaview_card, parent, false);

        // La classe ViewHolder farà de pont entre la classe User del model i la view (UserCard).
        return new RecetaPerfilAdapter.ViewHolder(view);
    }

    /* Mètode cridat per cada ViewHolder de la RecyclerView */
    @Override
    public void onBindViewHolder(@NonNull RecetaPerfilAdapter.ViewHolder holder, int position) {
        // El ViewHolder té el mètode que s'encarrega de llegir els atributs del User (1r parametre),
        // i assignar-los a les variables del ViewHolder.
        // Qualsevol listener que volguem posar a un item, ha d'entrar com a paràmetre extra (2n).
        holder.bind(mRecetes.get(position), this.mOnClickHideListener);
        //Aquí básicamente pasamos por parámetro los listeners de los botones que estarán en el CARD
    }

    /**
     * Retorna el número d'elements a la llista.
     *
     * @return
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
    public void setRecetes(ArrayList<RecetaUser> recetes) {
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
            private final ImageView mCardRecetaPictureUrl;
            private final TextView mCardNumberLikes;

            private final ImageView mCorazon;
            CardView card;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                this.mCardRecetaPictureUrl = itemView.findViewById(R.id.picturerecetaPerfil);
                this.mCardNumberLikes = itemView.findViewById(R.id.likesPicture);
                this.mCorazon = itemView.findViewById(R.id.corazonCard);

                card  = itemView.findViewById(R.id.cardRecetas);


            }

            public void bind(final RecetaUser recetaUser, OnClickDetailListener listener) {

                //mCorazon.setVisibility(View.VISIBLE);
                mCorazon.setImageResource(R.drawable.heart_24);

                mCardNumberLikes.setText( Integer.toString(recetaUser.getLikes()) );
                // Carrega foto de l'usuari de la llista directament des d'una Url
                // d'Internet.
                Picasso.get().load(recetaUser.getPictureURL())
                        .resize(200, 200)
                        .centerCrop().into(mCardRecetaPictureUrl);
                // Seteja el listener onClick del botó d'amagar (hide), que alhora
                // cridi el mètode OnClickHide que implementen els nostres propis
                // listeners de tipus OnClickHideListener.

                mCardRecetaPictureUrl.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       listener.OnClickDetail(getAdapterPosition());
                   }
                });
            }
        }

    }
