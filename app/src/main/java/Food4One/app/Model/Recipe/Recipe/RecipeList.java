package Food4One.app.Model.Recipe.Recipe;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeList implements Serializable {


    private static ArrayList<Recipe> recipesSaved;

    public static ArrayList<Recipe> getInstance(){
        if (recipesSaved == null) recipesSaved = new ArrayList<>();
        return recipesSaved;
    }

    public static ArrayList<Recipe> getRecipesSaved() {
        return recipesSaved;
    }

    public static void setRecipesSaved(ArrayList<Recipe> recipesSaved) {
        RecipeList.recipesSaved = recipesSaved;
    }

    public void add(Recipe recipe){
        this.recipesSaved.add(recipe);
    }

    public void remove(Recipe recipe){
        this.recipesSaved.remove(recipe);
    }

    public void remove(int pos){
        this.recipesSaved.remove(pos);
    }

}
