package com.sdchang.permissionpolice.lib.request.location;

import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.GpsStatus.Listener;
import android.location.GpsStatus.NmeaListener;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import auto.parcel.AutoParcel;
import com.sdchang.permissionpolice.lib.request.BaseRequest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.SecureRandom;

/**
 *
 */
@AutoParcel
public abstract class LocationRequest extends BaseRequest {

    static Builder newBuilder() {
        return new AutoParcel_LocationRequest.Builder();
    }

    @AutoParcel.Builder
    static abstract class Builder {
        public abstract Builder opCode(String value);

        public abstract Builder long0(long value);

        public abstract Builder float0(float value);

        public abstract Builder double0(double value);

        public abstract Builder double1(double value);

        public abstract Builder criteria0(Criteria value);

        public abstract Builder pendingIntent0(PendingIntent value);

        public abstract Builder string0(String value);

        public abstract LocationRequest build();
    }

    private LocationListener mLocationListener;
    private Handler mLooper;

    @Op
    public abstract String opCode();

    @Nullable
    public abstract long long0();

    @Nullable
    public abstract float float0();

    @Nullable
    public abstract double double0();

    @Nullable
    public abstract double double1();

    @Nullable
    public abstract Criteria criteria0();

    @Nullable
    public abstract PendingIntent pendingIntent0();

    @Nullable
    public abstract String string0();

    @Override
    public int getRequestType() {
        return LOCATION_REQUEST;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({REQUEST_LOCATION_UPDATES})
    public @interface Op {}

    public static final String ADD_GPS_STATUS_LISTENER = "addGpsStatusListener";
    public static final String ADD_NMEA_LISTENER = "addNmeaListener";
    public static final String ADD_PROXIMITY_ALERT = "addProximityAlert";
    public static final String GET_LAST_KNOWN_LOCATION = "getLastKnownLocation";
    public static final String REMOVE_GPS_STATUS_LISTENER = "removeGpsStatusListener";
    public static final String REMOVE_NMEA_LISTENER = "removeNmeaListener";
    public static final String REMOVE_PROXIMITY_ALERT = "removeProximityAlert";
    public static final String REMOVE_UPDATES = "removeUpdates";
    public static final String REMOVE_UPDATES1 = "removeUpdates1";
    public static final String REQUEST_LOCATION_UPDATES = "requestLocationUpdates";
    public static final String REQUEST_LOCATION_UPDATES1 = "requestLocationUpdates1";
    public static final String REQUEST_LOCATION_UPDATES2 = "requestLocationUpdates2";
    public static final String REQUEST_LOCATION_UPDATES3 = "requestLocationUpdates3";
    public static final String REQUEST_LOCATION_UPDATES4 = "requestLocationUpdates4";
    public static final String requestSingleUpdate = "requestSingleUpdate";
    public static final String requestSingleUpdate1 = "requestSingleUpdate1";
    public static final String requestSingleUpdate2 = "requestSingleUpdate2";
    public static final String requestSingleUpdate3 = "requestSingleUpdate3";

    public static LocationRequest addGpsStatusListener(Listener listener) {
        return newBuilder().opCode(ADD_GPS_STATUS_LISTENER).build();
    }

    public static LocationRequest addNmeaListener(NmeaListener listener) {
        return newBuilder().opCode(ADD_NMEA_LISTENER).build();
    }

    public static LocationRequest addProximityAlert(double latitude, double longtitude, float radius, long
            expiration, PendingIntent intent) {
        return newBuilder().opCode(ADD_PROXIMITY_ALERT).double0(latitude).double1(longtitude).float0(radius)
                .long0(expiration).pendingIntent0(intent).build();
    }

    public static LocationManager getLastKnownLocation(String provider) {
//        return newBuilder().opCode(GET_LAST_KNOWN_LOCATION).string0(provider).build();
        return null;
    }

    public static LocationRequest removeGpsStatusListener(Listener listener) {
        return newBuilder().opCode(REMOVE_GPS_STATUS_LISTENER).build();
    }

    public static LocationRequest removeNmeaListener(NmeaListener listener) {
        return newBuilder().opCode(REMOVE_NMEA_LISTENER).build();
    }

    public static LocationRequest removeProximityAlert(PendingIntent intent) {
        return newBuilder().opCode(REMOVE_PROXIMITY_ALERT).pendingIntent0(intent).build();
    }

    public static LocationRequest removeUpdates(PendingIntent intent) {
        return newBuilder().opCode(REMOVE_UPDATES).pendingIntent0(intent).build();
    }

    public static LocationRequest removeUpdates(LocationListener listener) {
        return newBuilder().opCode(REMOVE_UPDATES1).build();
    }

    public static LocationRequest requestLocationUpdates(long minTime,
                                                         float minDistance,
                                                         Criteria criteria,
                                                         PendingIntent intent) {
        return newBuilder().opCode(REQUEST_LOCATION_UPDATES).long0(minTime).float0(minDistance).criteria0(criteria)
                .pendingIntent0(intent).build();
    }

    public static LocationRequest requestLocationUpdates(long minTime,
                                                         float minDistance,
                                                         Criteria criteria,
                                                         LocationListener listener,
                                                         Looper looper) {
        LocationRequest request = newBuilder().opCode(REQUEST_LOCATION_UPDATES1).long0(minTime).float0(minDistance)
                .criteria0(criteria).build();
        request.mLocationListener = listener;
        request.mLooper = looper != null ? new Handler(looper) : new Handler();
        return request;
    }

    @Override
    public void startRequest(Context context, String reason) {
        String clientId = Long.toString(new SecureRandom().nextLong());
        context.registerReceiver(new LocationReceiver(mLocationListener, mLooper), new IntentFilter
                (clientId));
        context.sendBroadcast(newIntent(context, reason, clientId));
    }
}

