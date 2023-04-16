package Food4One.app.View.MainScreen.ui.Coleccion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ColeccionViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ColeccionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}