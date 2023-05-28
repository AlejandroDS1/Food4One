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

import java.util.ArrayList;

import Food4One.app.Model.User.UserRepository;
import Food4One.app.R;
import Food4One.app.databinding.FragmentColeccionBinding;

public class ColeccionFragment extends Fragment {

    public static final String TAG = "ColeccionFragment";
    private FragmentColeccionBinding binding;
    private BottomNavigationView savedListBottom;
    private ColeccionViewModel coleccionViewModel;
    private String fragment = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentColeccionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        savedListBottom = binding.navegationColeectionFragments;

        Animation animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.recycler_view_left_fadein);
        animation.setDuration(600);
        savedListBottom.setAnimation(animation);

        //Empezamos viendo la parte de Guardados
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFragmentCollection, new SavedFragment()).commit();

        coleccionViewModel = ColeccionViewModel.getInstance();

        cargarReceptesUsuari();

        savedListBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment changeFragment = null;
                String actualFragment = fragment;

                switch (item.getItemId()) {
                    case R.id.tab_saved:
                        if (!fragment.equals("SavedFrag"))
                            changeFragment = new SavedFragment();
                        fragment = "SavedFrag";
                        break;

                    case R.id.tab_list:
                        if (!fragment.equals("AllList"))
                            changeFragment = new AllListsFragment();
                        fragment = "AllList";
                        break;
                    default:
                        changeFragment = new SavedFragment();
                        fragment = "SavedFrag";
                        break;
                }
                if(! actualFragment.equals(fragment))
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.containerFragmentCollection, changeFragment).commit();
                return true;
            }
        });
        return root;
    }

    private void cargarReceptesUsuari() {
        // A partir d'aquí, en cas que es faci cap canvi a la llista de receptes, ColeccionFragment ho sabrà
        // Internament pobla les receptes de la DDBB
        coleccionViewModel.loadRecetasOfUserFromRepository(new ArrayList<>(UserRepository.getUser().getIdCollections().keySet()));
    }

    @Override
    public void onStart() {
        super.onStart();
        //Al empezar la vista siempre se verá el fragmento de Guardados
        savedListBottom.setSelectedItemId(R.id.tab_saved);
    }
}