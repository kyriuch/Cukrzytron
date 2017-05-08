package tomek.cukrzyca.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.util.UUID;


public class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private final Handler handler;
    private ConnectedThread thread = null;

    public AcceptThread(Handler handler) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            UUID MY_UUID = UUID.fromString("9023bdb4-ba6e-11e6-a4a6-cec0c932ce01");
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("DIABETIC_APP", MY_UUID);
        } catch (IOException ignored) {
        }
        mmServerSocket = tmp;

        this.handler = handler;
    }

    public void run() {
        BluetoothSocket socket;

        while (true) {

            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            if (socket != null) {
                thread = new ConnectedThread(socket, handler);
                thread.start();
                try {
                    mmServerSocket.close();
                } catch (IOException ignored) {

                }
                break;
            }
        }
    }

    /**
     * Will cancel the listening socket, and cause the thread to finish
     */
    public void cancel() {
        if(thread != null) {
            thread.cancel();
        }

        try {
            mmServerSocket.close();
        } catch (IOException ignored) {
        }
    }
}

