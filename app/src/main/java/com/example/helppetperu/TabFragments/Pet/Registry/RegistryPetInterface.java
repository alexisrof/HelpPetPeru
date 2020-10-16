package com.example.helppetperu.TabFragments.Pet.Registry;

import android.net.Uri;

import com.afollestad.materialdialogs.MaterialDialog;

public interface RegistryPetInterface {
    interface View{
        void disableInputs();
        void enableInputs();
        void showProgress();
        void hideProgress();
        void handlePetForm();
        void handlePetImage();
        boolean isValidateImage();
        boolean isValidateForm();
        void onPetForm();
        void onImage();
        void onError(String error);
    }
    interface Presenter{
        void toPetForm(String nombre,String detalle,String birthdate,int type,int origin,String imageUrl);
        void onDestroy();

    }
    interface Model{
        void doPetForm(String nombre,String detalle,String dob,int type,int origin,String imageUrl);
    }
    interface TaskListener{
        void onSucess();
        void onError(String error);
    }
}
