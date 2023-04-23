package Food4One.app.View.MainScreen.ui.home;

public class ListRecipes {
    private int imagen;
    private String nameRecipeCard;

    public ListRecipes(String nameRecipeCard, int imagen) {
        this.imagen = imagen;
        this.nameRecipeCard = nameRecipeCard;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getNameRecipeCard() {
        return nameRecipeCard;
    }

    public void setNameRecipeCard(String nameRecipeCard) {
        this.nameRecipeCard = nameRecipeCard;
    }
}
