package georgia.com.dogapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.data.DataBufferIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import georgia.com.dogapp.R;
import georgia.com.dogapp.databinding.FragmentDetailBinding;
import georgia.com.dogapp.model.DogBreed;
import georgia.com.dogapp.util.Util;
import georgia.com.dogapp.viewModel.DetailViewModel;

public class DetailFragment extends Fragment {

    private DetailViewModel viewModel;
    private int dogUid;
    private FragmentDetailBinding binding;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        this.binding = binding;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            dogUid = DetailFragmentArgs.fromBundle(getArguments()).getDogUid();
        }
        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.fetch(dogUid);
        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.dogLiveData.observe(this, dogBreed -> {
            if (dogBreed != null && dogBreed instanceof DogBreed && getContext() != null) {
                binding.setDog(dogBreed);


            }

        });
    }
}