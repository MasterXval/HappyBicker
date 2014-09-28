package com.happybiker;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;


public class BleManager {

    private static final String TAG = BleManager.class.getSimpleName();
    private static final BleManager sInstance = new BleManager();

    private final BluetoothAdapter adapter;
    private BleScanner bleScanner;
    private BleConnection bleConnection;

    public static BleManager get() {
        return sInstance;
    }

    private BleManager() {
        BluetoothManager bluetoothManager = (BluetoothManager) Help.appCtx().getSystemService(Context.BLUETOOTH_SERVICE);
        adapter = bluetoothManager.getAdapter();
        bleScanner = new BleScanner(this, adapter);
    }

    public void scan(boolean scan) {
        bleScanner.scanLeDevice(scan);
    }

    public void onConnectDevice(BleConnection bleConnection) {
        this.bleConnection = bleConnection;
    }

    public void write(int msg) {
        if (bleConnection == null) {
            Log.i(TAG, "NO BLE CONNECTION");
            return;
        }

        bleConnection.writeMessage(msg);
    }

    public void stop() {
        scan(false);
        bleConnection.stop();
    }
}
