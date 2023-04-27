package Food4One.app.View.MainScreen.MainScreenFragments.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import Food4One.app.Model.Recipe.Recipe.Recipe;
import Food4One.app.R;


public class DoRecipeActivity extends AppCompatActivity {

    private HomeViewModel homeViewModel;
    private Recipe recipeToMake;
    private ArrayAdapter ingredientsAdapter, pasosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_recipe);
        getSupportActionBar().hide();

        homeViewModel = HomeViewModel.getInstance();
        recipeToMake = homeViewModel.getDoRecipe().getValue();
        cargarDatosRecipeToMake();

        TextView back = findViewById(R.id.backButtonDoRecipe);
        back.setOnClickListener(view-> {
                getSupportFragmentManager().popBackStackImmediate();
            });
    }

    private void cargarDatosRecipeToMake() {
        //Se cargan los ingredientes y los pasos de la receta que se ha seleccionado antes
        ingredientsAdapter = new ArrayAdapter<String>(this,R.layout.list_ingredients_layout,
                recipeToMake.getIngredientes().getArrayString());
        pasosAdapter = new ArrayAdapter<>(this, R.layout.list_ingredients_layout,
                recipeToMake.getPasos());

        //Imagen, Descripción y Nombre de la Receta
        ListView ingredientesView = findViewById(R.id.ingredientsList);
        ingredientesView.setAdapter(ingredientsAdapter);
        ListView pasosView = findViewById(R.id.pasosList);
        pasosView.setAdapter(pasosAdapter);
        TextView description = findViewById(R.id.descriptionRecipeToDo);
        description.setText(recipeToMake.getDescription());
        ImageView recipePhoto = findViewById(R.id.recipeToDoImage);
        Picasso.get().load(recipeToMake.getPictureURL()).resize(790, 600).centerCrop().into(recipePhoto);
        TextView nameRecipe = findViewById(R.id.recetToDoName);
        nameRecipe.setText(recipeToMake.getNombre().split("@")[0]);

    }
}