package georgia.com.dogapp.view;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import georgia.com.dogapp.R;
import georgia.com.dogapp.databinding.ItemDogBinding;
import georgia.com.dogapp.model.DogBreed;
import georgia.com.dogapp.util.Util;

public class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.DogViewHolder> implements DogClickListerner {

    private ArrayList<DogBreed> dogList;

    public DogsListAdapter(ArrayList<DogBreed> dogList) {
        this.dogList = dogList;
    }

    public void updateDogList(List<DogBreed> newDogList) {
        dogList.clear();
        dogList.addAll(newDogList);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDogBinding view = DataBindingUtil.inflate(inflater, R.layout.item_dog, parent, false);
        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {
        holder.itemView.setDog(dogList.get(position));
        holder.itemView.setListener(this);
    }

    @Override
    public int getItemCount() {
        return dogList.size();
    }

    @Override
    public void onDogClicked(View v) {
        String stringUid = ((TextView) v.findViewById(R.id.dogId)).getText().toString();
        int uuid = Integer.valueOf(stringUid);

        ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
        action.setDogUid(uuid);
        Navigation.findNavController(v).navigate(action);
    }

    class DogViewHolder extends RecyclerView.ViewHolder {
        public ItemDogBinding itemView;

        public DogViewHolder(@NonNull ItemDogBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }
}
