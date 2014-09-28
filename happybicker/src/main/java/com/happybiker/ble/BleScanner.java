package com.happybiker.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.text.TextUtils;
import android.util.Log;

import com.happybiker.Help;


public class BleScanner implements BluetoothAdapter.LeScanCallback {

    private final BleManager manager;
    private boolean scanning;
    private BluetoothAdapter adapter;

    public BleScanner(BleManager manager, BluetoothAdapter adapter) {
        this.adapter = adapter;
        this.manager = manager;
    }

    void scanLeDevice(boolean enable) {
        if (enable && !scanning) {
            Log.i("BLE", "Start SCAN");
            scanning = true;
            adapter.startLeScan(this);
        } else if (!enable && scanning) {
            Log.i("BLE", "Stop SCAN");
            scanning = false;
            adapter.stopLeScan(this);
        }
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (device == null) {
            return;
        }
        String deviceName = device.getName();
        Log.i("BLE", "New Device scanned, " + deviceName + ":" + device.getAddress());

        if (!TextUtils.isEmpty(deviceName) && deviceName.equals("Helmet")) {
            Log.i("BLE", "Connecting to Helmet");
            scanLeDevice(false);
            BleConnection bleConnection = new BleConnection();
            BluetoothGatt bluetoothGatt = device.connectGatt(Help.appCtx(), true, bleConnection);
            bleConnection.setBluetoothGatt(bluetoothGatt);
            bluetoothGatt.connect();
            manager.onConnectDevice(bleConnection);
        }
    }
}
