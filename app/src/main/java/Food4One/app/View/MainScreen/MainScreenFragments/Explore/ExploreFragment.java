package Food4One.app.View.MainScreen.MainScreenFragments.Explore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;
import Food4One.app.Model.Recipe.Recipe.RecipesUserApp;
import Food4One.app.Model.User.UserRepository;
import Food4One.app.R;
import Food4One.app.View.MainScreen.MainScreenFragments.home.DoRecipeActivity;
import Food4One.app.View.MainScreen.MainScreenFragments.home.HomeViewModel;
import Food4One.app.databinding.FragmentExploreBinding;

public class ExploreFragment extends Fragment {

    private ExploreViewModel mViewModel;
    private static RecyclerView recyclerViewExplorer;
    private FragmentExploreBinding binding;
    private ExplorerScrollAdapter adapter;
    public ExplorerScrollAdapter getAdapter() {
        return adapter;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);

        binding = FragmentExploreBinding.inflate(inflater, container, false);
        mViewModel = ExploreViewModel.getInstance();

        userPictureObserver();
        recicleInit();
        recetasObserver();

        mViewModel.setmRecetas(RecipesUserApp.getRecetasExplorer());
        return binding.getRoot();
    }


    private void recicleInit() {
        adapter = new ExplorerScrollAdapter(mViewModel.getRecetas().getValue());
        adapter.setContext(getContext());

        clickListenersAdapter();

        recyclerViewExplorer = binding.explorerRecycleView;
        //Ahora le definimos un Manager Recycle View. (Deslizar verticalmente el Recycle)
        LinearLayoutManager manager = new LinearLayoutManager
                (this.getContext(), LinearLayoutManager.VERTICAL, true);

        recyclerViewExplorer.setLayoutManager(manager);
        recyclerViewExplorer.setAdapter(adapter);
    }

    private void clickListenersAdapter() {
        adapter.setOnClickDetailListener(new ExplorerScrollAdapter.OnClickDoRecipeUser() {
            @Override
            public void OnClickDoRecipe(Recipe recipe) {
                HomeViewModel.getInstance().loadRecipeToMake(recipe);
                startActivity(new Intent(getContext(), DoRecipeActivity.class));
            }
        });

        adapter.setOnLikeRecipeListener(new ExplorerScrollAdapter.OnLikeRecipeUser() {
            @Override
            public void OnLikeRecipe(Recipe recipe, boolean like) {
                UserRepository.getInstance().setUserLikeDDB(recipe,like );
            }
        });
        adapter.setOnClickSaveListener(new ExplorerScrollAdapter.OnClickSaveRecipe() {
            @Override
            public void OnClickSave(Recipe recipe, boolean saved) {
                UserRepository.getInstance().setUserRecetaCollectionDDB(recipe, saved);
            }
        });
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