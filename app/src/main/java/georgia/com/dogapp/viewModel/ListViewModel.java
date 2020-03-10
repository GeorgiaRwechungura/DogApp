package georgia.com.dogapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import georgia.com.dogapp.model.DogBreed;
import georgia.com.dogapp.model.DogsApiService;

import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {

    private CompositeDisposable disposable = new CompositeDisposable();
    private DogsApiService dogsApiService=new DogsApiService();
    public MutableLiveData<List<DogBreed>> dogs = new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();

    public ListViewModel(@NonNull Application application) {

        super(application);
    }

    public void refresh() {
        fetchFromRemote();
    }

    private void fetchFromRemote() {
        loading.setValue(true);
        disposable.add(
                dogsApiService.getDogs()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                            @Override
                            public void onSuccess(List<DogBreed> dogBreeds) {
                                dogs.setValue(dogBreeds);
                                dogError.setValue(false);
                                loading.setValue(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                dogError.setValue(true);
                                loading.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );


    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

