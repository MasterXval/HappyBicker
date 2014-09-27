package com.happybiker;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends Activity implements BluetoothAdapter.LeScanCallback {

    private BluetoothAdapter adapter;
    private boolean scanning;
    private BluetoothLeManager ble;
    private BluetoothGatt bluetoothGatt;

    @OnClick(R.id.left)
    public void turnLeft(){

    }

    @OnClick(R.id.right)
    public void turnRight(){

    }

    @OnClick(R.id.stop)
    public void goAhead(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        ble = new BluetoothLeManager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        adapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (adapter == null || !adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 10);
        }

        scanLeDevice(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(scanning)
            scanLeDevice(false);
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt = null;
        }
    }

    private void scanLeDevice(boolean enable) {
        if (enable) {
            scanning = true;
            adapter.startLeScan(this);
        } else {
            scanning = false;
            adapter.stopLeScan(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            bluetoothGatt = device.connectGatt(getApplicationContext(), true, ble);
            bluetoothGatt.connect();
        }
    }
}
