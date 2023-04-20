package Food4One.app.View.MainScreen.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Food4One.app.R;
import Food4One.app.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    ArrayList<ListRecipes> recetasHome = new ArrayList<>();

    private FragmentHomeBinding binding;
    private RecyclerView viewRV;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewRV = binding.recycleHomeRecetas;
        setupModelRecetasHome();

        ListRecipesAdapter adapter = new ListRecipesAdapter(getContext(), recetasHome);
        viewRV.setAdapter(adapter);
        viewRV.setLayoutManager(new LinearLayoutManager(getContext()));


        return root;
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