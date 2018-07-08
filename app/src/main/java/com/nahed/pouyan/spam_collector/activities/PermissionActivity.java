package com.nahed.pouyan.spam_collector.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nahed.pouyan.spam_collector.R;
import com.nahed.pouyan.spam_collector.helpers.PermissionHelper;

public class PermissionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        TextView txtPermissionDeny = findViewById(R.id.txtPermissionDeny);
        TextView txtPermissionRequest = findViewById(R.id.txtPermissionRequest);

        txtPermissionDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PermissionActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtPermissionRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.requestPermission(PermissionActivity.this);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if(requestCode == PermissionHelper.REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(PermissionActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "دسترسی گرفته نشد!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
