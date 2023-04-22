package Food4One.app.Model.Recipie.Recipie;

import java.util.ArrayList;

import Food4One.app.Model.User.User;

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
