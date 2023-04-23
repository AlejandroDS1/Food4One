package Food4One.app.Model.Recipe.Recipe;

import java.util.ArrayList;

public class RecipesUserApp {
    private static ArrayList<Recipe> recetasUser;

    public static ArrayList<Recipe>  getInstance(){
        if (recetasUser == null) recetasUser = new ArrayList<>();
        return recetasUser;
    }

    public static ArrayList<Recipe> getRecetasUser() {
        return recetasUser;
    }

    public static void setRecetasUser(ArrayList<Recipe> recetasUser) {
        RecipesUserApp.recetasUser = recetasUser;
    }

    public void addNewRecipeUserApp(Recipe recipe){
        RecipesUserApp.recetasUser.add(recipe);
    }
}
