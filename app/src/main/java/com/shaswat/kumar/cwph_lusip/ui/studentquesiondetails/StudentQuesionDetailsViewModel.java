package com.shaswat.kumar.cwph_lusip.ui.studentquesiondetails;

import android.content.Context;
import android.media.MediaPlayer;
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

import com.shaswat.kumar.cwph_lusip.R;
import com.shaswat.kumar.cwph_lusip.ui.model.Teacher_comment;
import com.shaswat.kumar.cwph_lusip.ui.teachercommenting.TeacherCommentingViewModel;

import java.util.ArrayList;

public class StudentQuesionDetailsViewModel  extends RecyclerView.Adapter<StudentQuesionDetailsViewModel.HolderData> {

    private Context context;
    private ArrayList<Teacher_comment> teach_data;


    public StudentQuesionDetailsViewModel(Context context, ArrayList<Teacher_comment> teach_data) {
        this.context = context;
        this.teach_data = teach_data;
    }


    @NonNull
    @Override
    public StudentQuesionDetailsViewModel.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_comment_content,parent,false);


        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentQuesionDetailsViewModel.HolderData holder, int position) {

        final Teacher_comment teacher_comment = teach_data.get(position);
        final String mMessage,mName,url;
        final String times = teacher_comment.getTime();
        final String id = teacher_comment.getTime();
        final boolean[] bool = {false};
        final MediaPlayer[] player = {null};

        mName = "Counsellor";
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

    }


    @Override
    public int getItemCount() {
        return teach_data.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        private TextView title,message,time;
        private Button play;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message_student_content);
            play = itemView.findViewById(R.id.playButton);
            time = itemView.findViewById(R.id.time_teacher_comment);

        }
    }
}
