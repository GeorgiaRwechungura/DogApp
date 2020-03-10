package georgia.com.dogapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import georgia.com.dogapp.R;
import georgia.com.dogapp.model.DogBreed;
import georgia.com.dogapp.viewModel.DetailViewModel;

public class DetailFragment extends Fragment {

    private DetailViewModel viewModel;
    private  int dogUid;
    @BindView(R.id.dogImage)
    ImageView dogImage;
    @BindView(R.id.dogName)
    TextView dogName;
    @BindView(R.id.dogPurpose)
    TextView dogPurpose;
    @BindView(R.id.dogTemperament)
    TextView dogTemperament;
    @BindView(R.id.dogLifeSpan)
    TextView dogLifeSpan;

    public DetailFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments()!=null){
            dogUid=DetailFragmentArgs.fromBundle(getArguments()).getDogUid();
        }
        viewModel= ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.fetch();
        observeViewModel();
    }

    private void observeViewModel(){
   viewModel.dogLiveData.observe(this, dogBreed -> {
       if(dogBreed!=null && dogBreed instanceof DogBreed){
           dogName.setText(dogBreed.dogBreed);
           dogLifeSpan.setText(dogBreed.lifeSpan);
           dogTemperament.setText(dogBreed.temperament);
           dogPurpose.setText(dogBreed.bredFor);
       }
   });
    }
}