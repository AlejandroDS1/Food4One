package Food4One.app.View.Authentification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccesActivityViewModel extends ViewModel {

    private final MutableLiveData<Boolean> completed;

    public AccesActivityViewModel(){
        completed = new MutableLiveData<>(false);
    }

    public LiveData<Boolean> getCompleted(){ return this.completed; }

    public void setCompleted(boolean bool) { this.completed.setValue(bool); }
}
