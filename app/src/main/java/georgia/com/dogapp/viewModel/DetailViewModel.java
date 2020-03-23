package georgia.com.dogapp.viewModel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import georgia.com.dogapp.model.DogBreed;
import georgia.com.dogapp.model.DogDatabase;

public class DetailViewModel extends AndroidViewModel {

  public   MutableLiveData<DogBreed > dogLiveData= new MutableLiveData<DogBreed>();
  private retriveDogTask task;

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }


    public void fetch(int uuid){
        task=new retriveDogTask();
        task.execute(uuid);

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(task!=null){
            task.cancel(true);
            task=null;
        }

    }

    private class retriveDogTask extends AsyncTask<Integer,Void,DogBreed>{

        @Override
        protected DogBreed doInBackground(Integer... integers) {
            int uuid=integers[0];
            return DogDatabase.getInstance(getApplication()).dogDao().getDog(uuid);
        }

        @Override
        protected void onPostExecute(DogBreed dogBreed) {
            super.onPostExecute(dogBreed);
            dogLiveData.setValue(dogBreed);
        }
    }

}
