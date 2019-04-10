package com.studio.sds.nfcapp;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



public class ViewAttendance extends Fragment implements View.OnClickListener, Spinner.OnItemSelectedListener{
    private AutoCompleteTextView txtViewDate;
    private DatePicker datePicker;
    private Button dateBtn,btnView;
    private FrameLayout frameLayout;
    private FloatingActionButton dateAdd;
    private Spinner spinnerSub,spinnerClass;
    private String email;
    private List<String> arrayList;
    private HashMap<Integer,String> studinfolist;
    private FirebaseAuth firebaseAuth;
    private TextView txtClass,txtSub;

    public ViewAttendance() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtViewDate=(AutoCompleteTextView)getView().findViewById(R.id.txtviewDate);
        datePicker=(DatePicker)getView().findViewById(R.id.datePickerView);
        dateBtn=(Button)getView().findViewById(R.id.btnselectdate);
        btnView=(Button)getView().findViewById(R.id.btnViewInfo);
        dateAdd=(FloatingActionButton)getView().findViewById(R.id.fabaddDate);
        spinnerSub=(Spinner)getView().findViewById(R.id.spinnerSubView);
        spinnerClass=(Spinner)getView().findViewById(R.id.spinnerClassView);
        frameLayout=(FrameLayout)getView().findViewById(R.id.datePickerFrame);
        txtClass=(TextView)getView().findViewById(R.id.textViewClass);
        txtSub=(TextView)getView().findViewById(R.id.textViewSub);

        firebaseAuth=FirebaseAuth.getInstance();
        email=firebaseAuth.getCurrentUser().getEmail().toString();
        dateAdd.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(103,58,183)));
        dateAdd.setRippleColor(Color.rgb(250,250,250));
        btnView.setVisibility(Button.INVISIBLE);
        txtViewDate.setOnClickListener(this);
        dateBtn.setOnClickListener(this);
        dateAdd.setOnClickListener(this);
        btnView.setOnClickListener(this);
        spinnerClass.setOnItemSelectedListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.getActivity().setTitle("View Attendance");
        return inflater.inflate(R.layout.fragment_view_attendance, container, false);

    }


    public void setDate(int day,int month,int year)
    {
        String date=day+"/"+month+"/"+year;
        txtViewDate.setText(date);
    }
    @Override
    public void onClick(View view)
    {

        int date=0,month=0,year=0;
        if(view==txtViewDate)
        {
            setDate(date,month,year);
        }
        else if(view==dateBtn)
        {
            frameLayout.setVisibility(FrameLayout.VISIBLE);
            txtClass.setVisibility(TextView.INVISIBLE);
            txtSub.setVisibility(TextView.INVISIBLE);
            spinnerClass.setVisibility(Spinner.INVISIBLE);
            spinnerSub.setVisibility(Spinner.INVISIBLE);

            dateBtn.setVisibility(Button.INVISIBLE);

        }
        else if(view==dateAdd)
        {


            date=datePicker.getDayOfMonth();
            month=datePicker.getMonth()+1;
            year=datePicker.getYear();

            String datestr=date+"/"+month+"/"+year;
            txtViewDate.setText(datestr);
            dateBtn.setVisibility(Button.VISIBLE);
            txtClass.setVisibility(TextView.VISIBLE);
            txtSub.setVisibility(TextView.VISIBLE);
            spinnerClass.setVisibility(Spinner.VISIBLE);
            spinnerSub.setVisibility(Spinner.VISIBLE);
            frameLayout.setVisibility(FrameLayout.GONE);
            Log.e("email",email);

        }

        else if(view==btnView)
        {
            if(spinnerClass.getSelectedItemPosition()<=0)
            {
                Toast.makeText(getContext(),"Please select a class",Toast.LENGTH_LONG).show();
            }
            else if(spinnerSub.getSelectedItemPosition()<=0)
            {
                Toast.makeText(getContext(),"Please select a Subject",Toast.LENGTH_LONG).show();
            }
            else
            {
                studinfolist=new HashMap<Integer,String>(62);
                studinfolist=new AttendanceDBHelper(getContext()).getStudentInfo(email,txtViewDate.getText().toString().trim(),spinnerSub.getSelectedItem().toString().trim(),spinnerClass.getSelectedItem().toString().trim());
                Bundle bundle=new Bundle();
                bundle.putSerializable("Student_Info",studinfolist);
                Intent intent=new Intent(getActivity(),ViewInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);


            }

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {

        String mca_class = spinnerClass.getSelectedItem().toString().trim();
        arrayList = new ModifySubDHelper(this.getContext()).getSubjects(email, mca_class);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, arrayList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerSub.setAdapter(dataAdapter);
        btnView.setVisibility(Button.VISIBLE);

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        Toast.makeText(this.getContext(),"PLease select a subject",Toast.LENGTH_LONG).show();
    }


}