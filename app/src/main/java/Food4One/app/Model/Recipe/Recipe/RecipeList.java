package Food4One.app.Model.Recipe.Recipe;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeList {


    private static ArrayList<Recipe> recipesSaved;
    public static ArrayList<Recipe> getInstance() {
        if (recipesSaved == null) recipesSaved = new ArrayList<>();
        return recipesSaved;
    }

    public static ArrayList<Recipe> getRecipesSaved() {
        return recipesSaved;
    }

    public static void setRecipesSaved(ArrayList<Recipe> recipesSaved) {
        RecipeList.recipesSaved = recipesSaved;
    }
}