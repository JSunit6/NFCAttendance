package com.studio.sds.nfcapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunit on 17-09-2016.
 */
public class ModifySubDHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="attendanceDb";
    private static final int DATABASE_VERSION=1;
    private SQLiteDatabase db;
    private List<String> subarraylist;
    public  ModifySubDHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        Log.e("CREATION","DATABASE");
        db=getWritableDatabase();
        onCreate(db);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query="create table if not exists Subjectdtls (" +
                "email Text," +
                "mca_class Text," +
                "subject Text" +
                ");";
        db.execSQL(query);
        Log.e("Creation","Table");
    }

    public void addSub(String email,String mcaclass, ArrayList<String> subject,int nnofsubs)
    {
        ContentValues contentValues=new ContentValues();
        db = getWritableDatabase();
        for(int i=0;i<nnofsubs;i++)
        {
            contentValues.put("email", email);
            contentValues.put("mca_class", mcaclass);
            contentValues.put("subject",subject.get(i).toString().trim());
            db.insert("Subjectdtls", null, contentValues);
            Log.e("Record", "Inserted");
        }
        return;
    }

    public List<String> getSubjects(String email, String mcaclass)
    {
        String sub="";
        subarraylist=new ArrayList<>();
        String query="SELECT * FROM Subjectdtls " +
        "WHERE email='"+email+"' AND mca_class='"+mcaclass+"';";
        Log.e("query",query);
        Cursor cursor=db.rawQuery(query,null);
        subarraylist.add(0,"Select a Subject");
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
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
