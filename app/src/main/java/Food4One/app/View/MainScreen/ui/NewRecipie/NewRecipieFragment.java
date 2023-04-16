package Food4One.app.View.MainScreen.ui.NewRecipie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import Food4One.app.databinding.FragmentNewRecipieBinding;

public class NewRecipieFragment extends Fragment {

    private FragmentNewRecipieBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewRecipieViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NewRecipieViewModel.class);

        binding = FragmentNewRecipieBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}