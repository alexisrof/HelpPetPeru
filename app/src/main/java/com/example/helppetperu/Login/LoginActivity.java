package com.example.helppetperu.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.helppetperu.Class.Persona;
import com.example.helppetperu.TabFragments.HomeActivity;
import com.example.helppetperu.R;
import com.example.helppetperu.SignUp.SignUpActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Arrays;
import java.util.Objects;

import mehdi.sakout.fancybuttons.FancyButton;

public class LoginActivity extends AppCompatActivity implements LoginInterface.View{

    private MaterialEditText ed_email, ed_password;
    private FancyButton btn_login;
    private MaterialDialog dialog;
    private LoginInterface.Presenter presenter;

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setViews();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
        FacebookLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            Ingresar();
        }
    }

    public void FacebookLogin(){
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email","public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    public void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Persona persona = new Persona(mAuth.getCurrentUser().getEmail(),"","","",0);
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("persona");
                    database.child(mAuth.getCurrentUser().getUid()).setValue(persona);
                    Ingresar();
                    Toast.makeText(LoginActivity.this,"Bienvenido",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this,"FAIL!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Ingresar(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void setViews(){
        presenter = new LoginPresenter(this);
        ed_email = findViewById(R.id.EditText_LoginEmail);
        ed_password = findViewById(R.id.EditText_LoginPassword);
        btn_login = findViewById(R.id.Button_Login);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Cargando")
                .content("Espere, por favor ...")
                .cancelable(false)
                .progress(true,0);
        dialog = builder.build();
    }

    private void setInputs(boolean enable){
        ed_email.setEnabled(enable);
        ed_password.setEnabled(enable);
        btn_login.setEnabled(enable);
    }

    @Override
    public void disableInputs() {
        setInputs(false);
    }

    @Override
    public void enableInputs() {
        setInputs(true);
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
    public void handleLogin() {
        if(!isValidEmail()){
            Toast.makeText(this,"No es un email válido", Toast.LENGTH_SHORT).show();
        } else if (!isValidPassword()){
            Toast.makeText(this,"No es un password válido", Toast.LENGTH_SHORT).show();
        }else{
            presenter.toLogin(Objects.requireNonNull(ed_email.getText()).toString().trim(), Objects.requireNonNull(ed_password.getText()).toString().trim());
        }
    }

    @Override
    public boolean isValidEmail() {
        return Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(ed_email.getText()).toString().trim()).matches();
    }

    @Override
    public boolean isValidPassword() {
        if(TextUtils.isEmpty(Objects.requireNonNull(ed_password.getText()).toString().trim()) || ed_password.getText().toString().trim().length() < 4){
            Toast.makeText(this,"No es una contraseña válida", Toast.LENGTH_SHORT).show();
            ed_password.setError("No es una contraseña válida");
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onLogin() {
        Toast.makeText(this,"Se realizó el login de forma correcta", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this,error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public void register(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
