package com.shaswat.kumar.cwph_lusip.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shaswat.kumar.cwph_lusip.R;
import com.shaswat.kumar.cwph_lusip.ui.model.Student_Data;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Student_Data> stu_data;
    private HomeViewModel homeAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    String uid;

    ProgressDialog mDialog;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        

        recyclerView = root.findViewById(R.id.student_recycler);


        stu_data = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student_Question");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stu_data.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    Student_Data student_data = ds.getValue(Student_Data.class);

                    if(student_data.getId().equals(mUser.getUid()))
                            stu_data.add(student_data);
                }
                homeAdapter = new HomeViewModel(getActivity(),stu_data);
                final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(homeAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}