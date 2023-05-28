package Food4One.app.View.MainScreen.MainScreenFragments.Coleccion;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Food4One.app.Model.User.UserRepository;
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
        changeAllListListener();

        return binding.getRoot();
    }

    void changeAllListListener(){
        UserRepository.getInstance().setOnSetListIngredientesListener(new UserRepository.OnSetListIngredientesListener() {
            @Override
            public void onSetListIngredientes(boolean state) {
                if(state)
                    binding.listAllLists.getAdapter().notifyDataSetChanged();
            }
        });

    }

    private void initAdapters() {

        binding.listAllLists.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listAllLists.setAdapter(new AllListsAdapter(viewModel.getAllListsNames()));

        viewModel.setOnChangedListsListener(new ShoppingListViewModel.OnChangedListsListener() {
            @Override
            public void onChangedListListener() {
                binding.emptyListNotifyer.setVisibility(View.GONE);
                binding.listAllLists.setVisibility(View.VISIBLE);
                binding.listAllLists.setAdapter(new AllListsAdapter(viewModel.getAllListsNames()));
            }
        });

        viewModel.setOnListIsEmptyListener(new ShoppingListViewModel.OnListIsEmptyListener() {
            @Override
            public void onListIsEmptyListener() {
                binding.listAllLists.setVisibility(View.GONE);
                binding.emptyListNotifyer.setVisibility(View.VISIBLE);
            }
        });
    }

    public class AllListsAdapter extends RecyclerView.Adapter<AllListsAdapter.ViewHolder> {
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
             holder.onBind(position);
        }
        @Override
        public int getItemCount() {
            return this.allLists.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            protected final TextView listName;
            protected final TextView editBtn;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.listName = itemView.findViewById(R.id.item_allLists_listName);
                this.editBtn = itemView.findViewById(R.id.editBtn_allLists);
            }

            public void onBind(@NonNull final int pos){
                final String listaName = allLists.get(pos);

                listName.setText(listaName);
                listName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        viewModel.setChoosedList(listaName);

                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setReorderingAllowed(true).addToBackStack(ShoppingListFragment.TAG); //Permet tirar enrere
                        fragmentTransaction.add(R.id.containerFragmentCollection, new ShoppingListFragment());
                        fragmentTransaction.commit();
                    }
                });

                editBtn.setOnClickListener(listener -> {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    final View view = View.inflate(getContext(), R.layout.alert_dialog_change_listname, null);

                    final EditText listName_editText = view.findViewById(R.id.listName_editText);

                    listName_editText.setText(listaName);
                    final String PREVIOUS_LISTNAME = listName_editText.getText().toString();

                    builder.setView(view);

                    AlertDialog alert = builder.create();

                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    view.findViewById(R.id.changeListName_icon_allLists).setOnClickListener(uploadListener -> {

                            final int LAST_POSITION = allLists.indexOf(PREVIOUS_LISTNAME);

                            final String newListaName = listName_editText.getText().toString();

                            // Si el nombre de la lista no es valido se lo notificamos al usuario con un toast
                            if (newListaName.isEmpty() || viewModel.getAllListsNames().contains(newListaName)){
                                Toast.makeText(getContext(), "Nombre de lista invalido", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (PREVIOUS_LISTNAME.equals(newListaName)) {
                                Toast.makeText(getContext(), "Introduce un nombre diferente", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            viewModel.changeListName(newListaName, listaName);

                            allLists.set(LAST_POSITION, newListaName);
                            notifyItemChanged(LAST_POSITION);
                            alert.dismiss();
                    });

                    view.findViewById(R.id.eliminarLista_Btn_dialogAllLists).setOnClickListener(deleteLista -> {

                        // Lanzamos este codigo en otro hilo para la subida a la base de datos de la modificacion en las listas.
                        viewModel.deleteList(listaName); // Eliminamos la lista, tambien en base de datos.

                        allLists.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        alert.dismiss();
                    });
                    alert.show();
                });
            }
        }
    }
}