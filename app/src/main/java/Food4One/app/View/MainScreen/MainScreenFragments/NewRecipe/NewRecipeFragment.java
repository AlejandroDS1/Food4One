package Food4One.app.View.MainScreen.MainScreenFragments.NewRecipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import Food4One.app.databinding.FragmentNewRecipeBinding;

public class NewRecipeFragment extends Fragment {
    private FragmentNewRecipeBinding binding;
    private NewRecipeViewModel newRecipeViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        newRecipeViewModel = new ViewModelProvider(this).get(NewRecipeViewModel.class);

        binding = FragmentNewRecipeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    private void initListeners(){

        // Listener

    }
}