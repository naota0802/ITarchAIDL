package com.example.hinoto.ITarchAIDL;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * Created by hinoto on 2017/12/21.
 */

public class AIDLService extends Service{
    @Override
    public IBinder onBind(Intent intent) {
        if(IMyAidlInterface.class.getName().equals(intent.getAction())){
            return isvc;
        }
        return null;
    }

    private IMyAidlInterface.Stub isvc = new IMyAidlInterface.Stub()
    {
        @Override
        public int add(int lhs, int rhs) throws RemoteException {
            return lhs + rhs;
        }

        public int half(int lhs, int rhs) throws RemoteException {
            return lhs / rhs;
        }
    };
}
