package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Ingredients.Ingrediente;
import Food4One.app.Model.Recipe.Ingredients.IngredientesList;
import Food4One.app.Model.User.UserRepository;
import Food4One.app.R;
import Food4One.app.databinding.FragmentShoppingListBinding;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ShoppingListFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class ShoppingListFragment extends Fragment {

    public static final String TAG = "ShoppingListFragment";
    private TextView BtnSaved;
    private TextView BtnList;
    private FragmentShoppingListBinding binding;

//    public ShoppingListFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @return A new instance of fragment ShoppingListFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ShoppingListFragment newInstance() {
//        ShoppingListFragment fragment = new ShoppingListFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

    private ShoppingListViewModel shoppingListViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);

        BtnList = getActivity().findViewById(R.id.BtnList);
        BtnSaved = getActivity().findViewById(R.id.BtnSaved);

        clickListenerObjectsView();
        initPruebaLista();

        return root;
    }

    private void initPruebaLista(){

        ArrayList<Ingrediente> in = new ArrayList<>();
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Mi puta madre|300|Gr|1"));
        in.add(new Ingrediente("La tuya|200|Gr|1"));
        in.add(new Ingrediente("Ahora la mia|200|Gr|1"));
        in.add(new Ingrediente("Tonto quien lo lea|200|Gr|1"));
        in.add(new Ingrediente("Un litro de gilipollas|1|L|1"));        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));
        in.add(new Ingrediente("Arroz con patatas|200|Gr|1"));


        IngredientesList ingredientesList = new IngredientesList(in);

        UserRepository.getInstance().loadUserIngredientesList(UserRepository.getUser().getEmail(), ingredientesList);

        ShoppingListAdapter listAdapter = new ShoppingListAdapter(ingredientesList);

        binding.shoopingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.shoopingRecyclerView.setAdapter(listAdapter);
    }

    private void clickListenerObjectsView() {
        BtnSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BtnSaved.setElevation(15);
                Drawable myDrawable1 = ContextCompat.getDrawable(getContext(), R.drawable.botonback);
                BtnSaved.setBackground(myDrawable1);

                BtnList.setElevation(0);
                Drawable myDrawable2 = ContextCompat.getDrawable(getContext(), R.drawable.greybutton);
                BtnList.setBackground(myDrawable2);

                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
    }
}