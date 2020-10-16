package com.example.helppetperu.TabFragments.Pet.Registry;

import android.net.Uri;

import com.afollestad.materialdialogs.MaterialDialog;

public class RegistryPetPresenter implements RegistryPetInterface.Presenter, RegistryPetInterface.TaskListener {

    private RegistryPetInterface.View view;
    private RegistryPetInterface.Model model;

    public RegistryPetPresenter(RegistryPetInterface.View view){
        this.view = view;
        model = new RegistryPetModel(this);
    }

    @Override
    public void toPetForm(String nombre,String detalle, String birthdate, int type, int origin, String imageUrl) {
        if(view != null){
            view.disableInputs();
            view.showProgress();
        }
        model.doPetForm(nombre,detalle,birthdate,type,origin,imageUrl);
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void onSucess() {
        if (view != null){
            view.enableInputs();
            view.hideProgress();
            view.onPetForm();
        }
    }

    @Override
    public void onError(String error) {
        if (view != null){
            view.enableInputs();
            view.hideProgress();
            view.onError(error);
        }
    }


}
