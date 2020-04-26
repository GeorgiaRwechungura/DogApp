package georgia.com.dogapp.view;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.palette.graphics.Palette;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import georgia.com.dogapp.R;
import georgia.com.dogapp.databinding.FragmentDetailBinding;
import georgia.com.dogapp.databinding.SendSmsDialogBinding;
import georgia.com.dogapp.model.DogBreed;
import georgia.com.dogapp.model.DogPalette;
import georgia.com.dogapp.model.SmsInfo;
import georgia.com.dogapp.util.Util;
import georgia.com.dogapp.viewModel.DetailViewModel;

public class DetailFragment extends Fragment {

    private DetailViewModel viewModel;
    private int dogUid;
    private FragmentDetailBinding binding;
    private Boolean sendSmsStarted=false;
    private DogBreed currentDog;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);
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
                currentDog=dogBreed;
                binding.setDog(dogBreed);
                if(dogBreed.imageUrl!=null){
                    setupBackgroundColor(dogBreed.imageUrl);

                }

            }

        });
    }
    private void setupBackgroundColor(String url){
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource)
                                .generate(palette -> {
                                    int intColor=palette.getMutedSwatch().getRgb();
                                    DogPalette myPalette=new DogPalette(intColor);
                                    binding.setPalette(myPalette);

                                });

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_send_sms:{
                if(!sendSmsStarted){
                    sendSmsStarted=true;
                    ( (MainActivity)getActivity()).checkSmsPermission();
                }


            }
            break;
            case R.id.action_share:{
                Toast.makeText(getContext(),"Action Share",Toast.LENGTH_LONG).show();

            }
            break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onPermissionResult(Boolean permissionGranted) {
        if(isAdded() && sendSmsStarted && permissionGranted){
            SmsInfo smsInfo=new SmsInfo("",currentDog.dogBreed + "bred for "+ currentDog.bredFor,currentDog.imageUrl);
            SendSmsDialogBinding dialogBinding=DataBindingUtil.inflate(
                    LayoutInflater.from(getContext()),
                    R.layout.send_sms_dialog,
                    null,
                    false
            );
            new AlertDialog.Builder(getContext())
                    .setView(dialogBinding.getRoot())
                    .setPositiveButton("Send SMS", ((dialog, which) -> {
                        if(!dialogBinding.smsDestination.getText().toString().isEmpty()) {
                            smsInfo.to = dialogBinding.smsDestination.getText().toString();
                            sendSms(smsInfo);
                        }
                    }));


        }

    }
    private void sendSms(SmsInfo smsInfo) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getContext(), 0, intent, 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(smsInfo.to, null, smsInfo.text, pi, null);
    }
}
