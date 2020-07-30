package com.shaswat.kumar.cwph_lusip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shaswat.kumar.cwph_lusip.ui.model.StudentId;

public class RegisterActivity extends AppCompatActivity {
    private TextView changableTextView;
    private TextInputLayout emailtextinputlayout,passwordtextinputlayout;
    private EditText passwordtext,emailtext;
    private Button loginButton;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    TextView reg_act;

    ProgressDialog mDialog;

    private DatabaseReference mref;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#a80008"));

        mDialog = new ProgressDialog(this);


        actionBar.setBackgroundDrawable(colorDrawable);

        emailtextinputlayout= findViewById(R.id.teacheremaileditTextReg);
        passwordtextinputlayout=findViewById(R.id.teacherpasswordeditTextReg);
        emailtext=(EditText)findViewById(R.id.emailtextReg);
        passwordtext=(EditText)findViewById(R.id.passwordtextReg);
        loginButton=(Button)findViewById(R.id.teacherloginButtonReg);
        saveLoginCheckBox = (CheckBox)findViewById(R.id.logincheckBoxReg);
        loginPreferences = getSharedPreferences("loginPrefsReg", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        reg_act = findViewById(R.id.register_clickReg);


        reg_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });



        /*if(saveLoginCheckBox.isChecked()){
            saveLoginCheckBox.setTextColor(Color.RED);
            saveLoginCheckBox.setBackgroundColor(Color.parseColor("#cbff75"));
        }*/

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            emailtext.setText(loginPreferences.getString("email", ""));
            passwordtext.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

//        changableTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent=new Intent(LoginActivity.this,Teacher_Login.class);
//                startActivity(intent);
//            }
//        });



        auth=FirebaseAuth.getInstance();





        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEmailAddress();
                validatePassword();
                login();
            }
        });

    }



    private void login(){

        String email=emailtextinputlayout.getEditText().getText().toString().trim();
        String password=passwordtextinputlayout.getEditText().getText().toString().trim();

        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("email", email);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){

            Toast.makeText(getApplicationContext(),"Enter email and password",Toast.LENGTH_LONG).show();
            mDialog.dismiss();
        }
        else {
            mDialog.setMessage("Processing..");
            mDialog.show();

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {


                                final String timestamp = ""+System.currentTimeMillis();
                                StudentId studentId = new StudentId(auth.getUid(),timestamp);
                                mref = FirebaseDatabase.getInstance().getReference("Student_id");

                                mref.child(timestamp).setValue(studentId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        mDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Sign Up complete", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                    }
                                });





                            } else {
                                mDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, ""+ task.getException(), Toast.LENGTH_SHORT).show();

                            }


                        }
                    });


        }
    }
    private boolean validateEmailAddress(){
        String email=emailtextinputlayout.getEditText().getText().toString().trim();
        if(email.isEmpty()){
            emailtextinputlayout.setError("Email is required.Can't be empty.");
            return false;
        }
        else {
            emailtextinputlayout.setError(null);
            return   true;
        }

    }
    private boolean validatePassword(){
        String password=passwordtextinputlayout.getEditText().getText().toString().trim();
        if(password.isEmpty()){
            passwordtextinputlayout.setError("Email is required.Can't be empty.");
            return false;
        }
        else {
            passwordtextinputlayout.setError(null);
            return true;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }
}
