package com.example.helppetperu.Login;

public class LoginPresenter implements LoginInterface.Presenter , LoginInterface.TaskListener{

    private LoginInterface.View view;
    private LoginInterface.Model model;

    public LoginPresenter(LoginInterface.View view){
        this.view = view;
        model = new LoginModel(this);
    }
    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void toLogin(String email, String password) {
        if (view !=null){
            view.disableInputs();
            view.showProgress();
        }
        model.doLogin(email,password);
    }

    @Override
    public void onSucess() {
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
