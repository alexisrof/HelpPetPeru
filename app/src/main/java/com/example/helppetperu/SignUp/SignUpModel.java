package com.example.helppetperu.SignUp;

import androidx.annotation.NonNull;

import com.example.helppetperu.Class.Persona;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpModel implements  SignUpInterface.Model{

    private SignUpInterface.TaskListener listener;
    private FirebaseAuth auth;
    private DatabaseReference database;

    public SignUpModel(SignUpInterface.TaskListener listener) {
        this.listener = listener;
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("persona");
    }

    @Override
    public void doSignUp(final String name, final String surname, final String email, String password, final String birthdate) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();
                    FirebaseUser user = auth.getCurrentUser();
                    Persona persona = new Persona(email,name,surname,birthdate,0);
                    database.child(user.getUid()).setValue(persona);
                    listener.onSuccess();
                    if(user != null){
                        user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                }else if(task.getException() != null){
                                    listener.onError(task.getException().getMessage());
                                }
                            }
                        });
                    }else{
                        //
                    }
                }else if(task.getException() != null){
                    listener.onError(task.getException().getMessage());
                }
            }
        });
    }
}
