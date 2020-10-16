package com.example.helppetperu.SignUp;

public interface SignUpInterface {

    interface View{
        void disableInputs();
        void enableInputs();

        void showProgress();
        void hideProgress();

        void handleSignUp();
        boolean validateViews();

        void onError(String error);
        void onLogin();
    }
    interface Presenter{
        void onDestroy();
        void toSignUp(String name, String surname, String email, String password, String birthdate);
    }
    interface Model{
        void doSignUp(String name, String surname, String email, String password, String birthdate);
    }
    interface TaskListener{
        void onSuccess();
        void onError(String error);
    }
}
