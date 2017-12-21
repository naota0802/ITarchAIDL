package com.example.hinoto.ITarchAIDL;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity{

    IMyAidlInterface aidl;
    TextView res;
    Button btn;
    private  int result = 1;
    private  int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        final Intent i = new Intent(this, AIDLService.class);
        i.setAction(IMyAidlInterface.class.getName());
        bindService(i, serviceConnection, BIND_AUTO_CREATE);

        btn = (Button) findViewById(R.id.start);
        res = (TextView) findViewById(R.id.calcResult);
        res.setTextSize(48);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                switch(v.getId()){
                    case R.id.start:

                        Timer timer = new Timer(true);
                        final Handler handler = new Handler();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        handler.post( new Runnable(){
                                            public void run(){
                                                try {
                                                    if(flag == 0) {
                                                        result = aidl.add(result, result);
                                                        if(result == 2048) flag = 1;
                                                    }else {
                                                        result = aidl.half(result, 2);
                                                        if(result == 2) flag = 0;
                                                    }
                                                } catch (RemoteException e) {
                                                    e.printStackTrace();
                                                }
                                                res.setText(String.valueOf(result));
                                            }
                                        });
                                    }
                                },
                                0,
                                1200

                        );

                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (aidl != null) {
            unbindService(serviceConnection);
        }
    }




    private ServiceConnection serviceConnection = new ServiceConnection(){
        // サービス接続時呼び出し
        public void onServiceConnected(ComponentName name, IBinder ibinder){
            aidl = IMyAidlInterface.Stub.asInterface(ibinder);
        }

        // サービス切断時呼び出し
        public void onServiceDisconnected(ComponentName name){
            aidl = null;
        }

    };
}
