package Food4One.app.Model.Recipie.Recipie;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class RecipieListAdapter extends ArrayAdapter<Recipie> {

    private ArrayList<Recipie> recipies;
    private Context contexto;

    public RecipieListAdapter(@NonNull Context context, int resource, @NonNull List<Recipie> objects) {
        super(context, resource, objects);
        recipies = (ArrayList<Recipie>) objects;
        contexto = context;
    }
}
