package georgia.com.dogapp.view;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import georgia.com.dogapp.R;
import georgia.com.dogapp.model.DogBreed;
import georgia.com.dogapp.util.Util;

public class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.DogViewHolder> {

    private ArrayList<DogBreed> dogList;

    public DogsListAdapter(ArrayList<DogBreed> dogList){
        this.dogList=dogList;
    }

    public void updateDogList(List<DogBreed> newDogList){
        dogList.clear();
        dogList.addAll(newDogList);
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog,parent,false);
           return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {
        ImageView image=holder.itemView.findViewById(R.id.imageView);
        TextView name=holder.itemView.findViewById(R.id.name);
        TextView lifeSpan=holder.itemView.findViewById(R.id.lifespan);
        LinearLayout linearLayout=holder.itemView.findViewById(R.id.dogLayout);

        name.setText(dogList.get(position).dogBreed);
        lifeSpan.setText(dogList.get(position).lifeSpan);
        Util.loadImage(image,dogList.get(position).imageUrl,Util.getProgressDrawable(image.getContext()));
        linearLayout.setOnClickListener(v -> {
           ListFragmentDirections.ActionDetail action=ListFragmentDirections.actionDetail();
           action.setDogUid(dogList.get(position).uuid);
            Navigation.findNavController(linearLayout).navigate(action);
        });

    }

    @Override
    public int getItemCount() {
        return dogList.size();
    }

    class DogViewHolder extends RecyclerView.ViewHolder{
            public View itemView;

            public DogViewHolder(@NonNull View itemView) {
                super(itemView);
                this.itemView=itemView;
            }
        }
}
