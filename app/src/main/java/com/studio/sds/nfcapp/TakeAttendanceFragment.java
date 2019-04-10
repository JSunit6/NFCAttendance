package com.studio.sds.nfcapp;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class TakeAttendanceFragment extends Fragment implements View.OnClickListener,Spinner.OnItemSelectedListener {
    private AutoCompleteTextView txtdate;
    private List<String> arrayList;
    private Spinner classspinner,subspinner;
    private Button btnStart;
    private AttendanceDBHelper attendanceDBHelper;
    private SQLiteDatabase db;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Context context=this.getActivity();
    private int m_intSpinnerInitiCount = 0;
    private static final int NO_OF_EVENTS = 1;
    private NfcAdapter mNfcAdapter;

    public TakeAttendanceFragment()
    {

    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        txtdate=(AutoCompleteTextView)getView().findViewById(R.id.datepick);

        classspinner=(Spinner)getView().findViewById(R.id.spinnerClass);
        subspinner=(Spinner)getView().findViewById(R.id.spinnerSubject);

        btnStart=(Button)getView().findViewById(R.id.start_button);

        //NFC CODE
        mNfcAdapter = NfcAdapter.getDefaultAdapter(getContext());
        if (mNfcAdapter == null)
        {
            Toast.makeText(getContext(), "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            getActivity().finish();
            return;

        }
        else if (!mNfcAdapter.isEnabled())
        {
            Toast.makeText(getContext(), "NFC is disabled, please enable it", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getContext(), "NFC is enabled, you can now fill all the details and press start", Toast.LENGTH_LONG).show();
        }
        //NFC CODE

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        String currentDateandTime = sdf.format(new Date());
        txtdate.setText(currentDateandTime);
        btnStart.setOnClickListener(this);
        classspinner.setOnItemSelectedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.getActivity().setTitle("Take Attendance");
        return inflater.inflate(R.layout.fragment_take_attendance, container, false);
    }


    @Override
    public void onClick(View view)
    {

        if(view==btnStart)
        {

            if(classspinner.getSelectedItemPosition()<=0)
            {
                Toast.makeText(getActivity(),"Please Select a Class",Toast.LENGTH_LONG).show();
            }
            else if(subspinner.getSelectedItemPosition()<=0)
            {
                Toast.makeText(getActivity(),"Please Select a Subect",Toast.LENGTH_LONG).show();
            }

            else
            {

                Bundle bundle=new Bundle();
                bundle.putStringArrayList("Info",getInfo());
                Intent intent=new Intent(getActivity(),TagReadActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);


            }


        }
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {


        if (m_intSpinnerInitiCount < NO_OF_EVENTS)
        {
            m_intSpinnerInitiCount++;
        }
        else
        {
                Log.e("Here", "Here");

                String email = firebaseAuth.getCurrentUser().getEmail().toString().trim();

                arrayList = new ModifySubDHelper(this.getContext()).getSubjects(email, classspinner.getSelectedItem().toString());
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, arrayList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                subspinner.setAdapter(dataAdapter);
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        Toast.makeText(getContext(),"Please Select Class",Toast.LENGTH_LONG).show();
    }
    public String getUserEmail()
    {   FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        Log.e("Class",firebaseAuth.getCurrentUser().getEmail().toString());
        return firebaseAuth.getCurrentUser().getEmail().toString();
    }
    public String getMcaDate()
    {
        Log.e("Class",txtdate.getText().toString().trim());
        return txtdate.getText().toString().trim();
    }
    public String getSubject()
    {
        Log.e("Class",subspinner.getSelectedItem().toString().trim());
        return subspinner.getSelectedItem().toString().trim();
    }
    public String getMcaClass()
    {
        Log.e("Class",classspinner.getSelectedItem().toString().trim());
        return classspinner.getSelectedItem().toString().trim();
    }

    public ArrayList<String> getInfo()
    {
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add(0,this.getUserEmail());
        arrayList.add(1,this.getMcaClass());
        arrayList.add(2,this.getSubject());
        arrayList.add(3,this.getMcaDate());
        return arrayList;
    }



}
