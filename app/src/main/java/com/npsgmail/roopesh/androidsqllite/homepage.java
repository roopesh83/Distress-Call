package com.npsgmail.roopesh.androidsqllite;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class homepage extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener{
    int double_taps=0;
    TextView count;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    StringBuffer buffer;

    private GestureDetectorCompat mDetector;

    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {

        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {



        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Toast.makeText(getApplicationContext(),"not sending sms",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {


        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {

        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        double_taps++;
        update_count(); //update text view count
        if(double_taps>5){
            show_message("Your Attension please!","More than 5 double taps.Do you want to broadcast sms?");

            double_taps=0;
        }

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        // Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        // Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }



    EditText query,result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        double_taps=0;
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);
        count=(TextView)findViewById(R.id.count);
        buffer=new StringBuffer("testing app");
        //query=(EditText)findViewById(R.id.query_test);
        //result=(EditText)findViewById(R.id.result);


    }

    @Override
    protected void onResume() {
        super.onResume();
        double_taps=0;

    }


    void show_message(String title,String msg)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setCancelable(false);
        alert.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int id){
                //Toast.makeText(getApplicationContext(),"sendin sms",Toast.LENGTH_SHORT).show();
                //SendSms();
                try_query();
            }
        });

        alert.setNegativeButton("No",new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog,int id){
            Toast.makeText(getApplicationContext(),"not sending sms",Toast.LENGTH_SHORT).show();
            dialog.cancel();
        }
    });

        AlertDialog alertDialog = alert.create();  //create the alert dialog

        // show it
        alertDialog.show();

    }


    void update_count(){
        count.setText("Double taps="+double_taps);
    }
    public void try_query(){
       DatabaseHelper dh=new DatabaseHelper(this);
        Cursor res=dh.try_query("select phone from contacts;"); //retrieving phone numbers
        StringBuffer buf=new StringBuffer("Sent to \n");
        String s;
        while(res.moveToNext()){    //each row is examined

            for(int i=0;i<1;i++)
            {
                s=res.getString(i);   //getting the phone number
                SendSms(s);
                buf.append(s);
            }

            buf.append("\n");

        }
    count.setText(buf.toString());

    }


    protected void SendSms(String number) {

/*
        SmsManager smsManager = SmsManager.getDefault();        //getting the deafult smsmanager
        smsManager.sendTextMessage(num.getText().toString(), null, text.getText().toString(), null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
*/
        //if permission is not granted,then request the permssion,and in callback check the request code ,and then send message
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                temp=number;
            }


        }   //if permsission is already granted then send sms from the default SmsManager
        else{
            SmsManager smsManager = SmsManager.getDefault();        //getting the deafult smsmanager
            smsManager.sendTextMessage(number, null, buffer.toString(), null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        }

    }

    //callback function for on requesting permision
String temp;
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {     //in other words,requestcode==0
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  //if permission is granted
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(temp, null, buffer.toString(), null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }   //end of onrequest callback


    void simple_message(String title,String msg){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();


    }


public void show_hint(View view){
 simple_message("Hint","1)Double tap more than 5 times to send sms to all saved contacts from the database.\n");
}

public void goto_next(View view){
    Intent k=new Intent(this,MainActivity.class);
    startActivity(k);
}

public void exit1(View view){
    finish();
    System.exit(0);
}


}
