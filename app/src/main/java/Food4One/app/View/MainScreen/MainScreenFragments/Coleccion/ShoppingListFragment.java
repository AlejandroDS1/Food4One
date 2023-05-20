package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import Food4One.app.R;
import Food4One.app.databinding.FragmentShoppingListBinding;


public class ShoppingListFragment extends Fragment {

    public static final String TAG = "ShoppingListFragment";
    private TextView BtnSaved;
    private TextView BtnList;
    private FragmentShoppingListBinding binding;
    private ShoppingListViewModel shoppingListViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        shoppingListViewModel.setDao(this);

        BtnList = getActivity().findViewById(R.id.BtnList);
        BtnSaved = getActivity().findViewById(R.id.BtnSaved);

        initAdapterList();

        initListeners();
        clickListenerObjectsView();

        return root;
    }

    private void initListeners() {

        binding.guardarEnFireBaseShoppingListBtn.setOnClickListener(view -> {

            shoppingListViewModel.setListStateFireBase();
        });
    }

    private void initAdapterList(){

        shoppingListViewModel.setOnChangedListListener(new ShoppingListViewModel.OnListChangedListener() {
            @Override
            public void onChangedListener() {

                binding.checkedItems.getAdapter().notifyDataSetChanged();
                binding.UnCheckedItems.getAdapter().notifyDataSetChanged();
            }
        });

//        final Observer<Boolean> observer = new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if (aBoolean){
//                    binding.checkedItems.getAdapter().notifyDataSetChanged();
//                    binding.UnCheckedItems.getAdapter().notifyDataSetChanged();
//                }
//            }
//        };
//        shoppingListViewModel.getCompleted().observe(getViewLifecycleOwner(), observer);


        binding.UnCheckedItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.UnCheckedItems.setAdapter(new ShoppingListAdapter(shoppingListViewModel.getUnCheckedIngredientes().getValue(), this.shoppingListViewModel));

        binding.checkedItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.checkedItems.setAdapter(new ShoppingListAdapter(shoppingListViewModel.getCheckedIngredientes().getValue(), this.shoppingListViewModel).setAsCheckList());

        ((ShoppingListAdapter) binding.checkedItems.getAdapter()).setSecondList(((ShoppingListAdapter)binding.UnCheckedItems.getAdapter())); // Relacionamos los adapters entre si
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