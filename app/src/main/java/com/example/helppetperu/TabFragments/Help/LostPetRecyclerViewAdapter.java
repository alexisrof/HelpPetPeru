package com.example.helppetperu.TabFragments.Help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.helppetperu.Class.LostPet;
import com.example.helppetperu.R;

import java.util.ArrayList;

public class LostPetRecyclerViewAdapter extends RecyclerView.Adapter<LostPetRecyclerViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<LostPet> list;

    public LostPetRecyclerViewAdapter(Context context, ArrayList<LostPet> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lostpet_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImgUrl()).into(holder.petImage);
        holder.petName.setText(list.get(position).getNombre());
        holder.petLocation.setText(list.get(position).getEstadoMascota());
        holder.petDescription.setText(list.get(position).getDetalle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView petImage;
        TextView petName;
        TextView petLocation;
        TextView petDescription;
        ImageView popmenu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            petImage = itemView.findViewById(R.id.cardLostPetImage);
            petName = itemView.findViewById(R.id.cardLostPetName);
            petLocation = itemView.findViewById(R.id.cardLostPetLocation);
            petDescription = itemView.findViewById(R.id.cardLostPetDetalle);
            popmenu = itemView.findViewById(R.id.iv_LostPetMenu);
        }
    }

}
