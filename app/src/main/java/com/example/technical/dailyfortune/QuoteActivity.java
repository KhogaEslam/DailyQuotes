package com.example.technical.dailyfortune;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;


public class QuoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fortune);
        MyPreferences pref=new MyPreferences(QuoteActivity.this);
        if(pref.isFirstTime()){
            Toast.makeText(QuoteActivity.this,"hi"+pref.getUserName(),Toast.LENGTH_LONG).show();

            pref.setOld(true);

            new GetDailyQuote().execute((Void) null);

        }
        else
        {
            Toast.makeText(QuoteActivity.this,"welcome back"+pref.getUserName(),Toast.LENGTH_LONG).show();
            new GetDailyQuote().execute((Void) null);
        }


    }
    public class GetDailyQuote extends AsyncTask<Void, Void,Boolean>{
        TextView fortuneText;
        String jsonResponse;

        @Override
        protected void onPreExecute() {
            fortuneText=(TextView)findViewById(R.id.fortune);
            String qt = "Loading...";
            fortuneText.setText(qt);
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            ConnectionDetector cd=new ConnectionDetector(getApplicationContext());
            if(cd.isConnectingToInternet()){
                try {
                    DefaultHttpClient httpClient=new DefaultHttpClient();
                    HttpGet httpGet=new HttpGet("http://api.theysaidso.com/qod.json");

                    HttpResponse httpResponse=httpClient.execute(httpGet);
                    HttpEntity httpEntity=httpResponse.getEntity();
                    InputStream is=httpEntity.getContent();
                    String encode="iso-8859-1";
                    InputStreamReader isr = new InputStreamReader(is,encode);
                    BufferedReader reader=new BufferedReader(isr,8);
                    StringBuilder sb=new StringBuilder();
                    String line=null;
                    while ((line=reader.readLine())!=null){
                        sb.append(line).append("\n");
                    }
                    is.close();
                    jsonResponse=sb.toString();
                    writeToFile(jsonResponse);


                }
                catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                    return false;

                }
                catch (ClientProtocolException e){
                    e.printStackTrace();
                    return false;
                }
                catch (IOException e){
                    e.printStackTrace();
                    return false;
                }

                return true;
            }
            else
                return false;
        }



        @Override
        protected void onPostExecute(final Boolean success) {
            JSONObject obj;
            try{
                if (success){
                    obj=new JSONObject(jsonResponse);
                }
                else {
                    obj=new JSONObject(readFromFile());
                }
                String fortune=obj.getString("quote");
                fortuneText.setText(fortune);
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
        String FILE_NAME = "fortune.json";
        private void writeToFile(String data){

            try {
                OutputStreamWriter outputStreamWriter=new OutputStreamWriter(openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
                outputStreamWriter.write(data);
                outputStreamWriter.close();

            } catch (IOException e){
                Log.e("Message:", "File write failed:" + e.toString());
            }
        }
        private String readFromFile(){
            String ret=null;
            try {
                InputStream inputStream=openFileInput("fortune.json");
                if (inputStream!=null){
                    InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                    BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                    String receiveString="";
                    StringBuilder stringBuilder=new StringBuilder();
                    Log.v("Message:","reading...");
                    while ((receiveString=bufferedReader.readLine())!=null){
                        stringBuilder.append(receiveString);
                    }
                    inputStream.close();
                    ret=stringBuilder.toString();
                }
            }
            catch (FileNotFoundException e){
                Log.e("Message:","File not found:"+e.toString());

            }
            catch (IOException e){
                Log.e("Message:","Can not read file:"+e.toString());
            }
            return ret;
        }
    }
}
