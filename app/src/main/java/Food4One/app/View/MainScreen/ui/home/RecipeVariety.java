package Food4One.app.View.MainScreen.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Food4One.app.R;
import Food4One.app.databinding.FragmentRecipeVarietyBinding;
import Food4One.app.databinding.RecetadetailsViewCardBinding;

public class RecipeVariety extends Fragment {
    private FragmentRecipeVarietyBinding binding;
    HomeViewModel homeViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=  FragmentRecipeVarietyBinding.inflate(inflater, container, false);

        homeViewModel = HomeViewModel.getInstance();

        homeViewModel.loadRecetasApp(requireArguments().getString("HomeSelection"));

        return binding.getRoot();
    }
}