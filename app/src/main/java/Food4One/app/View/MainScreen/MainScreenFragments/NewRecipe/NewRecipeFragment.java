package Food4One.app.View.MainScreen.MainScreenFragments.NewRecipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import Food4One.app.databinding.FragmentNewRecipeBinding;

public class NewRecipeFragment extends Fragment {

    private FragmentNewRecipeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewRecipeViewModel newRecipeViewModel =
                new ViewModelProvider(this).get(NewRecipeViewModel.class);

        binding = FragmentNewRecipeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}