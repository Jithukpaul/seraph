package com.example.jithukpaul.seraph;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread r = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    auth();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        r.start();
        setContentView(R.layout.activity_main);
    }

    protected  void auth() throws  Exception{
        URL url = new URL("http://go.microsoft.com");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.connect();
      //  Log.i(TAG, ""+conn.getResponseCode());
       //  Toast.makeText(this,conn.getResponseCode(),Toast.LENGTH_LONG).show();

        //Redirected
        URL url2 = new URL(conn.getHeaderField("Location"));
        conn.disconnect();

        HttpURLConnection conn2 = (HttpURLConnection)url2.openConnection();
        conn2.connect();


        InputStreamReader isr = new InputStreamReader(conn2.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        String inputLine,magic="";
        while((inputLine = br.readLine()) != null){
            if (inputLine.matches("(.)*magic(.)*")) {
                magic= inputLine.substring(57,inputLine.length()-2);
                break;
            }
        }

        conn2.disconnect();

        url = new URL("https","seraph.karunya.edu",1003,"/");
        HttpsURLConnection auth  = (HttpsURLConnection)url.openConnection();
        auth.setRequestMethod("POST");
        auth.setRequestProperty("User-Agent", "Mozilla/5.0");
        auth.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "username=&magic="+magic+"&password=";
        auth.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(auth.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        Log.i(TAG,""+auth.getResponseCode());
        auth.disconnect();

    }
}
