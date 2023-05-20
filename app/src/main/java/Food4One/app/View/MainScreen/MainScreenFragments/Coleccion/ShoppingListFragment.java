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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);

        BtnList = getActivity().findViewById(R.id.BtnList);
        BtnSaved = getActivity().findViewById(R.id.BtnSaved);

        initAdapterList();

        clickListenerObjectsView();

        return root;
    }



    private void initAdapterList(){

        // Creamos los adapters y los asginamos a los recyclersViews
        binding.checkedItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.checkedItems.setAdapter(new ShoppingListAdapter(viewModel.getCheckedItemsList().getValue(), viewModel));

        binding.UnCheckedItems.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.UnCheckedItems.setAdapter(new ShoppingListAdapter(viewModel.getUnCheckedItemsList().getValue(), viewModel)
                                                                .setSecondList((ShoppingListAdapter) binding.checkedItems.getAdapter()));

        viewModel.setOnChangedListsListener(new ShoppingListViewModel.OnChangedListsListener() {
            @Override
            public void onChangedListListener() {
                binding.checkedItems.getAdapter().notifyDataSetChanged();
                binding.UnCheckedItems.getAdapter().notifyDataSetChanged();
            }
        });

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