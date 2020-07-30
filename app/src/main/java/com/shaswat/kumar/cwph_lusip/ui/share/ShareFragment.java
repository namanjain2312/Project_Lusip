package com.shaswat.kumar.cwph_lusip.ui.share;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.shaswat.kumar.cwph_lusip.R;

public class ShareFragment extends Fragment {

        Button button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_share, container, false);

        button = root.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //have to add our app address
                Intent sharingintent=new Intent(Intent.ACTION_SEND);
                sharingintent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=com.zhiliaoapp.musically");
                sharingintent.setType("text/plain");
                startActivity(Intent.createChooser(sharingintent,"Share Using"));

            }
        });

        return root;
    }
}