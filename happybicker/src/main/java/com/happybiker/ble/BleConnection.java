package com.happybiker.ble;

import android.bluetooth.*;
import android.util.Log;

import java.nio.ByteBuffer;
import java.security.acl.LastOwnerException;
import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT;

public class BleConnection extends BluetoothGattCallback {

    private static final String TAG = BleConnection.class.getSimpleName();
    private UUID SERVICE_UUID = UUID.fromString("01010101-0101-0101-0101-010101010101");
    private UUID CHAR_UUID = UUID.fromString("01010101-0101-0101-0101-010101524742");

    private BluetoothGattCharacteristic characteristic;
    private BluetoothGatt bluetoothGatt;

    void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt;
    }

    void writeMessage(int message) {
        if (characteristic == null) {
            Log.e(TAG, "NO CHARACTERISTIC");
            return;
        }

        ByteBuffer bf = ByteBuffer.allocate(1);
        bf.put((byte) message);
        characteristic.setValue(bf.array());
        characteristic.setWriteType(WRITE_TYPE_DEFAULT);
        bluetoothGatt.writeCharacteristic(characteristic);
    }

    void stop() {
        bluetoothGatt.disconnect();
    }

// BluetoothGattCallback

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        List<BluetoothGattService> services = gatt.getServices();
        for (BluetoothGattService service : services) {
            if (service.getUuid().equals(SERVICE_UUID)) {
                Log.i("BLE", "GOT SERVICE");
                characteristic = service.getCharacteristic(CHAR_UUID);

                if (characteristic == null) {
                    Log.e("BLE", "CANNOT FIND CHARACTERISTIC");
                    for (BluetoothGattCharacteristic  chara : service.getCharacteristics()) {
                        Log.i("BLE", "CHAR : " + chara.getUuid().toString());
                    }
                } else {
                    Log.i("BLE", "GOT CHARACTERISTIC : " + characteristic);
                }
            }
        }

        if (characteristic == null) {
            Log.e("BLE", "NO CHARACTERISTIC FOUND");
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

    @Override
    public void onCharacteristicWrite(
            BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        Log.i(TAG, "onCharacteristicWrite() status : " + status);
    }
}
