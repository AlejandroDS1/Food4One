package Food4One.app.Model.Recipie.Recipie;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeList implements Serializable {


    private ArrayList<Recipe> recipes;


    public RecipeList(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public RecipeList(){
        this.recipes = new ArrayList<>();
    }

    public void add(Recipe recipe){
        this.recipes.add(recipe);
    }

    public void remove(Recipe recipe){
        this.recipes.remove(recipe);
    }

    public void remove(int pos){
        this.recipes.remove(pos);
    }

}
