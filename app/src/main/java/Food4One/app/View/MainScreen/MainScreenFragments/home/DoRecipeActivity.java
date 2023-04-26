package Food4One.app.View.MainScreen.MainScreenFragments.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

        homeViewModel = HomeViewModel.getInstance();
        recipeToMake = homeViewModel.getDoRecipe().getValue();

        //Se cargan los ingredientes y los pasos de la receta que se ha seleccionado antes
        ingredientsAdapter = new ArrayAdapter<String>(this,R.layout.list_ingredients_layout,
                                    recipeToMake.getIngredientes().getArrayString());
        pasosAdapter = new ArrayAdapter<>(this, R.layout.list_ingredients_layout,
                                            recipeToMake.getPasos());

        ListView l = findViewById(R.id.ingredientsList);
        l.setAdapter(ingredientsAdapter);


        TextView back = findViewById(R.id.backButtonDoRecipe);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        });
    }
}