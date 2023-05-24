package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

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
    private ShoppingListViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = ShoppingListViewModel.getInstance();

        BtnList = getActivity().findViewById(R.id.BtnList);
        BtnSaved = getActivity().findViewById(R.id.BtnSaved);

        initAdapterList();

        initView();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {

            @Override
            public void handleOnBackPressed() {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setReorderingAllowed(true); //Permet tirar enrere
                fragmentTransaction.replace(R.id.containerFragmentCollection, new AllListsFragment()).commit();
            }
        });

        return root;
    }

    private void initView() {
        // Boton para actualizar el firebase
        binding.guardarEnFireBaseShoppingListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewModel.addIngredientesList_toDDBB(((ShoppingListAdapter)binding.checkedItems.getAdapter()).getAllLists_toStore());
            }
        });

        binding.listaNameShoppingList.setText(viewModel.getCheckedItemsList().getValue().getListName());
    }

    private void initAdapterList(){

        // Creamos los adapters y los asginamos a los recyclersViews
        binding.checkedItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.checkedItems.setAdapter(new ShoppingListAdapter(viewModel.getCheckedItemsList().getValue(), viewModel));

        binding.UnCheckedItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.UnCheckedItems.setAdapter(new ShoppingListAdapter(viewModel.getUnCheckedItemsList().getValue(), viewModel)
                                                                .setSecondList((ShoppingListAdapter) binding.checkedItems.getAdapter()));

    }
}