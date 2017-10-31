package com.example.jithukpaul.seraph;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            auth();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
    }

    protected  void auth() throws  Exception{
        URL url = new URL("http://go.microsoft.com");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        //Redirected
        URL url2 = new URL(conn.getHeaderField("Location"));
        HttpURLConnection conn2 = (HttpURLConnection)url2.openConnection();


        BufferedReader br = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
        String inputLine,magic="";
        while((inputLine = br.readLine()) != null){
            if (inputLine.matches("(.)*magic(.)*")) {
                magic= inputLine.substring(57,inputLine.length()-2);
                break;
            }
        }

        url = new URL("https","seraph.karunya.edu",1003,"/");
        HttpsURLConnection auth  = (HttpsURLConnection)url.openConnection();
        auth.setRequestMethod("POST");
        auth.setRequestProperty("User-Agent", "Mozilla/5.0");
        auth.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "username=UR14CS214&magic="+magic+"&password=cheetah214";
        auth.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(auth.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        Toast.makeText(this,auth.getResponseCode(),Toast.LENGTH_LONG).show();
    }
}
