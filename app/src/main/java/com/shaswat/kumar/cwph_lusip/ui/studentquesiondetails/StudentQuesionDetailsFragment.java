package com.shaswat.kumar.cwph_lusip.ui.studentquesiondetails;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shaswat.kumar.cwph_lusip.R;
import com.shaswat.kumar.cwph_lusip.ui.model.Teacher_comment;
import com.shaswat.kumar.cwph_lusip.ui.teachercommenting.TeacherCommentingViewModel;

import java.util.ArrayList;

public class StudentQuesionDetailsFragment extends Fragment {


    private Button playbutton;
    private TextView messagetxt;


    private ProgressDialog mDialog;

    FirebaseAuth auth;

    private StudentQuesionDetailsViewModel mViewModel;
    MediaPlayer mediaPlayer ;

    EditText txtmsg;

    AlertDialog.Builder mdialog;
    AlertDialog alertDialog;

    private StorageReference mStrorage;
    private DatabaseReference mReference;
    FirebaseUser mUser;


    private ProgressDialog mProgressDialog,comments_dialog;

    private RecyclerView comments_recycle;
    private ArrayList<Teacher_comment> teach_comments;

    public static StudentQuesionDetailsFragment newInstance() {
        return new StudentQuesionDetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_quesion_details_fragment, container, false);

        Intent intent = getActivity().getIntent();

        final String id = intent.getStringExtra("Uid");
        mDialog  = new ProgressDialog(getContext());
        mDialog.show();


        mStrorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(getActivity());
        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();

        comments_dialog = new ProgressDialog(getActivity());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Student_Question").child(id);

        DatabaseReference urlref = ref.child("audio");
        DatabaseReference messageref  = ref.child("message");
        final String[] url = new String[1];

        final String[] message = new String[1];


        urlref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                url[0] = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        messagetxt = view.findViewById(R.id.message_teacher);
        messageref.keepSynced(true);

        messageref.getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                message[0] = dataSnapshot.getValue(String.class);
                messagetxt.setText(message[0]);
                mDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        playbutton = view.findViewById(R.id.playButtonTeacher);

        final MediaPlayer[] player = {null};
        final boolean[] bool = {false};
        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Audio Playing...", Toast.LENGTH_SHORT).show();

                if (bool[0] ==false){
                    player[0] = new MediaPlayer();

                    try{
                        player[0].setDataSource(url[0]);
                        player[0].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();

                            }
                        });
                        player[0].prepare();
                        bool[0] =true;
                    }catch(Exception e){
                        Toast.makeText(getActivity(), "Can't play now, Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    player[0].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playbutton.setText("PLAY THE AUDIO");
                            Toast.makeText(getContext(), "Media Player stopped", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if(!player[0].isPlaying()){
                    player[0].start();
                    playbutton.setText("PLAYING");
                }
                else{
                    player[0].pause();
                    playbutton.setText("PAUSED");

                }

            }
        });

        //recyclerview for comments below;
        comments_recycle = view.findViewById(R.id.commentsrecyclerview);

        mDialog.setMessage("loading...");
        comments_dialog.show();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setStackFromEnd(true);


        comments_recycle.setHasFixedSize(true);
        comments_recycle.setLayoutManager(mLayoutManager);

        teach_comments = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Teacher_reply");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teach_comments.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){


                    Teacher_comment teacher_comment = ds.getValue(Teacher_comment.class);
                    if(teacher_comment.getComId().equals(id)){
                        teach_comments.add(teacher_comment);
                    }

                }
                mViewModel = new StudentQuesionDetailsViewModel(getActivity(),teach_comments);
                comments_recycle.setAdapter(mViewModel);
                comments_dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                comments_dialog.dismiss();

            }
        });




        return view;
    }



}
