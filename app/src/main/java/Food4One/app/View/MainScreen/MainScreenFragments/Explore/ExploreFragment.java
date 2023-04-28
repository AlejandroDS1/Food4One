package Food4One.app.View.MainScreen.MainScreenFragments.Explore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.R;
import Food4One.app.View.MainScreen.MainScreenFragments.Perfil.PerfilViewModel;
import Food4One.app.View.MainScreen.MainScreenFragments.Perfil.ScrollPerfilAdapter;
import Food4One.app.View.MainScreen.MainScreenFragments.home.DoRecipeActivity;
import Food4One.app.View.MainScreen.MainScreenFragments.home.HomeViewModel;
import Food4One.app.databinding.FragmentExploreBinding;

public class ExploreFragment extends Fragment {

    private ExploreViewModel mViewModel;
    private RecyclerView recyclerViewExplorer;
    private FragmentExploreBinding binding;
    private ExplorerScrollAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);

        binding =  FragmentExploreBinding.inflate(inflater, container, false);
        mViewModel = ExploreViewModel.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final Observer<String> observerURLUser = new Observer<String>() {
            @Override
            public void onChanged(String description) {
                ;
            }
        };
        ExploreViewModel.getInstance().getUserURLFromRecipe().observe(this.getActivity(), observerURLUser);

        adapter = new ExplorerScrollAdapter(mViewModel.getRecetas().getValue());
        adapter.setOnClickDetailListener(new ExplorerScrollAdapter.OnClickDoRecipeUser(){
            @Override
            public void OnClickDoRecipe(Recipe recipe) {
                HomeViewModel.getInstance().loadRecipeToMake(recipe);
                startActivity(new Intent(getContext(), DoRecipeActivity.class));
            }
        });

        recyclerViewExplorer = binding.explorerRecycleView;
        //Ahora le definimos un Manager Recycle View. (Deslizar verticalmente el Recycle)
        LinearLayoutManager manager = new LinearLayoutManager
                (this.getContext(), LinearLayoutManager.VERTICAL, true);

        recyclerViewExplorer.setLayoutManager(manager);
        recyclerViewExplorer.setAdapter(adapter);

        final Observer<ArrayList<Recipe>> observerRecetes= new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recetas) {
                adapter.notifyDataSetChanged();
            }
        };
        mViewModel.getRecetas().observe(this.getViewLifecycleOwner(), observerRecetes);

        mViewModel.loadRecetasExplorer();
    }
}