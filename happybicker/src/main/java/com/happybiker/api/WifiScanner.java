package com.happybiker.api;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.ArrayMap;
import android.util.Log;

import com.happybiker.Help;

public class WifiScanner implements NsdManager.DiscoveryListener {

    private static final String TAG = WifiScanner.class.getSimpleName();
    private static final String SERVICE_NAME = "";
    private final NsdManager nsdManager;
    private final Callback callback;
    private boolean scanning = false;
    private ArrayMap<String, NsdServiceInfo> discoveredDevices = new ArrayMap<>();
    private List<NsdServiceInfo> resolvedDevices = new ArrayList<>();

    public interface Callback {
        public void onServiceFound(List<NsdServiceInfo> services);
    }

    public WifiScanner(Callback communicationManager) {
        this.callback = communicationManager;
        nsdManager = (NsdManager) Help.appCtx().getSystemService(Context.NSD_SERVICE);
    }

    public void scan() {
        if (scanning) {
            return;
        }

        scanning = true;
        nsdManager.discoverServices(SERVICE_NAME, NsdManager.PROTOCOL_DNS_SD, this);
    }

    public void stopScan() {
        if (!scanning) {
            return;
        }

        scanning = false;
        nsdManager.stopServiceDiscovery(this);
    }

// NsdManager.DiscoveryListener

    @Override
    public void onDiscoveryStarted(String regType) {
        Log.d(TAG, "Service discovery started");
        discoveredDevices.clear();
    }

    @Override
    public void onServiceFound(NsdServiceInfo service) {
        Log.d(TAG, "Service discovery success" + service);
        if (!service.getServiceType().contains(SERVICE_NAME)) {
            Log.e(TAG, "Unknown Service Type: " + service.getServiceType());
        } else {
            discoveredDevices.put(service.getServiceName(), service);
        }
    }

    @Override
    public void onServiceLost(NsdServiceInfo service) {
        Log.e(TAG, "service lost" + service);
        discoveredDevices.remove(service.getServiceName());
    }

    @Override
    public void onDiscoveryStopped(String serviceType) {
        Log.i(TAG, "Discovery stopped: " + serviceType);
        if (!discoveredDevices.isEmpty()) {
            for (NsdServiceInfo nsdServiceInfo : discoveredDevices.values()) {
                nsdManager.resolveService(nsdServiceInfo, new MyResolver(callback, nsdServiceInfo.getServiceName()));
            }
        }
    }

    @Override
    public void onStartDiscoveryFailed(String serviceType, int errorCode) {
        Log.e(TAG, "Discovery failed: Error code:" + errorCode);
        nsdManager.stopServiceDiscovery(this);
    }

    @Override
    public void onStopDiscoveryFailed(String serviceType, int errorCode) {
        Log.e(TAG, "Discovery failed: Error code:" + errorCode);
        nsdManager.stopServiceDiscovery(this);
    }

// Resolver

    private static class MyResolver implements NsdManager.ResolveListener {
        private final String name;
        private final WifiScanner wifiScanner;

        public MyResolver(WifiScanner wifiScanner, String name) {
            this.wifiScanner = wifiScanner;
            this.name = name;
        }

        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.w(TAG, "Resolve failed for " + name + " : " + errorCode);
            wifiScanner.discoveredDevices.remove(name);

            //Check if it's the last resolved device
            if (wifiScanner.discoveredDevices.isEmpty()) {
                wifiScanner.callback.onServiceFound(wifiScanner.resolvedDevices);
            }
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            Log.i(TAG, "Resolve Succeeded. " + name);

            wifiScanner.resolvedDevices.add(serviceInfo);
            wifiScanner.discoveredDevices.remove(name);

            //Check if it's the last resolved device
            if (wifiScanner.discoveredDevices.isEmpty()) {
                wifiScanner.callback.onServiceFound(wifiScanner.resolvedDevices);
            }
        }
    }
}
