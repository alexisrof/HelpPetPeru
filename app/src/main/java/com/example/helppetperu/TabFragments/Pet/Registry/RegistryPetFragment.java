package com.example.helppetperu.TabFragments.Pet.Registry;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.helppetperu.Class.Pet;
import com.example.helppetperu.R;
import com.example.helppetperu.TabFragments.Pet.PetFragment;
import com.example.helppetperu.Util.DateMask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

import mehdi.sakout.fancybuttons.FancyButton;


public class RegistryPetFragment extends Fragment implements View.OnClickListener, RegistryPetInterface.View{


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    FancyButton formButton;
    ShapeableImageView petImage;
    public Uri imguri;
    MaterialEditText nombre,detalle,dob;
    MaterialSpinner spinnerType,spinnerOrigin;
    MaterialDialog dialog;
    RegistryPetInterface.Presenter presenter;
    private StorageReference mStorageRef;
    String imagenUrl;

    public RegistryPetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public static RegistryPetFragment newInstance(String param1, String param2) {
        RegistryPetFragment fragment = new RegistryPetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registry_pet, container, false);
        setViews(v);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return v;
    }

    private void FileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    private String getExtension(Uri uri){
        ContentResolver cr= Objects.requireNonNull(getActivity()).getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode== androidx.fragment.app.FragmentActivity.RESULT_OK && data!=null && data.getData()!=null){
            imguri = data.getData();
            petImage.setImageURI(imguri);
        }
    }

    private void setViews(View v){
        presenter = new RegistryPetPresenter(this);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(v.getContext())
                .title("Registrando el formulario")
                .content("Espere por favor...")
                .cancelable(false)
                .progress(false,100);
        dialog = builder.build();

        //INITIALIZE VARIABLE
        nombre = v.findViewById(R.id.EditText_PetName);
        detalle = v.findViewById(R.id.EditText_PetDescription);

        //SPINNER DROPDOWN LIST
        spinnerType = v.findViewById(R.id.spinnerPetType);
        spinnerType.setItems("Seleccionar tipo","Perro","Gato","Hamster","Pig","Conejo");
        spinnerType.setSelectedIndex(0);
        spinnerType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                if(spinnerType.getSelectedIndex()!=0){
                    spinnerType.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }else{
                    spinnerType.setTextColor(getResources().getColor(R.color.colorRed));
                }
            }
        });

        spinnerOrigin = v.findViewById(R.id.spinnerPetOrigin);
        spinnerOrigin.setItems("Seleccionar origen","Propia","Rescatada","Encontrada");
        spinnerOrigin.setSelectedIndex(0);
        spinnerOrigin.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                if(spinnerOrigin.getSelectedIndex()!=0){
                    spinnerOrigin.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }else{
                    spinnerOrigin.setTextColor(getResources().getColor(R.color.colorRed));
                }
            }
        });

        dob = v.findViewById(R.id.EditText_PetBirthdate);
        dob.addTextChangedListener(new DateMask(new DateMask.DateMaskingCallback() {
            @Override
            public void dateOfBirthValidation(boolean isValid, String dateOfBirth, String error) throws Exception {
                if (isValid) {
                    dob.setText(dateOfBirth);
                    dob.setSelection(dateOfBirth.length());
                } else {
                    dob.setText(dateOfBirth);
                    dob.setSelection(dateOfBirth.length());
                    dob.setError(error);
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        },"/"));

        formButton = v.findViewById(R.id.Button_PetForm);
        formButton.setOnClickListener(this);

        petImage = v.findViewById(R.id.Shape_Image_pet);
        petImage.setOnClickListener(this);
    }

    private void setEnable(Boolean b){
        nombre.setEnabled(b);
        detalle.setEnabled(b);
        dob.setEnabled(b);
        spinnerType.setEnabled(b);
        spinnerOrigin.setEnabled(b);
        formButton.setEnabled(b);
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
    public void handlePetForm() {
        if(isValidateForm() && isValidateImage()){
            presenter.toPetForm(nombre.getText().toString(),detalle.getText().toString(),dob.getText().toString(),spinnerType.getSelectedIndex(),spinnerOrigin.getSelectedIndex(),imagenUrl);
        }
    }

    @Override
    public void handlePetImage() {
        if(isValidateImage() && isValidateForm()){
            disableInputs();
            showProgress();
            onImage();
        }
    }

    @Override
    public boolean isValidateImage() {
        boolean retVal = true;
        if(imguri == null){
            Toast.makeText(getActivity(), "Selecciona una imagen", Toast.LENGTH_SHORT).show();
            retVal = false;
        }
        return retVal;
    }

    @Override
    public boolean isValidateForm() {
        boolean retVal = true;
        if(TextUtils.isEmpty(nombre.getText())){
            nombre.setError("Campo obligatorio");
            retVal = false;
        }else{
            nombre.setError(null);
        }
        if(TextUtils.isEmpty(detalle.getEditableText())){
            detalle.setError("Campo obligatorio");
            retVal = false;
        }else if(detalle.getText().length()>150){
            onError("Máximo 150 caracteres");
            retVal = false;
        }else{
            detalle.setError(null);
        }
        if(TextUtils.isEmpty(dob.getText())){
            dob.setError("Campo obligatorio");
            retVal = false;
        }else{
            dob.setError(null);
        }
        if(TextUtils.isEmpty(detalle.getText())){
            dob.setError("Campo obligatorio");
            retVal = false;
        }else{
            detalle.setError(null);
        }
        if(spinnerType.getSelectedIndex() == 0){
            spinnerType.setError("Selecciona un tipo");
            retVal = false;
        }else{
            spinnerType.setError(null);
        }
        if(spinnerOrigin.getSelectedIndex() == 0){
            spinnerOrigin.setError("Selecciona un origen");
            retVal = false;
        }else{
            spinnerOrigin.setError(null);
        }
        return retVal;
    }

    @Override
    public void onPetForm() {
        PetFragment petFragment = new PetFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.Frame_FragmentsPageContainer, petFragment);
        fragmentTransaction.commit();
        onDestroy();
    }

    @Override
    public void onImage() {
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Images");
        final StorageReference Ref = mStorageRef.child(System.currentTimeMillis()+"."+getExtension(imguri));
        Ref.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imagenUrl = uri.toString();
                        handlePetForm();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                dialog.setProgress((int) progress);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onError(e.getMessage());
            }
        });
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.Button_PetForm:
                handlePetImage();
                break;
            case R.id.Shape_Image_pet:
                FileChooser();
                break;
        }
    }

}