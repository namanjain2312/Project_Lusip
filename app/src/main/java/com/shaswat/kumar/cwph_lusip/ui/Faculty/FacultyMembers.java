package com.shaswat.kumar.cwph_lusip.ui.Faculty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.shaswat.kumar.cwph_lusip.R;

public class FacultyMembers extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.faculty_member, container, false);

        return root;
    }
}