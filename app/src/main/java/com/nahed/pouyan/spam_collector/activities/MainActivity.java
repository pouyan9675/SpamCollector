package com.nahed.pouyan.spam_collector.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.nahed.pouyan.spam_collector.Core;
import com.nahed.pouyan.spam_collector.helpers.NetworkHelper;
import com.nahed.pouyan.spam_collector.helpers.PermissionHelper;
import com.nahed.pouyan.spam_collector.R;
import com.nahed.pouyan.spam_collector.adapters.AdapterMessage;
import com.nahed.pouyan.spam_collector.helpers.StorageHelper;
import com.nahed.pouyan.spam_collector.models.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean isUploading;
    private LottieAnimationView animationView;
    private List<Message> messages;
    private ViewGroup lytPermission;
    private RecyclerView lstMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lytPermission = findViewById(R.id.lytPermission);
        lstMessages = findViewById(R.id.lstMessages);
        TextView txtPermissionRequest = findViewById(R.id.txtPermissionRequest);
        final FABProgressCircle fabProgress = findViewById(R.id.fabProgressCircle);

        txtPermissionRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.requestPermission(MainActivity.this);
            }
        });

        final List<Message> selected = new ArrayList<>();
        fabProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isUploading){
                    isUploading = true;
                    for(Message m : messages)
                        if(m.selected)
                            selected.add(m);
                    if(selected.size() > 0){
                        fabProgress.show();
                        StorageHelper.exportSMS(selected);
                        String exportName = StorageHelper.getUsername();
                        File stored = new File(Core.getInstance().getCacheDir(), exportName + ".txt");
                        final Handler handler = new Handler();
                        NetworkHelper.upload(stored, new NetworkHelper.OnUploadListener() {
                            @Override
                            public void onSuccess() {
                                isUploading = false;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        fabProgress.beginFinalAnimation();
                                        Toast.makeText(MainActivity.this, getString(R.string.upload_successful), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onFail() {
                                isUploading = false;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        fabProgress.hide();
                                        Toast.makeText(MainActivity.this, getString(R.string.upload_failed), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    }

                }
            }
        });

        if(PermissionHelper.hasPermission()){
            loadSMSList();
        }else{
            animationView = findViewById(R.id.lottieAnim);
            setAnimation("sad.json");
        }


    }

    private void loadSMSList() {
        messages = getFilteredSMS();

        lytPermission.setVisibility(View.GONE);
        lstMessages.setVisibility(View.VISIBLE);
        AdapterMessage adapter = new AdapterMessage(messages);
        lstMessages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lstMessages.setAdapter(adapter);
    }

    private List<String> getContactNumbers() {
        List<String> phones = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if(!phoneNo.equals("")){
                            phones.add(phoneNo);
                        }
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
        return phones;
    }


    public List<Message> getSMS(){
        // Create Inbox box URI
        Uri inboxURI = Uri.parse("content://sms/inbox");

        // List required columns
        String[] reqCols = new String[] { "_id", "address", "body" };

        // Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = getContentResolver();

        // Fetch Inbox SMS Message from Built-in Content Provider
        Cursor c = cr.query(inboxURI, reqCols, null, null, null);

        List<Message> messages = new ArrayList<Message>();
        try{
            int i=0;
            while(c.moveToNext()){
                Message m = new Message(c.getString(c.getColumnIndex("_id")),
                        c.getString(c.getColumnIndex("body")),
                        c.getString(c.getColumnIndex("address")));
                messages.add(m);
            }
        } finally {
            c.close();
        }
        return messages;
    }

    private List<Message> getFilteredSMS(){
        List<String> contacts = getContactNumbers();

        Uri inboxURI = Uri.parse("content://sms/inbox");
        String[] reqCols = new String[] { "_id", "address", "body" };
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(inboxURI, reqCols, null, null, null);
        List<Message> messages = new ArrayList<Message>();
        try{
            while(c.moveToNext()){
                if(!contacts.contains(c.getString(c.getColumnIndex("address")))){
                    Message m = new Message(c.getString(c.getColumnIndex("_id")),
                            c.getString(c.getColumnIndex("body")),
                            c.getString(c.getColumnIndex("address")));
                    messages.add(m);
                }
            }
        } finally {
            c.close();
        }
        return messages;
    }

    public void setAnimation(String fileName){
        LottieComposition lottieComposition = LottieComposition.Factory.fromFileSync(Core.getInstance().getContext(), fileName);
        animationView.setComposition(lottieComposition);
        animationView.setScale(2.0f);
        animationView.playAnimation();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if(requestCode == PermissionHelper.REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadSMSList();
            }
        }
    }


}
