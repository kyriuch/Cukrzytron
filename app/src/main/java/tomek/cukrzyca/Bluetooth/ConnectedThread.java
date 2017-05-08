package tomek.cukrzyca.Bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;

import java.io.IOException;
import java.io.InputStream;

import tomek.cukrzyca.Fragments.PopFragment;
import tomek.cukrzyca.R;


class ConnectedThread extends Thread {
    private BluetoothSocket mmServerSocket;
    private final InputStream mmInStream;
    private final Handler handler;
    public static int input;

    ConnectedThread(BluetoothSocket mmServerSocket, Handler handler) {
        this.mmServerSocket = mmServerSocket;
        this.handler = handler;
        InputStream tmpIn = null;

        try {
            tmpIn = mmServerSocket.getInputStream();
        } catch (IOException ignored) {

        }

        mmInStream = tmpIn;
    }

    public void run() {
        byte[] buffer = new byte[1024];

        while(true) {
            try {
                int bytes = mmInStream.read(buffer);

                String sAmount = "";

                for(int i = 0; i < bytes; i++) {
                    sAmount += (char) buffer[i];
                }

                int amount = Integer.parseInt(sAmount);

                handler.sendEmptyMessage(amount);
            } catch (IOException ignored) {
                break;
            }
        }
    }

    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException ignored) {

        }
    }

}
