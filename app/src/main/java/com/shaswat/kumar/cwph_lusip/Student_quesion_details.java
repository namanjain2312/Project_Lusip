package com.shaswat.kumar.cwph_lusip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.shaswat.kumar.cwph_lusip.ui.studentquesiondetails.StudentQuesionDetailsFragment;

public class Student_quesion_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_quesion_details_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, StudentQuesionDetailsFragment.newInstance())
                    .commitNow();
        }
    }
}
