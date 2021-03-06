package com.permissionnanny.demo.telephony;

import android.app.Dialog;
import android.content.Context;
import com.permissionnanny.demo.DataAdapter;
import com.permissionnanny.demo.ResponseDisplayListener;
import com.permissionnanny.demo.SimpleRequestFactory;
import com.permissionnanny.lib.request.simple.SimpleRequest;
import com.permissionnanny.lib.request.simple.TelephonyRequest;

/**
 *
 */
public class TelephonyRequestFactory implements SimpleRequestFactory {
    private static String[] mLabels = new String[]{
            TelephonyRequest.GET_ALL_CELL_INFO,
            TelephonyRequest.GET_DEVICE_ID,
            TelephonyRequest.GET_DEVICE_SOFTWARE_VERSION,
            TelephonyRequest.GET_GROUP_ID_LEVEL_1,
            TelephonyRequest.GET_LINE_1_NUMBER,
            TelephonyRequest.GET_NEIGHBORING_CELL_INFO,
            TelephonyRequest.GET_SIM_SERIAL_NUMBER,
            TelephonyRequest.GET_SUBSCRIBER_ID,
            TelephonyRequest.GET_VOICE_MAIL_ALPHA_TAG,
            TelephonyRequest.GET_VOICE_MAIL_NUMBER,
    };

    @Override
    public SimpleRequest getRequest(int position, DataAdapter adapter) {
        return getRequest(position).listener(new ResponseDisplayListener(position, adapter));
    }

    public SimpleRequest getRequest(int position) {
        switch (position) {
        case 0:
            return TelephonyRequest.getAllCellInfo();
        case 1:
            return TelephonyRequest.getDeviceId();
        case 2:
            return TelephonyRequest.getDeviceSoftwareVersion();
        case 3:
            return TelephonyRequest.getGroupIdLevel1();
        case 4:
            return TelephonyRequest.getLine1Number();
        case 5:
            return TelephonyRequest.getNeighboringCellInfo();
        case 6:
            return TelephonyRequest.getSimSerialNumber();
        case 7:
            return TelephonyRequest.getSubscriberId();
        case 8:
            return TelephonyRequest.getVoiceMailAlphaTag();
        case 9:
            return TelephonyRequest.getVoiceMailNumber();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mLabels.length;
    }

    @Override
    public String getLabel(int position) {
        return mLabels[position];
    }

    @Override
    public boolean hasExtras(int position) {
        return false;
    }

    @Override
    public Dialog buildDialog(Context context, int position) {
        return null;
    }
}
