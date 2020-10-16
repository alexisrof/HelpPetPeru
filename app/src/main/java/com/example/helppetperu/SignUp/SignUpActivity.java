package com.example.helppetperu.SignUp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.helppetperu.R;
import com.example.helppetperu.Util.DateMask;
import com.rengwuxian.materialedittext.MaterialEditText;

import mehdi.sakout.fancybuttons.FancyButton;

public class SignUpActivity extends AppCompatActivity implements SignUpInterface.View {

    private MaterialEditText ed_name, ed_surname, ed_birthdate, ed_email, ed_password, ed_passwordConfirmation;
    private FancyButton btn_signup;
    private MaterialDialog dialog;
    private SignUpInterface.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        showToolbar("Registro", true);
        setViews();
    }

    private void setViews(){
        presenter = new SignUpPresenter(this);
        ed_name = findViewById(R.id.EditText_SignUpName);
        ed_surname = findViewById(R.id.EditText_SignUpSurname);
        ed_birthdate = findViewById(R.id.EditText_SignUpDate);
        ed_email = findViewById(R.id.EditText_SignUpEmail);
        ed_password = findViewById(R.id.EditText_SignUpPassword);
        ed_passwordConfirmation = findViewById(R.id.EditText_SignUpRepeatPassword);
        btn_signup = findViewById(R.id.Button_SignUp);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });

        final MaterialEditText dob = findViewById(R.id.EditText_SignUpDate);
        dob.addTextChangedListener(new DateMask(new DateMask.DateMaskingCallback() {
            @Override
            public void dateOfBirthValidation(boolean isValid, String dateOfBirth, String error) throws Exception {
                if (isValid) {
                    // sets formatted string here
                    dob.setText(dateOfBirth);
                    dob.setSelection(dateOfBirth.length());
                } else {
                    // sets error here
                    dob.setText(dateOfBirth);
                    dob.setSelection(dateOfBirth.length());
                    dob.setError(error);
                    Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        },"/"));

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Cargando")
                .content("Espere, por favor...")
                .cancelable(false)
                .progress(true,0);
        dialog = builder.build();
    }

    private void showToolbar(String registro, boolean b){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(b);
            actionBar.setTitle(registro);
        }
    }

    private void setEnable(boolean b){
        ed_name.setEnabled(b);
        ed_surname.setEnabled(b);
        ed_birthdate.setEnabled(b);
        ed_email.setEnabled(b);
        ed_password.setEnabled(b);
        ed_passwordConfirmation.setEnabled(b);
    }

    @Override
    public void disableInputs() {
        setEnable(false);
    }

    @Override
    public void enableInputs() {
        setEnable(true);
    }

    @Override
    public void showProgress() {
        dialog.show();
    }

    @Override
    public void hideProgress() {
        dialog.dismiss();
    }

    @Override
    public void handleSignUp() {
        if (validateViews()){
            presenter.toSignUp(ed_name.getText().toString(),ed_surname.getText().toString(),ed_email.getText().toString(),ed_password.getText().toString(),ed_birthdate.getText().toString());
        }
    }

    @Override
    public boolean validateViews() {
        boolean retVal = true;
        if (TextUtils.isEmpty(ed_name.getText())){
            ed_name.setError("Este campo es obligatorio");
            retVal = false;
        }

        if(TextUtils.isEmpty(ed_surname.getText())){
            ed_surname.setError("Este campo es obligatorio");
            retVal = false;
        }

        if (TextUtils.isEmpty(ed_email.getText())){
            ed_email.setError("Este campo es obligatorio.");
            retVal = false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(ed_email.getText().toString().trim()).matches()){
            ed_email.setError("No es un mail válido.");
            retVal = false;
        }

        if (TextUtils.isEmpty(ed_password.getText())){
            ed_password.setError("Este campo es obligatorio.");
            retVal = false;
        }else if(ed_password.getText().toString().length() < 4){
            ed_password.setError("Debe tener al menos 4 caracteres.");
            retVal = false;
        }
        if (TextUtils.isEmpty(ed_passwordConfirmation.getText())){
            ed_passwordConfirmation.setError("Este campo es obligatorio.");
            retVal = false;
        }else if(!ed_password.getText().toString().trim().equals(ed_passwordConfirmation.getText().toString().trim())){
            ed_passwordConfirmation.setError("Las contraseñas no coinciden.");
            Log.d("PASSWORD_LOG",ed_password.getText()+" - "+ed_passwordConfirmation.getText());
            retVal = false;
        }

        return retVal;
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLogin() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
