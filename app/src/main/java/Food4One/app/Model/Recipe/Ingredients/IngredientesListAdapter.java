package Food4One.app.Model.Recipe.Ingredients;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class IngredientesListAdapter extends ArrayAdapter<Ingrediente> {

    private List<Ingrediente> ingredientes;
    private Context contexto;

    public IngredientesListAdapter(@NonNull Context context, int resource, @NonNull List<Ingrediente> objects) {
        super(context, resource, objects);
        this.ingredientes = objects;
        this.contexto = context;
    }


    /*
    // PROYECTO 'PruebaLista' para lista de ingredientes.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View layout = convertView;

        if (layout == null)
            layout = LayoutInflater.from(contexto).inflate(R.layout.item_list, null);

        //Objeto ingrediente de la posicion
        Ingrediente ingrediente = ingredientes.get(position);

        // Consigo los objetos de vista de la posicion de la lista
        TextView ingredientName = layout.findViewById(R.id.ingrediente_name),
                cantidad = layout.findViewById(R.id.cantidad_list);

        // El check box en principio no hace falta aqui
        //CheckBox checkBox = convertView.findViewById(R.id.Marcado);

        // Pongo el texto necesario en cada uno
        ingredientName.setText(ingrediente.getNombre());
        cantidad.setText(ingrediente.getCantidadStr());

        return layout;
    }

     */
}
