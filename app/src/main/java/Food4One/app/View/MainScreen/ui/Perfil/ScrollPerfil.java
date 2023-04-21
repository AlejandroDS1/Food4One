package Food4One.app.View.MainScreen.ui.Perfil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

import Food4One.app.Model.Recipie.Recipie.Recipe;
import Food4One.app.R;
import Food4One.app.databinding.FragmentScrollPerfilBinding;

public class ScrollPerfil extends Fragment {

    private FragmentScrollPerfilBinding binding;
    private PerfilViewModel perfilViewModel;
    private RecyclerView mRecetaCardsRV;
    private ScrollPerfilAdapter mCardRecetaRVAdapter;

    public ScrollPerfil() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentScrollPerfilBinding.inflate(inflater, container, false);

        perfilViewModel = new ViewModelProvider(requireActivity()).get(PerfilViewModel.class);

        mRecetaCardsRV = binding.scrollrecipesPerfil;

        //Ahora le definimos un Manager Grid
        LinearLayoutManager manager = new LinearLayoutManager
                (this.getContext(), LinearLayoutManager.VERTICAL, true);

        mRecetaCardsRV.setLayoutManager(manager);

        int focusRecycle = requireArguments().getInt("RecycleViewPosition");

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postponeEnterTransition();

        final ViewGroup parentView = (ViewGroup) view.getParent();
        // Wait for the data to load
        perfilViewModel.getRecetes()
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {
                    @Override
                    public void onChanged(ArrayList<Recipe> recipes) {
                        // Set the data on the RecyclerView adapter
                        //Luego instanciamos el Adapter de las fotos
                        mCardRecetaRVAdapter = new ScrollPerfilAdapter(
                                perfilViewModel.getRecetes().getValue());

                        mRecetaCardsRV.setAdapter(mCardRecetaRVAdapter);

                        // Start the transition once all views have been
                        // measured and laid out
                        parentView.getViewTreeObserver()
                                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                    @Override
                                    public boolean onPreDraw(){
                                        parentView.getViewTreeObserver()
                                                .removeOnPreDrawListener(this);
                                        startPostponedEnterTransition();
                                        return true;
                                    }
                        });
                    }
                });


    }
}