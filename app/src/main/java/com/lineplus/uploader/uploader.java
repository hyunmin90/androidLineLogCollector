package com.lineplus.uploader;

import android.os.AsyncTask;


import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.MultipartBuilder;
import java.io.IOException;
import java.io.File;
import android.os.Environment;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public final class uploader {
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType TEXT
            = MediaType.parse("application/octet-stream");


    public boolean run(String uuid) throws Exception {
        //File filet = new File("/sdcard/line_log/");

        String storageDirectory = Environment.getExternalStorageDirectory().toString();
        System.out.println(storageDirectory);
        //List<File> ter = findDirectoriesWithSameName("line_log",filet);

        /*ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = filet.listFiles();
        for (File file : files) {
          inFiles.add(file);
        }*/
        //System.out.println(filet.getPath());
        //System.out.println(uuid);
        File f = new File(storageDirectory+"/line_log/");
        System.out.print(f.getPath());
        List<File> files = getListFiles(new File(storageDirectory+"/line_log/"));
        System.out.println(files.size());
        if(files.size()==0)
        {
            return false;
        }

        for(int i =0; i<files.size();i++)
        {
            files.get(i).getName();
        }

        System.out.println(files.get(0).getName());
        //while(file[z]!=null)
        //{
           // z++;
       // }
        /*System.out.println(files[0].getName());
        if(files.length==0)
            return;
        */
        MultipartBuilder mBuilder = new MultipartBuilder().type(MultipartBuilder.FORM).addFormDataPart("uuid", uuid);
       /* RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM).addFormDataPart("name","logs").addFormDataPart("file", file[0].getName(), RequestBody.create(TEXT, file[0]))
                    .type(MultipartBuilder.FORM).addFormDataPart("name","logs").addFormDataPart("file",file[1].getName(),RequestBody.create(TEXT,file[1]))
                    .type(MultipartBuilder.FORM).addFormDataPart("name","logs").addFormDataPart("file",file[2].getName(),RequestBody.create(TEXT,file[2])).build();*/
            for (int i = 0; i < files.size(); i++) {
                mBuilder.addFormDataPart(files.get(i).getName(),Long.toString(files.get(i).length())).addFormDataPart(uuid, files.get(i).getName(), RequestBody.create(TEXT, files.get(i)));
            }
                //System.out.println(i);
                //System.out.println(file[i].getName());
               // partBuilder.addFormDataPart("file", file[i].getName(),RequestBody.create(TEXT, file[i]));
          // }

        RequestBody requestBody=mBuilder.build();

        Request request = new Request.Builder()
                .url("http://10.70.14.90:3000/post/")
                .post(requestBody)
                .build();
        System.out.println(requestBody);
        System.out.println(request.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                System.out.println(response.body().string());
                String storageDirectory = Environment.getExternalStorageDirectory().toString();
                deleteFiles(new File(storageDirectory+"/line_log/"));
            }
        });
        return true;


    }


    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                    inFiles.add(file);

            }
        }
        return inFiles;
    }
    private List<File> deleteFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
               file.delete();

            }
        }
        return inFiles;
    }

}

