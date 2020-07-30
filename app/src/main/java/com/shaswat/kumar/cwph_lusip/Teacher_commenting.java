package com.shaswat.kumar.cwph_lusip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.shaswat.kumar.cwph_lusip.ui.teachercommenting.TeacherCommentingFragment;

public class Teacher_commenting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_commenting_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, TeacherCommentingFragment.newInstance())
                    .commitNow();
        }
    }
}
