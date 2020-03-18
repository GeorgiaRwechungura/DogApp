package georgia.com.dogapp.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import georgia.com.dogapp.model.DogBreed;

public class DetailViewModel extends ViewModel {

  public   MutableLiveData<DogBreed > dogLiveData= new MutableLiveData<DogBreed>();



    public void fetch(){

    }
}
