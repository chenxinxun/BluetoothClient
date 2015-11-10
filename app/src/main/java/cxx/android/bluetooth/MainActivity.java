package cxx.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Set;

import javax.obex.ObexTransport;

import cxx.android.vcard.VCardEntry;
import cxx.android.vcard.VCardEntryCommitter;
import cxx.bluetooth.client.pbap.BluetoothPbapClient;
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "cxx.bluetooth.client";
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mSocket;
    private  ObexTransport mTransport;
    private static final String PBAP_UUID =
            "0000112f-0000-1000-8000-00805f9b34fb";

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothPbapClient.EVENT_PULL_PHONE_BOOK_DONE:
                    ArrayList<VCardEntry> list = (ArrayList<VCardEntry>)msg.obj;
                    VCardEntryCommitter committer = new VCardEntryCommitter(getContentResolver());
                    for (int i =0; i < list.size(); i++){
                        committer.onEntryCreated(list.get(i));
                        Log.d(TAG, "a" + list.get(i).getDisplayName());
                    }

                    break;
                default:

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        connect();
    }
    void connect(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices =  mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device: devices) {
            try {
               // mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(PBAP_UUID));
                BluetoothPbapClient client = new BluetoothPbapClient(device,handler);
                client.connect();
                boolean flg =   client.pullPhoneBook(BluetoothPbapClient.PB_PATH);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
