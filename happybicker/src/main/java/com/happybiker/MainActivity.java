package com.happybiker;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends Activity {


    private BleManager bleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        bleManager = BleManager.get();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothAdapter adapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (adapter == null || !adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 10);
        }

        bleManager.scan(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bleManager.stop();
    }

// Directions

    @OnClick(R.id.left)
    public void turnLeft(){
        bleManager.write(0);
    }

    @OnClick(R.id.right)
    public void turnRight(){
        bleManager.write(1);
    }

    @OnClick(R.id.stop)
    public void goAhead(){
        bleManager.write(2);
    }
}
