package Food4One.app.View.MainScreen.MainScreenFragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Food4One.app.R;
import Food4One.app.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    ArrayList<ListRecipes> recetasHome = new ArrayList<>();
    private FragmentHomeBinding binding;
    private RecyclerView viewRV;
    private HomeViewModel homeViewModel ;
    ListRecipesAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =HomeViewModel.getInstance();
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Recetas principales de la App /BARB/PAST/BEBID/ARROZ...
        setupModelRecetasHome();

        viewRV = binding.recycleHomeRecetas;
        viewRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = new ListRecipesAdapter(getContext(), recetasHome);
        viewRV.setAdapter(adapter);
        agregarListener();

        return root;
    }

    private void agregarListener() {

        adapter.setmOnClickListenerHomeSelection(new ListRecipesAdapter.OnClickListenerHomeSelection() {
            @Override
            public void onClickHomeSelection(String position) {
                openSelectionHome(position);
            }
        });
    }

    private void openSelectionHome(String seleccion) {

        if (seleccion.equalsIgnoreCase("Surprise me")){
            startActivity(new Intent(getContext(), RotateActivity.class));
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("HomeSelection", seleccion);
        // Create new fragment and transaction
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true)
                .addToBackStack("HomeFragChange") ;

//        homeViewModel.loadRecetasApp(seleccion);

        //Remplazamos el fragmento del Home con la Selección que se ha cargado
        //También le pasamos al nuevo fragmento la selección para que sepa su camino por BBD
        transaction.replace(R.id.homeViewLayout, RecipeVarietyFragment.class, bundle);
        // Commit the transaction
        transaction.commit();
    }

    public void setupModelRecetasHome(){
        String [] recetasName = getResources().getStringArray(R.array.nombreRecetasHome);
        int [] recetasImage = {R.drawable.barbacoa_photo, R.drawable.pasta_image,R.drawable.bocatas_photo, R.drawable.arroz_photo,
        R.drawable.cakes_photo, R.drawable.bebidas_photo, R.drawable.surpriseme};
        for (int i=0; i < recetasName.length; i++){
            recetasHome.add(new ListRecipes(recetasName[i], recetasImage[i]));
        }
    }
}