package Food4One.app.Model.Recipe.Recipe;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends ArrayAdapter<Recipe> {

    private ArrayList<Recipe> recipies;
    private Context contexto;

    public RecipeListAdapter(@NonNull Context context, int resource, @NonNull List<Recipe> objects) {
        super(context, resource, objects);
        recipies = (ArrayList<Recipe>) objects;
        contexto = context;
    }
}
