package Food4One.app.View.MainScreen.MainScreenFragments.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import Food4One.app.Model.Recipe.Recipe.RecipeRepository;

public class RotateViewModel  extends ViewModel {

    private final MutableLiveData<String> completed;
    private static RotateViewModel rotateViewModel;
    private RecipeRepository mRecetasRepository;

    public static RotateViewModel getInstance() {
        if (rotateViewModel == null)
            rotateViewModel = new RotateViewModel();
        return rotateViewModel;
    }

    public RotateViewModel(){
        completed = new MutableLiveData<>("");
        mRecetasRepository = RecipeRepository.getInstance();
    }

    public LiveData<String> getCompleted(){ return this.completed; }

    public void setCompleted(String tipo) { this.completed.setValue(tipo); }



}
