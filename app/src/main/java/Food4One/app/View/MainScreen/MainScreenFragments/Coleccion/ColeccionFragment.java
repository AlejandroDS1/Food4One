package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import Food4One.app.databinding.FragmentColeccionBinding;

public class ColeccionFragment extends Fragment {

    private FragmentColeccionBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ColeccionViewModel coleccionViewModel =
                new ViewModelProvider(this).get(ColeccionViewModel.class);

        binding = FragmentColeccionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        coleccionViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}