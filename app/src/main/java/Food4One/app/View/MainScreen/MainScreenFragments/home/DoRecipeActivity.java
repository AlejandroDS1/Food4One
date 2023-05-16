package Food4One.app.View.MainScreen.MainScreenFragments.home;

import static android.content.res.Resources.getSystem;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.R;


public class DoRecipeActivity extends AppCompatActivity {

    private HomeViewModel homeViewModel;
    private Recipe recipeToMake;
    private ArrayAdapter ingredientsAdapter, pasosAdapter;
    private ListView ingredientesView, pasosView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_recipe);
        getSupportActionBar().hide();

        homeViewModel = HomeViewModel.getInstance();
        recipeToMake = homeViewModel.getDoRecipe().getValue();
        cargarDatosRecipeToMake();

    }
    private void cargarDatosRecipeToMake() {
        //Se cargan los ingredientes y los pasos de la receta que se ha seleccionado antes
        ingredientsAdapter = new ArrayAdapter<String>(this,R.layout.list_ingredients_layout,
                recipeToMake.getIngredientes().getArrayString());
        pasosAdapter = new ArrayAdapter<>(this, R.layout.list_ingredients_layout,
                recipeToMake.getPasos());

        LinearLayout parent=(LinearLayout)findViewById(R.id.parentList);
        parent.setLayoutParams(new LinearLayout.LayoutParams(parent.getLayoutParams().width, dpToPx( ingredientsAdapter.getCount() * 45 )));

        ingredientesView = findViewById(R.id.ingredientsList);
        ingredientesView.setAdapter(ingredientsAdapter);

        LinearLayout parentPasos =(LinearLayout)findViewById(R.id.parentPasos);
        parentPasos.setLayoutParams(new LinearLayout.LayoutParams(parent.getLayoutParams().width, dpToPx( pasosAdapter.getCount() * 150 )));
        pasosView = findViewById(R.id.pasosList);
        pasosView.setAdapter(pasosAdapter);


        //Imagen, Descripción y Nombre de la Receta
        TextView description = findViewById(R.id.descriptionRecipeToDo);
        description.setText(recipeToMake.getDescription());
        ImageView recipePhoto = findViewById(R.id.recipeToDoImage);
        Picasso.get().load(recipeToMake.getPictureURL()).resize(790, 600).centerCrop().into(recipePhoto);
        TextView nameRecipe = findViewById(R.id.recetToDoName);
        nameRecipe.setText(recipeToMake.getNombre().split("@")[0]);

    }
    public static int dpToPx(int dp)
    {
        return (int) (dp * getSystem().getDisplayMetrics().density);
    }

}