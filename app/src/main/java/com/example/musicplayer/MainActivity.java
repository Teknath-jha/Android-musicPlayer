package com.example.musicplayer;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView myListViewForSongs;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myListViewForSongs = (ListView) findViewById(R.id.mySongListView);
                    runtimePermission();
    }
    public void runtimePermission (){
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }) .check();
    }

    public ArrayList<File> findsong (File file){
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();

        for(File singleFile : files)
        {
                if(singleFile.isDirectory() &&  !singleFile.isHidden())
                {
                    arrayList.addAll(findsong(singleFile));
                }
                else
                {
                    if( singleFile.getName().endsWith(".wav") || singleFile.getName().endsWith(".m4a") || singleFile.getName().endsWith(".ogg") || singleFile.getName().endsWith(".mp3"))
                        arrayList.add(singleFile);
                }
        }
        return  arrayList;
    }

    void display()
    {
        final ArrayList<File>  mysongs = findsong(Environment.getExternalStorageDirectory());
        items =  new String[mysongs.size()];

        for(int i=0;i<mysongs.size();i++)
        {
            items[i] = mysongs.get(i).getName().replace(".wav","").replace(".m4a","").replace(".ogg","").replace(".mp3" ,"");
        }

        ArrayAdapter<String>  myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);

        myListViewForSongs.setAdapter(myAdapter);
    }
}