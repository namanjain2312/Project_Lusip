package com.shaswat.kumar.cwph_lusip.ui.tools;

import android.content.Intent;
import android.net.Uri;
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

public class ToolsFragment extends Fragment {

    Button buttonted,buttonachieve,buttonheal,buttonsuicide,buttonsu;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        buttonted = root.findViewById(R.id.gototed);
        buttonachieve = root.findViewById(R.id.gotoachieve);
        buttonheal = root.findViewById(R.id.gotoheal);
        buttonsuicide = root.findViewById(R.id.gotosuicide);
        buttonsu = root.findViewById(R.id.goToSu);

        //click listeners below

        buttonted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gototed();
            }
        });
        buttonachieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoachieve();
            }
        });
        buttonheal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoheal();
            }
        });
        buttonsuicide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotosuicide();
            }
        });
        buttonsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSu();
            }
        });


        return root;
    }

    public void gototed () {
        goToUrl ( "https://www.ted.com/talks");
    }
    public void gotoachieve () {
        goToUrl ( "https://achievement.org/");
    }
    public void gotoheal () {
        goToUrl ( "https://www.healyourlife.com/");
    }
    public void gotosuicide () {
        goToUrl ( "https://www.happiness.com/en/magazine/health-body/eight-powerful-suicide-prevention-quotes/");
    }
    public void goToSu () {
        goToUrl ( "https://www.success.com/?s=personal+development");
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}