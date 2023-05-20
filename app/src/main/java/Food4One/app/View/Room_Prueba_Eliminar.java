package Food4One.app.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import Food4One.app.DataBase.ShoppingList.ShoppingList;
import Food4One.app.DataBase.ShoppingList.ShoppingListDataBase;
import Food4One.app.R;
import Food4One.app.databinding.ActivityRoomPruebaEliminarBinding;

public class Room_Prueba_Eliminar extends AppCompatActivity {

    private ActivityRoomPruebaEliminarBinding binding;
    private static ShoppingListDataBase DB;
    private RoomViewModel roomViewModel;
    private IngreAdapter ingreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomPruebaEliminarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        initDB();

        roomViewModel.setDao(DB.shoppingListDao());

        initListeners();

    }

    private void initDB() {
        DB = Room.databaseBuilder(getApplicationContext(),
                ShoppingListDataBase.class, "ShoppingList.db").allowMainThreadQueries().build();
    }

    private void initListeners() {


        binding.anadirDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShoppingList _newIngrediente = new ShoppingList(binding.listNameDB.getText().toString(), binding.valorDB.getText().toString(), false);

                DB.shoppingListDao().addIngrediente(_newIngrediente);

                ingreAdapter.addItem(_newIngrediente.ingrediente);
            }
        });


        binding.consigueAtributoDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Consigo el ID de un ingrediente.
                String id = String.valueOf(DB.shoppingListDao().getId("HARD", binding.valorDB.getText().toString()));

                Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
            }
        });
        binding.eliminarDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.shoppingListDao().deleteAll();
            }
        });



        // INiT de lis view

        List<ShoppingList> ingredientes = DB.shoppingListDao().getList();

        List<String> _i = new ArrayList<>();

        for (ShoppingList s : ingredientes){
            _i.add(s.ingrediente);
        }

        ingreAdapter = new IngreAdapter(_i);

        binding.listaDB.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.listaDB.setAdapter(ingreAdapter);
        ingreAdapter.notifyDataSetChanged();
    }

    public static class IngreAdapter extends RecyclerView.Adapter<IngreAdapter.ViewHolder> {

        private final List<String> ingredientes;

        public IngreAdapter(List<String> ingredientes) {

            List<ShoppingList> shpList = DB.shoppingListDao().getList();

            List<String> _l = new ArrayList<>();

            for (ShoppingList s : shpList)
                _l.add(s.ingrediente);

            this.ingredientes = _l;
        }

        public final void addItem(String ingrediente){
            this.ingredientes.add(ingrediente);
            notifyItemInserted(getItemCount()); // Actualizamos los datos
        }

        @NonNull
        @Override
        public IngreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View view = inflater.inflate(R.layout.item_ingredientes_do_recipe, parent, false);

            return new IngreAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull IngreAdapter.ViewHolder holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return ingredientes.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            private final CheckBox checkBox;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.mIngrediente_doRecipe);
            }

            public void onBind(final int pos){

                checkBox.setText(ingredientes.get(pos));

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                        DB.shoppingListDao().checkIngrediente("HARD", checkBox.getText().toString(), isChecked);

                        if (isChecked){
                            checkBox.setText(ingredientes.get(pos) + "    |   checked");
                        }else
                            checkBox.setText(ingredientes.get(pos));

                    }
                });
            }
        }
    }

}