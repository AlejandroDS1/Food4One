package Food4One.app.Model.Recipie.Recipie;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipieList implements Serializable {


    private ArrayList<Recipie> recipies;


    public RecipieList(ArrayList<Recipie> recipies) {
        this.recipies = recipies;
    }

    public RecipieList(){
        this.recipies = new ArrayList<>();
    }

    public void add(Recipie recipie){
        this.recipies.add(recipie);
    }

    public void remove(Recipie recipie){
        this.recipies.remove(recipie);
    }

    public void remove(int pos){
        this.recipies.remove(pos);
    }

}
