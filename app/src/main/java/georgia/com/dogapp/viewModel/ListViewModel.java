package georgia.com.dogapp.viewModel;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import georgia.com.dogapp.model.DogBreed;
import georgia.com.dogapp.model.DogDao;
import georgia.com.dogapp.model.DogDatabase;
import georgia.com.dogapp.model.DogsApiService;

import georgia.com.dogapp.util.NotificationHelper;
import georgia.com.dogapp.util.SharedPreferenceHelper;
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
    private AsyncTask<List<DogBreed>,Void, List<DogBreed>> insertTask;
    private AsyncTask<Void,Void,List<DogBreed>> retriveTask;
    private SharedPreferenceHelper prefsHelper=  SharedPreferenceHelper.getInstance(getApplication());
    private long refreshTime= 5* 60*1000* 1000* 1000L;

    public ListViewModel(@NonNull Application application) {

        super(application);
    }

    public void refresh() {
        long updateTime=prefsHelper.getUpdateTime();
        long currentTime=System.nanoTime();
        if(updateTime!=0 && currentTime-updateTime< refreshTime){
         fetchFromDatabase();
        }else {
            fetchFromRemote();
        }
    }
    public void refreshByPassCache(){
        fetchFromRemote();
    }
    private void fetchFromDatabase(){
        loading.setValue(true);
        retriveTask=new RetriveDogTask();
        retriveTask.execute();

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
                            insertTask=new insertDogTask();
                            insertTask.execute(dogBreeds);
                                Toast.makeText(getApplication(),"Dog Retrived from Endpoint",Toast.LENGTH_LONG).show();
                                NotificationHelper.getInstance(getApplication()).createNotification();
                                prefsHelper.saveUpdateTime(System.nanoTime());
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
public void dogRetrived(List<DogBreed> dogList){
    dogs.setValue(dogList);
    dogError.setValue(false);
    loading.setValue(false);

}

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
        if(insertTask!=null){
            insertTask.cancel(true);
            insertTask=null;
        }
        if(retriveTask!=null){
            retriveTask.cancel(true);
            retriveTask=null;
        }
    }

    private class insertDogTask extends AsyncTask<List<DogBreed>,Void, List<DogBreed>>{


        @Override
        protected List<DogBreed> doInBackground(List<DogBreed>... lists) {
            List<DogBreed> list=lists[0];
            DogDao dao= DogDatabase.getInstance(getApplication()).dogDao();
            dao.deleteAllDogs();

            ArrayList<DogBreed> newList=new ArrayList<>(list);
            List<Long> result=dao.insertAll(newList.toArray(new DogBreed[0]));
            int i=0;
            while(i< list.size()){
                list.get(i).uuid=result.get(i).intValue();
                i++;

            }
            return list;
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            super.onPostExecute(dogBreeds);
            dogRetrived(dogBreeds);

        }
    }
    private class RetriveDogTask extends AsyncTask<Void,Void,List<DogBreed>>{

        @Override
        protected List<DogBreed> doInBackground(Void... voids) {
            return DogDatabase.getInstance(getApplication()).dogDao().getAllDogs();
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            super.onPostExecute(dogBreeds);
            dogRetrived(dogBreeds);
            Toast.makeText(getApplication(),"Dogs retrived from Database",Toast.LENGTH_LONG).show();

        }
    }
}


