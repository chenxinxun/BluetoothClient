package cxx.android.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import javax.obex.ObexTransport;

import cxx.android.vcard.VCardEntry;
import cxx.android.vcard.VCardEntryCommitter;
import cxx.bluetooth.client.pbap.BluetoothPbapClient;
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "cxx.bluetooth.client";
    private Button mSimContactbtn;
    private Button mContactbtn;
    private ListView mBlueDevices;
    private TextView mState;
    private DeviceMaintain maintain = new DeviceMaintain();
    private BluetoothPbapClient client;
    private String devcieName = "";


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
                    int size = list.size();
                    int i = 0;
                    int pacth = 0;
                    if (size / 20  > 0){
                        pacth = (size / 20) * 20;
                    }
                    for (i =0; i < pacth; i++){

                        committer.onEntryCreated(list.get(i));
                        Log.d(TAG, "a" + list.get(i).getDisplayName());
                    }

                    for (; i < size; i++){
                        committer.onEntryCreateNoPatch(list.get(i));
                        Log.d(TAG, "a" + list.get(i).getDisplayName());
                    }
                    mState.setText(R.string.success);
                    break;
                default:
                  //  client.disconnect();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mState = (TextView)findViewById(R.id.state);
        mState.setText(R.string.init);
        mContactbtn = (Button)findViewById(R.id.contact);
        mContactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });
        mSimContactbtn = (Button)findViewById(R.id.simcontact);


        mSimContactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        mBlueDevices = (ListView)findViewById(R.id.bluetoothdevice);
        mBlueDevices.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                maintain.getDevicesName()
        ));
        if (maintain.getDevicesName().size() == 0){
            Toast.makeText(MainActivity.this, R.string.bluecp, Toast.LENGTH_LONG).show();
        }
        mBlueDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                devcieName = (String) mBlueDevices.getItemAtPosition(position);
            }
        });
    }
    void connect(){
        try {
            BluetoothDevice device = maintain.getDeviceFromName(devcieName);
            client = new BluetoothPbapClient(device, handler);
            client.connect();
            boolean flg = client.pullPhoneBook(BluetoothPbapClient.PB_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
