package com.shaswat.kumar.cwph_lusip.ui.teachercommenting;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shaswat.kumar.cwph_lusip.MainActivity;
import com.shaswat.kumar.cwph_lusip.R;
import com.shaswat.kumar.cwph_lusip.ui.model.Student_Data;
import com.shaswat.kumar.cwph_lusip.ui.model.Teacher_comment;
import com.shaswat.kumar.cwph_lusip.ui.teacheranswerpage.TeacherAnswerPageViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class TeacherCommentingFragment extends Fragment {

    private TeacherCommentingViewModel mViewModel;
    private Button playbutton;
    private TextView messagetxt;

    private Button comment_btn;

    private ProgressDialog mDialog;

    FirebaseAuth auth;

    Button buttonStart, buttonStop, buttonPlayLastRecordAudio,
            buttonStopPlayingRecording ;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
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

    public static TeacherCommentingFragment newInstance() {
        return new TeacherCommentingFragment();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_commenting_fragment, container, false);
        Intent intent = getActivity().getIntent();

        final String id = intent.getStringExtra("Uid");
        mDialog  = new ProgressDialog(getContext());
        mDialog.show();

        comment_btn = view.findViewById(R.id.teacher_reply);
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
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setStackFromEnd(true);




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
                mViewModel = new TeacherCommentingViewModel(getActivity(),teach_comments);
                comments_recycle.setHasFixedSize(true);
                comments_recycle.setLayoutManager(mLayoutManager);
                comments_recycle.setAdapter(mViewModel);
                comments_dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                comments_dialog.dismiss();

            }
        });


        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comemntFeatures(id);
            }
        });




        return view;


    }

    //after pressing the comment button in teacher section
    private void comemntFeatures(final String id) {

        View mview = getLayoutInflater().inflate(R.layout.content_audio_record_dialog,null);

        buttonStart =  mview.findViewById(R.id.button);
        buttonStop = mview.findViewById(R.id.button2);
        buttonPlayLastRecordAudio = mview.findViewById(R.id.button3);
        buttonStopPlayingRecording = mview.findViewById(R.id.button4);
        txtmsg = mview.findViewById(R.id.text_message);

        mdialog = new AlertDialog.Builder(getActivity());

        mdialog.setTitle("Reply....");
        mdialog.setView(mview);
        alertDialog = mdialog.create();
        alertDialog.show();

        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);

        random = new Random();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(26) + "AudioRecording.3gp";



                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);

                    Toast.makeText(getActivity(), "Recording started",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    requestPermission();
                }

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getActivity(), "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(true);

                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                    buttonPlayLastRecordAudio.setText("PAUSE");
                }
                else{
                    mediaPlayer.pause();
                    buttonPlayLastRecordAudio.setText("PLAY");

                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        buttonPlayLastRecordAudio.setText("PLAY");
                        Toast.makeText(getContext(), "Media Player stopped", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "Played Liked it or record again click record button", Toast.LENGTH_LONG).show();
                    }
                });




                Toast.makeText(getContext(), AudioSavePathInDevice , Toast.LENGTH_SHORT).show();

            }
        });


        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgressDialog.setMessage("Sending Message");
                mProgressDialog.show();


                final String message = txtmsg.getText().toString();
                final String timestamp = ""+System.currentTimeMillis();

                StorageReference filePath = mStrorage.child("Teacher_audio").child(CreateRandomAudioFileName(26));

                Uri uri = Uri.fromFile(new File(AudioSavePathInDevice));

                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downloadVideoUri = uriTask.getResult();

                        if(uriTask.isSuccessful()){

                            Teacher_comment teacher_comment = new Teacher_comment(downloadVideoUri.toString(),mUser.getUid(),message,timestamp,id);

                            mReference = FirebaseDatabase.getInstance().getReference("Teacher_reply");
                            mReference.child(timestamp).setValue(teacher_comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    mProgressDialog.dismiss();
                                    Toast.makeText(getContext(), "Upload Successful", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            });
                        }



                        //now we have to store this to firebase database model class will be
                        //id (the teacher who will reply will also have the same id)
                        //audio link for streaming
                        //text Message


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getContext(), "File upload to storage Failed", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });


                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });


    }
    private void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    private String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(getActivity(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }



    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
