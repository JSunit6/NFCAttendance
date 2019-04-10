package com.studio.sds.nfcapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class ModifySubjectsFragment extends Fragment implements View.OnClickListener, Spinner.OnItemSelectedListener{

    private ScrollView subscrollview;
    private Button btnAdd,btnclear;
    private AutoCompleteTextView txtnoofsub,txtemail;
    private int noofsubs;
    private Spinner classSpinner;
    private int m_intSpinnerInitiCount = 0;
    private static final int NO_OF_EVENTS = 1;
    private LinearLayout linearLayout;
    private int counter=0;
    private FirebaseAuth firebaseAuth;
    private int subCounter;

    public ModifySubjectsFragment()
    {

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Modify Subject");

        subscrollview=(ScrollView)getView().findViewById(R.id.addSubScrollView);
        firebaseAuth=FirebaseAuth.getInstance();
        txtemail=(AutoCompleteTextView)getView().findViewById(R.id.txtemail);
        txtnoofsub=(AutoCompleteTextView)getView().findViewById(R.id.txtnoofsubjects);
        btnAdd=(Button)getView().findViewById(R.id.subadd_button);
        btnclear=(Button)getView().findViewById(R.id.subclear_button);
        classSpinner=(Spinner)getView().findViewById(R.id.spinnerClass);
        linearLayout=(LinearLayout)getView().findViewById(R.id.addSublayout);

        btnAdd.setOnClickListener(this);
        btnclear.setOnClickListener(this);
        classSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.fragment_modify_subjects, container, false);
    }

    @Override
    public void onClick(View v)
    {

        ArrayList<String> sublist=new ArrayList<>();

        String email=txtemail.getText().toString().trim();
        String mcaclass=classSpinner.getSelectedItem().toString();
        if(v==btnAdd)
        {
            boolean falg=true;
            if (classSpinner.getSelectedItemPosition() <= 0)
            {
                Toast.makeText(getActivity(), "Please Select a Class", Toast.LENGTH_SHORT).show();
                falg=false;
            }
            if(TextUtils.isEmpty(txtnoofsub.getText().toString()) || Integer.parseInt(txtnoofsub.getText().toString())<=0)
            {
                txtnoofsub.setError("Required");
                falg=false;
            }
            if(TextUtils.isEmpty(email))
            {
                txtemail.setError("Required");
                falg=false;
            }
            if(falg)
            {
                int noofsub=Integer.parseInt(txtnoofsub.getText().toString());
                String subname;
                AutoCompleteTextView txtsub;
                int sublistcounter=0;
                for(int i=(subCounter-noofsub)+1;i<=subCounter;i++)
                {
                    Log.e("TEst",sublistcounter+" && "+subCounter);
                    txtsub=(AutoCompleteTextView)getView().findViewById(i);
                    subname=txtsub.getText().toString().toUpperCase();
                    sublist.add(sublistcounter,subname);
                    Log.e("Elements",sublist.get(sublistcounter).toString()+"|| "+subname);
                    sublistcounter++;

                }
                new ModifySubDHelper(getContext()).addSub(email,mcaclass,sublist,noofsub);
                Toast.makeText(getContext(),"ADDED",Toast.LENGTH_LONG).show();

            }
            else
            {
                 Toast.makeText(getContext(),"Please fill all the details",Toast.LENGTH_LONG).show();
            }


        }
        else if(v==btnclear)
        {
            txtnoofsub.getText().clear();
            classSpinner.setSelection(0);
            if(linearLayout.getChildCount()>0)
            {
                linearLayout.removeAllViews();
            }


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if(counter>0)
        {
            ((LinearLayout) linearLayout).removeAllViews();
        }
        if (classSpinner.getSelectedItemPosition() > 0)
        {
            noofsubs = Integer.parseInt(txtnoofsub.getText().toString().trim());
            Log.e("Subjects", "" + noofsubs);
            for (int i = 1; i <= noofsubs; i++)
            {
                AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(getContext());
                autoCompleteTextView.setId(View.generateViewId());

                autoCompleteTextView.setHint("Subject" + i);
                Log.e("Id's", "" + autoCompleteTextView.getId());
                linearLayout.addView(autoCompleteTextView);
                subCounter=autoCompleteTextView.getId();

            }
            Log.e("Subcounter",""+subCounter);
            counter++;

        }
        else
        {
            Toast.makeText(getContext(),"Please select a subject",Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        Toast.makeText(getContext(),"PLEASE SELECT CLASS",Toast.LENGTH_LONG).show();
    }


}
