package com.example.helppetperu.TabFragments.Pet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.helppetperu.Class.LostPet;
import com.example.helppetperu.Class.Pet;
import com.example.helppetperu.Class.ReportLocation;
import com.example.helppetperu.R;
import com.example.helppetperu.TabFragments.Pet.Registry.RegistryPetFragment;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public class PetFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    double latitude;
    double longitude;
    LinearLayoutManager linearLayoutManager;

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase, refLostPets;

    PetRecyclerViewAdapter petRecyclerViewAdapter;

    ArrayList<Pet> PetList = new ArrayList<>();
    ArrayList<String> KeyList = new ArrayList<>();

    public PetFragment() {
    }

    public static PetFragment newInstance(String param1, String param2) {
        PetFragment fragment = new PetFragment();
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
        View view = inflater.inflate(R.layout.fragment_pet, container, false);
        setViews(view);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getCurrentLocation();
        }

        /*
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_COD);
        }
        */

        petRecyclerViewAdapter = new PetRecyclerViewAdapter(view.getContext(), PetList);
        recyclerView.setAdapter(petRecyclerViewAdapter);

        petRecyclerViewAdapter.setOnClickListener(new PetRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onReportClick(int position) {
                Toast.makeText(getContext(), "ReportClick", Toast.LENGTH_SHORT).show();
                UpdateLostPet(PetList.get(position));
            }

            @Override
            public void onRemoveClick(int position) {
                Toast.makeText(getContext(), "RemoveClick", Toast.LENGTH_SHORT).show();
                DeletePet(PetList.get(position));
            }
        });

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Pet pet;
                pet = snapshot.getValue(Pet.class);
                PetList.add(0, pet);
                KeyList.add(0, snapshot.getKey());
                petRecyclerViewAdapter.notifyDataSetChanged();
                Log.d("PetFragment", "onChildAdded");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int index = KeyList.indexOf(snapshot.getKey());
                Pet pet = snapshot.getValue(Pet.class);
                PetList.remove(index);
                KeyList.remove(index);
                PetList.add(0,pet);
                KeyList.add(0,snapshot.getKey());
                petRecyclerViewAdapter.notifyDataSetChanged();
                Log.d("PetFragment", "onChildChanged");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int index = KeyList.indexOf(snapshot.getKey());
                KeyList.remove(index);
                PetList.remove(index);
                petRecyclerViewAdapter.notifyItemRemoved(index);
                Log.d("PetFragment", "onChildRemoved");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(this);
                if(locationResult != null && locationResult.getLocations().size()>0){
                    int lastLocationIndex = locationResult.getLocations().size() -1;
                    latitude = locationResult.getLocations().get(lastLocationIndex).getLatitude();
                    longitude = locationResult.getLocations().get(lastLocationIndex).getLongitude();
                    Log.i("LOCATIONSERVICE",latitude+" ; "+longitude);
                }
            }
        }, Looper.getMainLooper());
    }

    private void setViews(View view) {
        FloatingActionButton floatingActionButton = view.findViewById(R.id.button_AddPet);
        floatingActionButton.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("pet").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mDatabase.keepSynced(true);

        recyclerView = view.findViewById(R.id.PetRecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void UpdateLostPet(Pet pet){
        Pet petUpdate = new Pet(pet.getId(),pet.getNombre(),pet.getNacimiento(),pet.getDetalle(),pet.getTipoMascota(),pet.getOrigenMascota(),"Perdido",pet.getImgUrl(),pet.getPropietario());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReferencePet = FirebaseDatabase.getInstance().getReference();
        databaseReferencePet.child("pet").child(user.getUid()).child(petUpdate.getId()).setValue(petUpdate);

        LostPet lostPet = new LostPet(petUpdate.getId(),petUpdate.getNombre(),petUpdate.getNacimiento(),petUpdate.getDetalle(),petUpdate.getTipoMascota(),petUpdate.getOrigenMascota(),petUpdate.getEstadoMascota(),petUpdate.getImgUrl(),petUpdate.getPropietario(),new ArrayList<ReportLocation>());
        lostPet.addElement(new ReportLocation(latitude,longitude,user.getUid()));

        DatabaseReference databaseReferenceLost = FirebaseDatabase.getInstance().getReference();
        databaseReferenceLost.child("lostpets").child(lostPet.getId()).setValue(lostPet);
    }

    public void DeletePet(Pet pet){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("pet");
        databaseReference.child(user.getUid()).child(pet.getId()).removeValue();
        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(pet.getImgUrl());
        photoRef.delete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_AddPet:
                RegistryPetFragment registryPetFragment = new RegistryPetFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.Frame_FragmentsPageContainer, registryPetFragment);
                fragmentTransaction.commit();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getCurrentLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

