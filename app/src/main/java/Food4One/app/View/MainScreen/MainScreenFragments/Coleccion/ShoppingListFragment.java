package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

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
    private ShoppingListAdapter shoppingListAdapter;
    private IngredientesList ingredientesList;

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

        initAdapterList();
        initLayout();

        clickListenerObjectsView();

        shoppingListAdapter.notifyDataSetChanged();
        return root;
    }

    private void initLayout() {
        // Iniciamos los observers
        final Observer<IngredientesList> ingredientesListObserver = new Observer<IngredientesList>() {
            @Override
            public void onChanged(IngredientesList ingredientesList) {
                shoppingListAdapter.setList(ingredientesList);
            }
        };
        shoppingListViewModel.getIngredientesList().observe(this.getActivity(), ingredientesListObserver);
    }


    private void initAdapterList(){

        UserRepository.getInstance().loadUserIngredientesList(UserRepository.getUser().getEmail());

        shoppingListAdapter = new ShoppingListAdapter(shoppingListViewModel.getIngredientesList().getValue());

        binding.shoopingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.shoopingRecyclerView.setAdapter(shoppingListAdapter);
    }

    private void clickListenerObjectsView() {
        BtnSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mirem si el fragment encara està en l'activitat
                if (isAdded()) {
                    BtnSaved.setElevation(15);
                    Drawable myDrawable1 = ContextCompat.getDrawable(getContext(), R.drawable.botonback);
                    BtnSaved.setBackground(myDrawable1);

                    BtnList.setElevation(0);
                    Drawable myDrawable2 = ContextCompat.getDrawable(getContext(), R.drawable.greybutton);
                    BtnList.setBackground(myDrawable2);

                    // POP_BACK_STACK_INCLUSIVE es fa servir per eliminar el mateix fragment i els que estiguin per sobre en el back stack
                    getActivity().getSupportFragmentManager().popBackStack(ShoppingListFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                }

            }
        });
    }
}