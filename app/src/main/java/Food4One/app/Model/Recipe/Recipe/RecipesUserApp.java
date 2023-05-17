package Food4One.app.Model.Recipe.Recipe;

import java.util.ArrayList;

public class RecipesUserApp {
    private static ArrayList<Recipe> recetasUser;
    private static ArrayList<Recipe> recetasExplorer;

    public static ArrayList<Recipe>  getRecetasExplorer(){
        if (recetasExplorer == null) recetasExplorer = new ArrayList<>();
        return recetasExplorer;
    }


    public static ArrayList<Recipe> getRecetasUser() {
        if (recetasUser == null) recetasUser = new ArrayList<>();
        return recetasUser;
    }
    public static void removeSingleton(){ recetasUser = null; }

    public static void setRecetasUser(ArrayList<Recipe> recetasUser) {
        RecipesUserApp.recetasUser = recetasUser;
    }

    public static void setRecetasExplorer(ArrayList<Recipe> recetasExplorer) {
        RecipesUserApp.recetasExplorer = recetasExplorer;
    }

    public static void addNewRecipeUserApp(Recipe recipe){
        RecipesUserApp.recetasUser.add(recipe);
    }

}
