package com.studio.sds.nfcapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sunit on 14-09-2016.
 */
public class AttendanceDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="attendanceDb";
    private static final int DATABASE_VERSION=1;
    private SQLiteDatabase db;
    private List<String> subarraylist;
    private HashMap<Integer,String> studarraylist;
    public AttendanceDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        Log.e("CREATION","DATABASE");
        db=getWritableDatabase();
        onCreate(db);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("Create table if not exists Attendancedtls (" +
                "email Text," +
                "class Text," +
                "subject Text," +
                "date Text," +
                "stud_name Text," +
                "stud_roll Integer" +
                ");");
        Log.e("CREATION","TABLE");

    }

    public void addAttendanceInfo(String email,String mcaclass,String subject,String date,String studname,int studrol)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put("email",email);
        contentValues.put("class",mcaclass);
        contentValues.put("subject",subject);
        contentValues.put("date",date);
        contentValues.put("stud_name",studname);
        contentValues.put("stud_roll",studrol);
        SQLiteDatabase db=getWritableDatabase();
        db.insert("Attendancedtls",null,contentValues);
        Log.e("Record","Inserted");
    }

    public List<String> getSubList(String email,String date)
    {
        String sub;
        subarraylist=new ArrayList<>();
        String query="SELECT * FROM Attendancedtls " +
        "WHERE email='"+email+"' AND date='"+date+"';";
        Log.e("query",query);
        Cursor cursor=db.rawQuery(query,null);




       if(cursor!=null)
       {
           if (cursor.moveToFirst())
        {
            while(!cursor.isAfterLast())
            {
                sub=cursor.getString(cursor.getColumnIndex("subject"));
                if(!(subarraylist.contains(sub.toString())))
                {
                    subarraylist.add(sub);
                }


                cursor.moveToNext();

            }
        }
       }
        cursor.close();
        return subarraylist;
    }

    public List<String> getClassList(String email,String date,String sub)
    {

        subarraylist=new ArrayList<>();
        String query="SELECT * FROM Attendancedtls " +
                "WHERE email='"+email+"' AND date='"+date+"' AND subject='"+sub+"';";
        Log.e("query",query);
        Cursor cursor=db.rawQuery(query,null);




        if(cursor!=null)
        {
            if (cursor.moveToFirst())
            {
                while(!cursor.isAfterLast())
                {
                    sub=cursor.getString(cursor.getColumnIndex("class"));
                    if(subarraylist.isEmpty() || !(subarraylist.equals(sub.toString())))
                    {
                        subarraylist.add(sub);
                    }
                    cursor.moveToNext();

                }
            }
        }
        cursor.close();
        return subarraylist;
    }

    public HashMap<Integer,String> getStudentInfo(String email, String date, String sub, String mcaclass)
    {
        studarraylist=new HashMap<Integer, String>(62);
        String name="";
        int roll=0;
        String query="SELECT * FROM Attendancedtls " +
                "WHERE email='"+email+"' AND date='"+date+"' AND subject='"+sub+"' AND class='"+mcaclass+"';";
        Cursor cursor=db.rawQuery(query,null);
        if(cursor!=null)
        {
            if (cursor.moveToFirst())
            {
                while (!cursor.isAfterLast())
                {
                    name=cursor.getString(cursor.getColumnIndex("stud_name"));
                    roll=cursor.getInt(cursor.getColumnIndex("stud_roll"));
                    Log.e("Record",name);
                    Log.e("Record",String.valueOf(roll));
                    studarraylist.put(roll,name);
                    cursor.moveToNext();
                }
            }
        }
        return studarraylist;
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
