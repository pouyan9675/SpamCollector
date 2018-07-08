package com.nahed.pouyan.spam_collector.helpers;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by pouyan on 3/24/18.
 */

public class NetworkHelper {

    public interface OnUploadListener {
        void onSuccess();
        void onFail();
    }

    public static void upload(final File file, final OnUploadListener listener) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://apps.pouyanna.ir/sms/";

                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("up_file", file.getName(), RequestBody.create(MediaType.parse("text/plain"), file))
            //                .addFormDataPart("other_field", "other_field_value")
                            .build();
                    Request request = new Request.Builder().url(url).post(formBody).build();
                    Response response = client.newCall(request).execute();

                    if(listener != null){
                        if(response.body().string().equals("1")){       // Success
                            listener.onSuccess();
                        }else if(response.body().string().equals("0")){     // Failed
                            listener.onFail();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

}
