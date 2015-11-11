package cxx.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by chenxinxun on 2015/11/11.
 */
public class DeviceMaintain {
    private BluetoothAdapter mBluetoothAdapter;
    private List<String> devicesName;
    private Set<BluetoothDevice> bondedDevices;

    /**
     * defalut construct
     * init BlueToothDevice's adpter
     */
    public DeviceMaintain(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * BlueToothDevice's name collection,every device which is
     * bonded the same another bluetoothdevice
     *
     * @return bonded device's name
     */
    public List<String> getDevicesName(){
        devicesName = new ArrayList<String>();
        bondedDevices = new HashSet<BluetoothDevice>();
        Set<BluetoothDevice> devices =  mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device: devices
             ) {
            if (device.getBondState() == BluetoothDevice.BOND_BONDED){
                devicesName.add(device.getName());
                bondedDevices.add(device);
            }
        }
        return devicesName;
    }


    public BluetoothDevice getDeviceFromName(String name){
        for (BluetoothDevice device: bondedDevices
                ) {
            if (device.getName().equals(name)){
                return device;
            }
        }
        return null;
    }




}
