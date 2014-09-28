package com.happybiker;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.text.TextUtils;
import android.util.Log;


public class BleScanner implements BluetoothAdapter.LeScanCallback {

    private final BleManager manager;
    private boolean scanning;
    private BluetoothAdapter adapter;

    public BleScanner(BleManager manager, BluetoothAdapter adapter) {
        this.adapter = adapter;
        this.manager = manager;
    }

    public void scanLeDevice(boolean enable) {
        if (enable && !scanning) {
            scanning = true;
            adapter.startLeScan(this);
        } else if (!enable && scanning) {
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
            scanLeDevice(false);
            BleConnection bleConnection = new BleConnection();
            BluetoothGatt bluetoothGatt = device.connectGatt(Help.appCtx(), true, bleConnection);
            bleConnection.setBluetoothGatt(bluetoothGatt);
            bluetoothGatt.connect();
            manager.onConnectDevice(bleConnection);
        }
    }
}
