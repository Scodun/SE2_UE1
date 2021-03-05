package com.example.se2_ue1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements Runnable{

    Socket client;
    EditText studentNumber;
    TextView serverResponseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentNumber=(EditText)findViewById(R.id.studentNumberInput);
        serverResponseView =(TextView)findViewById(R.id.serverResponseView);
    }

    public void sendToServer(View view) {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            client=new Socket("se2-isys.aau.at",53212);
            DataOutputStream outToServer = new DataOutputStream(client.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outToServer.writeBytes(studentNumber.getText().toString()+"\n");
            String text =inFromServer.readLine();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    serverResponseView.setText(text);
                }
            });
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}