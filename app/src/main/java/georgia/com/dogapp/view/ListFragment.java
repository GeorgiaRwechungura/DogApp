package georgia.com.dogapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import georgia.com.dogapp.R;
import georgia.com.dogapp.model.DogBreed;
import georgia.com.dogapp.viewModel.ListViewModel;


public class ListFragment extends Fragment {

    ListViewModel viewModel;
    private DogsListAdapter dogsListAdapter=new DogsListAdapter(new ArrayList<>());
    @BindView(R.id.dogslist)
    RecyclerView dogList;
    @BindView(R.id.listError)
    TextView listError;
    @BindView(R.id.progressBar)
    ProgressBar loadingView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    public ListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel= ViewModelProviders.of(this).get(ListViewModel.class);
        viewModel.refresh();

        dogList.setLayoutManager(new LinearLayoutManager(getContext()));
        dogList.setAdapter(dogsListAdapter);
        observeViewModel();

    }
    private  void observeViewModel(){
      viewModel.dogs.observe(this, (List<DogBreed> dogs) -> {
          if(dogs !=null && dogs instanceof  List){
               dogList.setVisibility(View.VISIBLE);
               dogsListAdapter.updateDogList(dogs);
          }

      });
      viewModel.dogError.observe(this, isError -> {

          if(isError!=null && isError instanceof Boolean){
            listError.setVisibility(isError? View.VISIBLE : View.GONE);
          }

      });
      viewModel.loading.observe(this, isLoading -> {
         if(isLoading!=null && isLoading instanceof Boolean){
              loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
           if(isLoading){
               listError.setVisibility(View.GONE);
               dogList.setVisibility(View.GONE);
           }
         }
      });
    }
}
