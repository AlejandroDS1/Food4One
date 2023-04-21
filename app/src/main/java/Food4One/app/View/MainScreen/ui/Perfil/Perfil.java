package Food4One.app.View.MainScreen.ui.Perfil;

import static android.content.ContentValues.TAG;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import Food4One.app.R;
import Food4One.app.databinding.FragmentPerfilBinding;

public class Perfil extends Fragment {

    private FragmentPerfilBinding binding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    // TODO: Pasamos objeto user
    //private DocumentReference userInformation = FirebaseFirestore.getInstance()
            //.document("Users/"+auth.getCurrentUser().getEmail());

    //Para guardar la información del Usuario en el perfil

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        PerfilViewModel perfilViewModel =
                new ViewModelProvider(this).get(PerfilViewModel.class);


        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // perfilViewModel.cargarWindowProfile(userInformation, binding);

        //Se carga los procesos que realiza el fragmento...
        binding.logoutButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perfilViewModel.logoutButton(binding);
            }
        });

        binding.editarPerfilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perfilViewModel.editarButton(binding);
                //Toast.makeText(getContext(), "Unimplemented", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getActivity(), EditarPerfilActivity.class));
            }
        });

        final Observer<String> observerPerfil = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //perfilViewModel.cargarWindowProfile(userInformation, binding);
            }
        };

        perfilViewModel.getText().observe(this.getViewLifecycleOwner(), observerPerfil );
        return root;
    }

    public void cargarWindowProfile(DocumentReference userInformation){

        binding.progressBarPerfil.getIndeterminateDrawable()
                .setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);

        //Debo obtener la información del usuario del Cloud FireBase...

        userInformation.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            //Como parámetro recibe el Documento donde debería estar la información del usuario, si es que existe
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    binding.nomusuari.setText(documentSnapshot.getString("Name"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding Document", e);
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        binding.progressBarPerfil.setVisibility(View.VISIBLE);
        //cargarWindowProfile(userInformation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume(){
        super.onResume();

        try{
            Thread.sleep(600);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        binding.progressBarPerfil.setVisibility(View.GONE);

    }


}