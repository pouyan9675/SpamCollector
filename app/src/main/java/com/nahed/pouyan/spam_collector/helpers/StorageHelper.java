package com.nahed.pouyan.spam_collector.helpers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.nahed.pouyan.spam_collector.Core;
import com.nahed.pouyan.spam_collector.models.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pouyan on 3/25/18.
 */

public class StorageHelper {

    public static void exportSMS(List<Message> messages){
        String exportName = getUsername();
        if(exportName == null || exportName.equals(""))
            exportName = android.os.Build.MODEL;
        try {
            File file = new File(Core.getInstance().getCacheDir(), exportName + ".txt");
            FileOutputStream fOut = new FileOutputStream(file);
            String result = "";
            for(int i=0; i<messages.size(); i++){
                result += messages.get(i).getBody();
                if(i != messages.size()-1)
                    result += '\n' + "###################################################################" + '\n';
            }
            fOut.write(result.getBytes());
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getUsername() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        String deviceName = Build.MODEL;
        return deviceName + "_" + currentDateandTime;
    }


}
