package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import Food4One.app.R;
import Food4One.app.databinding.FragmentShoppingListBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment {

    public static final String TAG = "ShoppingListFragment";
    private TextView BtnSaved;
    private TextView BtnList;
    private FragmentShoppingListBinding binding;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ShoppingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingListFragment newInstance() {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        BtnList = getActivity().findViewById(R.id.BtnList);
        BtnSaved = getActivity().findViewById(R.id.BtnSaved);

        clickListenerObjectsView();

        return root;
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