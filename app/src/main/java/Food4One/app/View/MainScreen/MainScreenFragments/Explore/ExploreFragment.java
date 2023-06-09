package Food4One.app.View.MainScreen.MainScreenFragments.Explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.databinding.FragmentExploreBinding;

public class ExploreFragment extends Fragment {
    private static RecyclerView recyclerViewExplorer;
    private ExploreViewModel mViewModel;
    private FragmentExploreBinding binding;
    private ExplorerScrollAdapter adapter;
    public ExplorerScrollAdapter getAdapter() {
        return adapter;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = ExploreViewModel.getInstance();

        binding = FragmentExploreBinding.inflate(inflater, container, false);
        mViewModel = ExploreViewModel.getInstance();

        userPictureObserver();
        recicleInit();
        recetasObserver();

        mViewModel.loadRecetasExplorer();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewExplorer.scrollToPosition(0);
    }

    private void recicleInit() {
        adapter = new ExplorerScrollAdapter(mViewModel.getRecetas().getValue(), mViewModel);

        recyclerViewExplorer = binding.explorerRecycleView;
        //Ahora le definimos un Manager Recycle View. (Deslizar verticalmente el Recycle)
        LinearLayoutManager manager = new LinearLayoutManager
                (this.getContext(), LinearLayoutManager.VERTICAL, true);

        recyclerViewExplorer.setLayoutManager(manager);
        recyclerViewExplorer.setAdapter(adapter);
    }

    private void recetasObserver() {

        final Observer<ArrayList<Recipe>> observerRecetes = new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recetas) {

                adapter.notifyDataSetChanged();
            }
        };
        mViewModel.getRecetas().observe(this.getViewLifecycleOwner(), observerRecetes);
    }

    private void userPictureObserver() {
        final Observer<String> observerURLUser = new Observer<String>() {
            @Override
            public void onChanged(String description) {
                ;
            }
        };
        mViewModel.getUserURLFromRecipe().observe(this.getActivity(), observerURLUser);
    }

}