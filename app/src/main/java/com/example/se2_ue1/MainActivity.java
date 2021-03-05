package com.example.se2_ue1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

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

    // My number: 11903735 % 7 = 4 -> Sort and exclude primes
    public void calcNumber(View view){
        if(!studentNumber.getText().toString().isEmpty()){
            //regex for prime number check, sort and display
            char[] numbers = studentNumber.getText().toString().replaceAll("2|3|5|7","").toCharArray();
            Arrays.sort(numbers);
            serverResponseView.setText(String.valueOf(numbers));
        }
        else {
            Toast.makeText(getApplicationContext(),"No Number entered!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void run() {
        try {
            //open connection and send data to server
            client=new Socket("se2-isys.aau.at",53212);
            DataOutputStream outToServer = new DataOutputStream(client.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outToServer.writeBytes(studentNumber.getText().toString()+"\n");
            String text =inFromServer.readLine();

            //display retrieved data
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    serverResponseView.setText(text);
                }
            });
            client.close();

        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}