package com.example.apptry.ui.perfil.model;


import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

/** Classe que fa d'adaptador entre la base de dades (Cloud Firestore) i les classes del model
 * Segueix el patró de disseny Singleton.
 */
public class RecetaUserRepository {

    private static final String TAG = "Repository";

    /** Autoinstància, pel patró singleton */
    private static final RecetaUserRepository mInstance = new RecetaUserRepository();

    /** Referència a la Base de Dades */
    private FirebaseFirestore mDb;


//----------------------------------------------------------------------------------------------
    /** Definició de listener (interficie),
     *  per escoltar quan s'hagin acabat de llegir les recetes de la BBDD */
    public interface OnLoadRecetaListener {
        void onLoadRecetas(ArrayList<RecetaUser> recetas);
    }
    public ArrayList<OnLoadRecetaListener> mOnloadRecetaListeners = new ArrayList<>();
//-----------------------------------------------------------------------------------------------

    /** Definició de listener (interficie)
     * per poder escoltar quan s'hagi acabat de llegir la Url de la foto de perfil
     * d'un usuari concret */
    public interface OnLoadRecetaPictureUrlListener {
        void OnLoadRecetaPictureUrl(String pictureUrl);
    }

    public OnLoadRecetaPictureUrlListener mOnLoadRecetaPictureUrlListener;
//-------------------------------------------------------------------------------------------------

    /**
     * Constructor privat per a forçar la instanciació amb getInstance(),
     * com marca el patró de disseny Singleton class
     */

    private RecetaUserRepository() {
        mDb = FirebaseFirestore.getInstance();
    }

    /**
     * Retorna aqusta instancia singleton
     * @return
     */
    public static RecetaUserRepository getInstance() {
        return mInstance;
    }

//-------------------------------------------------------------------------------------------------
    /**
     * Afegir un listener de la operació OnLoadRecetaListener.
     * Pot haver-n'hi només un. Fem llista, com a exemple, per demostrar la flexibilitat
     * d'aquest disseny.
     * @param listener
     */
    public void addOnLoadRecetaListener(OnLoadRecetaListener listener) {
        mOnloadRecetaListeners.add(listener);
    }
//-------------------------------------------------------------------------------------------------
    /**
     * Setejem un listener de la operació OnLoadUserPictureUrlListener.
     * En aquest cas, no és una llista de listeners. Només deixem haver-n'hi un,
     * també a tall d'exemple.
     * @param listener
     */
    public void setOnLoadUserPictureListener(OnLoadRecetaPictureUrlListener listener) {
        mOnLoadRecetaPictureUrlListener = listener;
    }


    /**
     * Mètode que llegeix les recetes. Vindrà cridat des de fora i quan acabi,
     * avisarà sempre als listeners, invocant el seu OnLoadReceta.
     */
    public void loadRecetasUser(ArrayList<RecetaUser> recetaUsers, ArrayList<String> idRecetasUser) {
        recetaUsers.clear();
        //Se cargan todas las recetas de la base de datos...

        Iterator iterator = idRecetasUser.iterator();
        while(iterator.hasNext()) {

            String idReceta = (String) iterator.next();
            mDb.collection("Recetas").document(idReceta).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    RecetaUser recetaUser = documentSnapshot.toObject(RecetaUser.class);
                    recetaUsers.add(recetaUser);
                    /*Cuando se consigan todas las recetas del usuario, se llaman a los listeners
                    para que puedan cargar las recetas al RecycleView*/
                    if(recetaUsers.size() == idRecetasUser.size()) onLoadRecetasListenerMethod();
                }

                private void onLoadRecetasListenerMethod() {
                    /*Llamamos a sus listeners*/
                    for (OnLoadRecetaListener l : mOnloadRecetaListeners)
                        l.onLoadRecetas(recetaUsers);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.toString());
                }
            });
        }
    }

    public void loadRecetas(ArrayList<RecetaUser> recetaUsers) {
        recetaUsers.clear();

        //Se cargan todas las recetas de la base de datos...

        mDb.collection("Recetas").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document: queryDocumentSnapshots) {

                    Log.d(TAG, document.getId() + " => " + document.getData());
                    RecetaUser recetaUser = new RecetaUser(document.getId(),
                            document.getString("pictureURL"),
                            Integer.parseInt(document.getString("Likes"))
                    );
                    recetaUsers.add(recetaUser);
                }
                /*Luego llamamos a sus listeners*/
                for (OnLoadRecetaListener l: mOnloadRecetaListeners){
                    l.onLoadRecetas(recetaUsers);
                }
            }
        });
    }

    public void loadPictureOfReceta(){
       // mOnLoadRecetaPictureUrlListener.OnLoadUserPictureUrl();
    }


}