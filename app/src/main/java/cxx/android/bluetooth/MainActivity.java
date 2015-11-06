package cxx.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import  cxx.bluetooth.client.pbap.BluetoothPbapClient;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import javax.obex.ClientSession;
import javax.obex.ObexTransport;
import android.util.Log;
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "cxx.bluetooth.client";
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mSocket;
    private  ObexTransport mTransport;
    private static final String PBAP_UUID =
            "0000112f-0000-1000-8000-00805f9b34fb";
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
                BluetoothPbapClient client = new BluetoothPbapClient(device,new Handler());
                client.connect();
               boolean flg = client.pullVcardListing(BluetoothPbapClient.PB_PATH);
                Log.d(TAG, "Handler: msg: " + flg);
                //    mSessionHandler.obtainMessage(RFCOMM_CONNECTED, transport).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
