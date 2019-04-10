package com.studio.sds.nfcapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewInfoActivity extends AppCompatActivity {
    private TableLayout tbl;
    private AutoCompleteTextView txt1,txt2;
    private TableRow tbr;
    private List studentinfo;
    private Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info);

        tbl=(TableLayout)findViewById(R.id.tableStudentInfo);
        txt1=new AutoCompleteTextView(this);
        txt2=new AutoCompleteTextView(this);
        Bundle bundle=getIntent().getExtras();
        HashMap<Integer,String> al= (HashMap<Integer, String>) bundle.getSerializable("Student_Info");
        Log.e("Size",""+al.size());
        for(int i=0;i<62;i++)
        {
            tbr =new TableRow(this);
            if(al.get(i)!=null)
            {
                txt1.setText(al.get(i));
                tbr.addView(txt1);

                txt2.setText(String.valueOf(i));
                tbr.addView(txt2);


            }
            tbl.addView(tbr);

        }
    }
}
