package com.example.helppetperu.SignUp;

public class SignUpPresenter implements SignUpInterface.Presenter, SignUpInterface.TaskListener{

    private SignUpInterface.View view;
    private SignUpInterface.Model model;

    public SignUpPresenter(SignUpInterface.View view) {
        this.view = view;
        model = new SignUpModel(this);
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void toSignUp(String name, String surname, String email, String password, String birthdate) {
        if(view != null){
            view.disableInputs();
            view.showProgress();
        }
        model.doSignUp(name,surname,email,password,birthdate);
    }

    @Override
    public void onSuccess() {
        if (view != null){
            view.enableInputs();
            view.hideProgress();
            view.onLogin();
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
