package com.example.helppetperu.TabFragments.Help;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helppetperu.Class.LostPet;
import com.example.helppetperu.Class.Pet;
import com.example.helppetperu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class LostPetTabFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;

    LinearLayoutManager linearLayoutManager;
    LostPetRecyclerViewAdapter lostPetRecyclerViewAdapter;

    ArrayList<LostPet> LostPetList = new ArrayList<>();
    ArrayList<String> KeyList = new ArrayList<>();

    public LostPetTabFragment() {
    }

    public static LostPetTabFragment newInstance(String param1, String param2) {
        LostPetTabFragment fragment = new LostPetTabFragment();
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
        View view = inflater.inflate(R.layout.fragment_lost_pet_tab, container, false);
        setViews(view);
        lostPetRecyclerViewAdapter = new LostPetRecyclerViewAdapter(getContext(),LostPetList);
        recyclerView.setAdapter(lostPetRecyclerViewAdapter);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LostPet lostPet;
                lostPet = snapshot.getValue(LostPet.class);
                LostPetList.add(0, lostPet);
                KeyList.add(0, snapshot.getKey());
                lostPetRecyclerViewAdapter.notifyDataSetChanged();
                Log.d("LostPetFragment", "onChildAdded");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

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

    private void setViews(View view) {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("lostpets");
        mDatabase.keepSynced(true);

        recyclerView = view.findViewById(R.id.PetLostRecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}