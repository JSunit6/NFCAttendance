package com.studio.sds.nfcapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText useremail;
    private EditText userpass,conpass;
    private Button btnregister;
    private ProgressDialog regprogress;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth=FirebaseAuth.getInstance();

        regprogress=new ProgressDialog(this);

        useremail=(EditText)findViewById(R.id.email);
        userpass=(EditText)findViewById(R.id.password);
        conpass=(EditText)findViewById(R.id.confirmpassword);
        btnregister=(Button)findViewById(R.id.register_button);

        btnregister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view==btnregister)
        {
            registerUser();
        }
    }

    private void registerUser()
    {
        String email=useremail.getText().toString().trim();
        String pass=userpass.getText().toString().trim();
        String cpass=conpass.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            useremail.setError(getString(R.string.error_field_required));
            return;
        }
        if(TextUtils.isEmpty(pass))
        {
            userpass.setError(getString(R.string.error_field_required));
            return;
        }


        if(pass.equals(cpass))
        {
            regprogress.setMessage("Registering....");
            regprogress.show();

            firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                            regprogress.dismiss();
                            finish();
                            Intent myIntent=new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(myIntent);
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this,"Could not Register",Toast.LENGTH_SHORT).show();
                            regprogress.dismiss();
                        }

                    }
                });

        }

        else
        {
            Toast.makeText(RegisterActivity.this,"Passwords do not match!",Toast.LENGTH_LONG).show();
        }

    }
}