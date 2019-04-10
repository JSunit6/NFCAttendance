package com.studio.sds.nfcapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Loader;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{

    private TextView txtreg,txtforgotpass;
    private FirebaseAuth firebaseAuth;
    private EditText email;
    private EditText pass;
    private Button btnlogin;
    private ProgressDialog logindialog;
    private String useremail;
    private String upass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();

        logindialog=new ProgressDialog(this);

        txtforgotpass=(TextView)findViewById(R.id.forgotpass);
        txtreg=(TextView)findViewById(R.id.registertext);
        email=(EditText)findViewById(R.id.email) ;
        pass=(EditText)findViewById(R.id.password) ;
        btnlogin=(Button)findViewById(R.id.login_button);



        btnlogin.setOnClickListener(this);
        txtreg.setOnClickListener(this);
        txtforgotpass.setOnClickListener(this);
    }

    public String getEmail()
    {
        return email.getText().toString().trim();
    }

    public String getPass()
    {
        return pass.getText().toString().trim();
    }

    @Override
    public void onClick(View view)
    {
        if(view==btnlogin)
        {
            Log.w("G","HI");
            login();
        }
        else if(view==txtforgotpass)
        {
            forgotpassword();
        }
        else if(view==txtreg)
        {
            Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(myIntent);
        }
    }

    private void forgotpassword()
    {
        useremail=getEmail();

        if(TextUtils.isEmpty(useremail))
        {
            email.setError("Email Required");
        }
        else
        {
            logindialog.setMessage("Sending email to reset password");
            logindialog.show();
            firebaseAuth.sendPasswordResetEmail(useremail)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Email Successfully Sent", Toast.LENGTH_LONG).show();
                                logindialog.dismiss();
                                finish();
                                Intent myintent = new Intent(LoginActivity.this, LoginActivity.class);
                                startActivity(myintent);
                            } else {
                                logindialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Unable to send email, please check your email-id", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }


    private void login()
    {
        useremail=getEmail();
        upass=getPass();
        if(TextUtils.isEmpty(useremail))
        {
            email.setError("Email Required");
            return;
        }
        else if(TextUtils.isEmpty(upass))
        {
            pass.setError("Password Required");
            return;
        }
        else
        {
            if(useremail.equals("Admin") && upass.equals("Admin"))
            {
                startActivity(new Intent(LoginActivity.this,AdminPanelActivity.class));
            }
            else
            {
                logindialog.setMessage("Loggin In...");
                logindialog.show();
                firebaseAuth.signInWithEmailAndPassword(useremail, upass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    logindialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_LONG).show();
                                    finish();
                                    Intent myIntent = new Intent(LoginActivity.this, ProfileActivity.class);
                                    startActivity(myIntent);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                                    logindialog.dismiss();
                                }
                            }
                        });
            }
        }
    }
}
