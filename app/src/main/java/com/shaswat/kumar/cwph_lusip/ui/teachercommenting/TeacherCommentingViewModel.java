package com.shaswat.kumar.cwph_lusip.ui.teachercommenting;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shaswat.kumar.cwph_lusip.R;
import com.shaswat.kumar.cwph_lusip.ui.home.HomeViewModel;
import com.shaswat.kumar.cwph_lusip.ui.model.Student_Data;
import com.shaswat.kumar.cwph_lusip.ui.model.Teacher_comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TeacherCommentingViewModel extends RecyclerView.Adapter<TeacherCommentingViewModel.HolderData> {

    private Context context;
    private ArrayList<Teacher_comment> teach_data;


    public TeacherCommentingViewModel(Context context, ArrayList<Teacher_comment> teach_data) {
        this.context = context;
        this.teach_data = teach_data;
    }


    @NonNull
    @Override
    public TeacherCommentingViewModel.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_content,parent,false);


        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TeacherCommentingViewModel.HolderData holder, int position) {

        final Teacher_comment teacher_comment = teach_data.get(position);
        final String mMessage,mName,url;
        final String times = teacher_comment.getTime();
        final String id = teacher_comment.getTime();
        final boolean[] bool = {false};
        final MediaPlayer[] player = {null};

        mName = "Counsellor Reply";
        mMessage = teacher_comment.getMessage();
        url = teacher_comment.getAudio();

        holder.title.setText(mName);
        holder.message.setText(mMessage);

        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Audio Playing...", Toast.LENGTH_SHORT).show();

                if (bool[0]==false) {
                    player[0] = new MediaPlayer();
                    try {
                        player[0].setDataSource(url);
                        player[0].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                            }
                        });
                        player[0].prepare();
                        bool[0]=true;

                    } catch (Exception e) {
                        Toast.makeText(context, "Can't play now, Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    player[0].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            holder.play.setText("CLICK TO PLAY AUDIO");
                            Toast.makeText(context, "Media Player stopped", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                if(!player[0].isPlaying()){
                    player[0].start();
                    holder.play.setText("PLAYING");
                }
                else{
                    player[0].pause();
                    holder.play.setText("PAUSED");

                }

            }
        });


        String time = DateUtils.formatDateTime(context, Long.parseLong(times), DateUtils.FORMAT_SHOW_TIME);
        String date =  DateUtils.formatDateTime(context, Long.parseLong(times), DateUtils.FORMAT_SHOW_DATE);
        String t = time + " " + date;
        holder.time.setText(t);

        //below is comment deleting

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                final ProgressDialog mDialog;
                                mDialog =new  ProgressDialog(context);

                                mDialog.setMessage("Deleting...");
                                mDialog.show();
                                int newPosition;
                                newPosition=holder.getAdapterPosition();
                                teach_data.remove(newPosition);
                                notifyItemRemoved(newPosition);
                                notifyItemRangeChanged(newPosition, teach_data.size());
                                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // File deleted successfully
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Teacher_reply").child(id);
                                        ref.removeValue();
                                        Toast.makeText(context, "Deleted...", Toast.LENGTH_SHORT).show();
                                        mDialog.dismiss();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        mDialog.dismiss();
                                        // Uh-oh, an error occurred!
                                        Toast.makeText(context, "Failure...", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return teach_data.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {

        private TextView title,message,time;
        private Button play;
        private Button delete;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message_student_content);
            play = itemView.findViewById(R.id.playButton);
            delete = itemView.findViewById(R.id.delete);
            time = itemView.findViewById(R.id.time_teacher_comment);

        }
    }

    // TODO: Implement the ViewModel
}
