package georgia.com.dogapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import georgia.com.dogapp.model.DogBreed;

public class ListViewModel extends AndroidViewModel {
    public MutableLiveData<List<DogBreed>> dogs= new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogError= new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading=new MutableLiveData<Boolean>();
    public ListViewModel(@NonNull Application application) {

        super(application);
    }
    public void refresh(){
        DogBreed dog1=new DogBreed("1","corgie","6 years","","","","");
        DogBreed dog2=new DogBreed("2","towailer","10 years","","","","");
        DogBreed dog3=new DogBreed("3","GermanShepad","12 years","","","","");
        DogBreed dog4=new DogBreed("4","towailer","10 years","","","","");
        DogBreed dog5=new DogBreed("5","GermanShepad","12 years","","","","");
        DogBreed dog6=new DogBreed("6","towailer","10 years","","","","");
        DogBreed dog7=new DogBreed("7","GermanShepad","12 years","","","","");
        DogBreed dog8=new DogBreed("8","towailer","10 years","","","","");
        DogBreed dog9=new DogBreed("9","GermanShepad","12 years","","","","");

        ArrayList<DogBreed> dogsList=new ArrayList<>();
        dogsList.add(dog1);
        dogsList.add(dog2);
        dogsList.add(dog3);
        dogsList.add(dog4);
        dogsList.add(dog5);
        dogsList.add(dog6);
        dogsList.add(dog7);
        dogsList.add(dog8);
        dogsList.add(dog9);

        dogs.setValue(dogsList);
        dogError.setValue(false);
        loading.setValue(false);

    }
}

