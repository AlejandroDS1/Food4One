package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

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
    private FragmentShoppingListBinding binding;
    private ShoppingListViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = ShoppingListViewModel.getInstance();
        initAdapterList();

        initView();
        //Al precionar Back en este fragment, hay que regresar al fragment del Tab
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setReorderingAllowed(true); //Permet tirar enrere
                fragmentTransaction.replace(R.id.containerFragmentCollection, new AllListsFragment()).commit();

                UserRepository.getInstance().setUserCheckedListDDB( viewModel.getMapAllLists_toDDBB(),
                        viewModel.getCheckedItemsList().getValue(), viewModel.getUnCheckedItemsList().getValue() );
            }
        });
        return root;
    }

    private void initView() {
        binding.listaNameShoppingList.setText(viewModel.getCheckedItemsList().getValue().getListName());
    }

    private void initAdapterList(){

        // Creamos los adapters y los asginamos a los recyclersViews
        binding.checkedItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.checkedItems.setAdapter(new ShoppingListAdapter(viewModel.getCheckedItemsList().getValue(), viewModel));

        binding.UnCheckedItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.UnCheckedItems.setAdapter(new ShoppingListAdapter(viewModel.getUnCheckedItemsList().getValue() , viewModel)
                                                                .setSecondList((ShoppingListAdapter) binding.checkedItems.getAdapter()));
    }
}