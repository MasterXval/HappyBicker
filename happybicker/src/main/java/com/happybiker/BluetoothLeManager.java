package com.happybiker;

import android.bluetooth.*;
import android.util.Log;

import java.util.List;
import java.util.UUID;

public class BluetoothLeManager extends BluetoothGattCallback {

    private UUID SERVICE_UUID = UUID.fromString("01010101-0101-0101-0101-010101010101");
    private UUID CHAR_UUID = UUID.fromString("01010101-0101-0101-0101-010101524742");

    private BluetoothGattCharacteristic characteristic;
    private BluetoothGatt bluetoothGatt;

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        List<BluetoothGattService> services = gatt.getServices();
        for (BluetoothGattService service : services) {
            if (service.getUuid().equals(SERVICE_UUID)) {
                bluetoothGatt = gatt;
                characteristic = service.getCharacteristic(CHAR_UUID);
            }
        }
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Log.i("BLE", "Attempting to start service discovery");
            gatt.discoverServices();
        }
    }

    public void writeMessage(int message) {
        if (bluetoothGatt != null) {

        }
    }

}
