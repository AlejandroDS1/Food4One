package Food4One.app.View.MainScreen.ui.Perfil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import Food4One.app.Model.Recipie.Recipie.Recipe;
import Food4One.app.Model.Recipie.Recipie.RecipesUserApp;
import Food4One.app.R;
import Food4One.app.databinding.FragmentScrollPerfilBinding;

public class ScrollPerfil extends Fragment {

    private FragmentScrollPerfilBinding binding;
    private PerfilViewModel perfilViewModel;
    private RecyclerView mRecetaCardsRV;
    private ScrollPerfilAdapter mCardRecetaRVAdapter;
    private TextView backBottom;
    private int focusSelection;

    public ScrollPerfil() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScrollPerfilBinding.inflate(inflater, container, false);

        perfilViewModel = new ViewModelProvider(requireActivity()).get(PerfilViewModel.class);

        inicioObjectsView();

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void inicioObjectsView() {
        mRecetaCardsRV = binding.scrollrecipesPerfil;
        backBottom = binding.backflecha;

        //Ahora le definimos un Manager Recycle View. (Deslizar verticalmente el Recycle)
        LinearLayoutManager manager = new LinearLayoutManager
                (this.getContext(), LinearLayoutManager.VERTICAL, true);

        mRecetaCardsRV.setLayoutManager(manager);
        //Obtenemos el elemento que se ha seleccionado en el Grid del Perfil
        focusSelection = requireArguments().getInt("RecycleViewPosition");


        backBottom.setOnClickListener(v-> { //Regresamos manualmente al Fragment anterior
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Tenemos que esperar a que se cargue el recycle View con la info del Fragment anterior
        postponeEnterTransition();
        ArrayList<Recipe> recetasGrid = (ArrayList<Recipe>) RecipesUserApp.getInstance().clone();
        //Colocamos todas las recetas del Usuario al perfilViewModel
        perfilViewModel.setRecetes(recetasGrid);

        final ViewGroup parentView = (ViewGroup) view.getParent();
        // Wait for the data to load
        perfilViewModel.getRecetes()
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<Recipe>>() {
                    @Override
                    public void onChanged(ArrayList<Recipe> recipes) {

                        //Hay que invertir el  orden de las recetas, ya que el recycle view los
                        //va añadiendo como una pila, y no como un array
                        Collections.reverse(perfilViewModel.getRecetes().getValue());

                        // Set the data on the RecyclerView adapter
                        //Instanciamos el Adapter de las fotos como el nuevo diseño con detalles

                        mCardRecetaRVAdapter = new ScrollPerfilAdapter(
                                perfilViewModel.getRecetes().getValue());

                        mRecetaCardsRV.setAdapter(mCardRecetaRVAdapter);

                        // Start the transition once all views have been
                        // measured and laid out
                        parentView.getViewTreeObserver()
                                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                    @Override
                                    public boolean onPreDraw(){
                                        mRecetaCardsRV.setFocusable(focusSelection);
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