package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Food4One.app.R;
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
                        changeFragment = new AllListsFragment();
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