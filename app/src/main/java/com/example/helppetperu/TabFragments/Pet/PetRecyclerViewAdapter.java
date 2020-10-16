package com.example.helppetperu.TabFragments.Pet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.helppetperu.Class.Pet;
import com.example.helppetperu.R;
import java.lang.reflect.Field;
import java.util.ArrayList;


public class PetRecyclerViewAdapter extends RecyclerView.Adapter<PetRecyclerViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<Pet> list;
    OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onReportClick(int position);
        void onRemoveClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public PetRecyclerViewAdapter(Context context, ArrayList<Pet> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PetRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_card,parent,false);
        return new MyViewHolder(v,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final PetRecyclerViewAdapter.MyViewHolder holder, final int position) {
        //Picasso.with(context).load(list.get(position).getImgUrl()).into(holder.petImage);
        Glide.with(context).load(list.get(position).getImgUrl()).into(holder.petImage);
        holder.petName.setText(list.get(position).getNombre());
        holder.petEstado.setText(list.get(position).getEstadoMascota());
        holder.petDescription.setText(list.get(position).getDetalle());
        holder.petBirthdate.setText(list.get(position).getNacimiento());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView petImage;
        TextView petName;
        TextView petEstado;
        TextView petDescription;
        TextView petBirthdate;
        ImageView popmenu;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            petImage = itemView.findViewById(R.id.cardPetImage);
            petName = itemView.findViewById(R.id.cardPetName);
            petEstado = itemView.findViewById(R.id.cardPetEstado);
            petDescription = itemView.findViewById(R.id.cardPetDetalle);
            petBirthdate = itemView.findViewById(R.id.cardPetBirthdate);
            popmenu = itemView.findViewById(R.id.iv_menu);

            popmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(),popmenu);
                    popupMenu.inflate(R.menu.cardview_menu);

                    Object menuHelper;
                    Class[] argTypes;
                    try {
                        Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                        fMenuHelper.setAccessible(true);
                        menuHelper = fMenuHelper.get(popupMenu);
                        argTypes = new Class[] { boolean.class };
                        menuHelper.getClass().getDeclaredMethod("setForceShowIcon",
                                argTypes).invoke(menuHelper, true);
                    } catch (Exception e) {
                        popupMenu.show();
                        return;
                    }

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.report_pet:
                                    if (listener != null) {
                                        int position = getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            listener.onReportClick(position);
                                        }
                                    }
                                    break;
                                case R.id.remove_pet:
                                    if (listener != null) {
                                        int position = getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            listener.onRemoveClick(position);
                                        }
                                    }
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

        }
    }
}
