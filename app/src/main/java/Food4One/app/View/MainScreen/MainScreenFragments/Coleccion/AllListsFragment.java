package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Food4One.app.R;
import Food4One.app.databinding.FragmentAllListsBinding;

public class AllListsFragment extends Fragment {

    public static final String TAG = "AllListsFragment";

    private ShoppingListViewModel viewModel;
    private FragmentAllListsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllListsBinding.inflate(inflater, container, false);

        viewModel = ShoppingListViewModel.getInstance();

        initAdapters();

        return binding.getRoot();
    }

    private void initAdapters() {

        binding.listAllLists.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.setOnChangedListsListener(new ShoppingListViewModel.OnChangedListsListener() {
            @Override
            public void onChangedListListener() {
                binding.listAllLists.setAdapter(new AllListsAdapter(viewModel.getAllListsNames()));
            }
        });
    }

    private final class AllListsAdapter extends RecyclerView.Adapter<AllListsAdapter.ViewHolder> {

        private final List<String> allLists;

        public AllListsAdapter(final List<String> allLists) {
            this.allLists = allLists;
        }

        @NonNull
        @Override
        public AllListsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View view = inflater.inflate(R.layout.item_all_lists_recyclerview, parent, false);

            return new AllListsAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AllListsAdapter.ViewHolder holder, int position) {
             holder.onBind(allLists.get(position));
        }

        @Override
        public int getItemCount() {
            return this.allLists.size();
        }

        private final class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView listName;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.listName = itemView.findViewById(R.id.item_allLists_listName);
            }

            public void onBind(@NonNull final String listaName){

                listName.setText(listaName);

                listName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        viewModel.setChoosedList(listaName);

                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setReorderingAllowed(true).addToBackStack(ShoppingListFragment.TAG); //Permet tirar enrere
                        fragmentTransaction.add(R.id.coleccionFragment, new ShoppingListFragment());
                        fragmentTransaction.commit();
                    }
                });
            }
        }
    }
}