package Food4One.app.View.MainScreen.MainScreenFragments.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Food4One.app.databinding.FragmentRecipeVarietyBinding;

public class RecipeVariety extends Fragment {
    private FragmentRecipeVarietyBinding binding;
    private HomeViewModel homeViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=  FragmentRecipeVarietyBinding.inflate(inflater, container, false);

        homeViewModel = HomeViewModel.getInstance();

       // homeViewModel.loadRecetasApp(requireArguments().getString("Selection"));

        return binding.getRoot();
    }
}