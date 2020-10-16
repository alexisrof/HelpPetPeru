package com.example.helppetperu.TabFragments.Pet.Registry;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.helppetperu.Class.LostPet;
import com.example.helppetperu.Class.Pet;
import com.example.helppetperu.Class.ReportLocation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegistryPetModel implements RegistryPetInterface.Model {

    private RegistryPetInterface.TaskListener listener;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private Pet pet;
    private String petKey;

    public RegistryPetModel(RegistryPetInterface.TaskListener listener){
        this.listener = listener;
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        pet = new Pet("","","","","","","Segura","","");
    }

    @Override
    public void doPetForm(String nombre,String detalle,String dob,int type,int origin, String imageUrl) {
        pet.setNombre(nombre);
        pet.setNacimiento(dob);
        pet.setDetalle(detalle);
        switch (type){
            case 1: pet.setTipoMascota("Perro");break;
            case 2: pet.setTipoMascota("Gato");break;
            case 3: pet.setTipoMascota("Hamster");break;
            case 4: pet.setTipoMascota("Pig");break;
            case 5: pet.setTipoMascota("Conejo");break;
        }
        switch (type){
            case 1: pet.setOrigenMascota("Propia");break;
            case 2: pet.setOrigenMascota("Rescatada");break;
            case 3: pet.setOrigenMascota("Encontrada");break;
        }

        pet.setImgUrl(imageUrl);
        pet.setPropietario(user.getUid());
        petKey = databaseReference.push().getKey();
        pet.setId(petKey);
        databaseReference.child("pet").child(user.getUid()).child(petKey).setValue(pet).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSucess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError(e.getMessage());
            }
        });

    }

}
