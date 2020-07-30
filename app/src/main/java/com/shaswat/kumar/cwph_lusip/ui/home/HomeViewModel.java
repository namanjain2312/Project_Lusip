package com.shaswat.kumar.cwph_lusip.ui.home;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.shaswat.kumar.cwph_lusip.R;
import com.shaswat.kumar.cwph_lusip.Student_quesion_details;
import com.shaswat.kumar.cwph_lusip.ui.model.Student_Data;


import java.util.ArrayList;

public class HomeViewModel extends RecyclerView.Adapter<HomeViewModel.HolderData>{

    private Context context;
    private ArrayList<Student_Data> stu_data;


    public HomeViewModel(Context context, ArrayList<Student_Data> stu_data) {
        this.context = context;
        this.stu_data = stu_data;
    }
    @NonNull
    @Override
    public HomeViewModel.HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.student_recycler_content,parent,false);


        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeViewModel.HolderData holder, int position) {

        final Student_Data student_data = stu_data.get(position);
        final String mMessage,mName,url;
        final String id = student_data.getTime();//timestamp is the id
        final boolean[] bool = {false};
        final MediaPlayer[] player = {null};


        mName = "Anonymous Question";
        mMessage = student_data.getMessage();
        url = student_data.getAudio();

        holder.title.setText(mName);
        holder.message.setText(mMessage);
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Audio Playing...", Toast.LENGTH_SHORT).show();

                if (bool[0] ==false) {
                    player[0] = new MediaPlayer();

                    try{
                        player[0].setDataSource(url);
                        player[0].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                            }
                        });
                        player[0].prepare();
                        bool[0] =true;
                    }catch(Exception e){
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

        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Student_quesion_details.class);
                intent.putExtra("Uid",id);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return stu_data.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {

        private TextView title,message;
        private Button play;
        private TextView click;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message_student_content);
            play = itemView.findViewById(R.id.playButton);
            click = itemView.findViewById(R.id.student_reply);
        }
    }
}