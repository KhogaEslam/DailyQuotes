package com.example.technical.dailyfortune;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyPreferences pref=new MyPreferences(MainActivity.this);

        if(!pref.isFirstTime()){
            Intent i=new Intent(getApplicationContext(),QuoteActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
            finish();
        }
        final Button button =(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserName(v);
            }
        });

    }
    public void saveUserName(View v){
        EditText username=(EditText)findViewById(R.id.editText);
        MyPreferences pref=new MyPreferences(MainActivity.this);
        pref.setUserName(username.getText().toString().trim());

        Intent i=new Intent(getApplicationContext(),QuoteActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        finish();
    }

}
