package com.npsgmail.roopesh.androidsqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by roopesh on 18/10/17.
 */
//database class
public class DatabaseHelper extends SQLiteOpenHelper{


    public static final String DATABASE_NAME="contacts.db";
    public static final String TABLE_NAME="contacts";

    //public static final String COL1="id";
    public static final String COL1="name";
    public static final String COL2="surname";
    public static final String COL3="phone",COL4="email",COL5="address";


    public DatabaseHelper(Context context){//}, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table contacts(name varchar,surname varchar,phone varchar,email varchar,address varchar);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists contacts");
        onCreate(db);
    }

    public boolean insert_data(String name,String surname,String phone,String email,String address){


        SQLiteDatabase db=this.getWritableDatabase();   //creating an instance
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL1,name);        contentValues.put(COL2,surname);
        contentValues.put(COL3,phone);
        contentValues.put(COL4,email);
        contentValues.put(COL5,address);
        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
            return false;
        else
            return true;


    }

    public Cursor dup_name(String num){ //check for duplicate phone numbers
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME+" where phone='"+num+"';",null);
        return res;

    }

    public Cursor getAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME,null);
        return res;



    }
    public void remove_data(String phone){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_NAME+" where phone='"+phone+"'");
        //db.execSQL("alter table "+TABLE_NAME+" drop column impt;");

    }


    Cursor try_query(String query){
        SQLiteDatabase db=this.getWritableDatabase();   //creating an instance
        Cursor res=db.rawQuery(query,null);
        return res;

    }

    void try_query1(String query){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(query);

    }


}
