package Food4One.app.Model.Recipie.Recipie;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipieList implements Serializable {


    private ArrayList<Recipe> recipies;


    public RecipieList(ArrayList<Recipe> recipies) {
        this.recipies = recipies;
    }

    public RecipieList(){
        this.recipies = new ArrayList<>();
    }

    public void add(Recipe recipe){
        this.recipies.add(recipe);
    }

    public void remove(Recipe recipe){
        this.recipies.remove(recipe);
    }

    public void remove(int pos){
        this.recipies.remove(pos);
    }

}
