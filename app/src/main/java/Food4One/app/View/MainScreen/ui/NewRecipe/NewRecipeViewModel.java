package Food4One.app.View.MainScreen.ui.NewRecipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewRecipeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public NewRecipeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}