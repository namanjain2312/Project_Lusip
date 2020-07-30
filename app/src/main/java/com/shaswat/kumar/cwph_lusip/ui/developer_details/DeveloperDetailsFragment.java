package com.shaswat.kumar.cwph_lusip.ui.developer_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.shaswat.kumar.cwph_lusip.R;

public class DeveloperDetailsFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.developer_details, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);

        return root;
    }
}