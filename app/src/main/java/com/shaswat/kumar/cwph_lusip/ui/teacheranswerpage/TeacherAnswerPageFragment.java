package com.shaswat.kumar.cwph_lusip.ui.teacheranswerpage;

import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shaswat.kumar.cwph_lusip.LoginActivity;
import com.shaswat.kumar.cwph_lusip.R;
import com.shaswat.kumar.cwph_lusip.ui.home.HomeViewModel;
import com.shaswat.kumar.cwph_lusip.ui.model.Student_Data;

import java.util.ArrayList;

public class TeacherAnswerPageFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Student_Data> stu_data;
    private TeacherAnswerPageViewModel teacherAnswerPageViewModel;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog mDialog;

    public static TeacherAnswerPageFragment newInstance() {
        return new TeacherAnswerPageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_answer_page_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        recyclerView = view.findViewById(R.id.teacher_recycler_view);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Student_id");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        stu_data = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student_Question");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stu_data.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    Student_Data student_data = ds.getValue(Student_Data.class);

                    stu_data.add(student_data);
                }
                teacherAnswerPageViewModel = new TeacherAnswerPageViewModel(getActivity(),stu_data);

                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(teacherAnswerPageViewModel);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });




        return view;
    }

}
