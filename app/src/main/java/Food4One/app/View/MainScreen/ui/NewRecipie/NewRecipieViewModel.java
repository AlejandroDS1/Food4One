package Food4One.app.View.MainScreen.ui.NewRecipie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewRecipieViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public NewRecipieViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}