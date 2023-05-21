package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.j2objc.annotations.ObjectiveCName;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.Model.Recipe.Recipe.RecipeList;
import Food4One.app.Model.Recipe.Recipe.RecipeRepository;
import Food4One.app.Model.Recipe.Recipe.RecipesUserApp;
import Food4One.app.Model.User.UserRepository;
import Food4One.app.R;
import Food4One.app.View.MainScreen.MainScreenFragments.home.DoRecipeActivity;
import Food4One.app.View.MainScreen.MainScreenFragments.home.HomeViewModel;
import Food4One.app.databinding.FragmentColeccionBinding;

public class ColeccionFragment extends Fragment {
    public static final String TAG = "ColeccionFragment";
    private FragmentColeccionBinding binding;

    private BottomNavigationView savedListBottom;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentColeccionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        savedListBottom = binding.navegationColeectionFragments;

        Animation animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.recycler_view_left_fadein);
        animation.setDuration(1000);
        savedListBottom.setAnimation(animation);

        //Empezamos viendo la parte de Guardados
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFragmentCollection, new SavedFragment()).commit();

        savedListBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {Fragment changeFragment = null;

                switch (item.getItemId()) {
                    case R.id.tab_saved:
                        changeFragment = new SavedFragment();
                        break;

                    case R.id.tab_list:
                        changeFragment = new ShoppingListFragment();
                        break;
                }
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.containerFragmentCollection, changeFragment).commit();
                return true;
            }
        });

        return root;
    }

}