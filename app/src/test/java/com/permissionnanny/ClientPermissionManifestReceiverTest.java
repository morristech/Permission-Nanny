package com.permissionnanny;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.permissionnanny.common.test.NannyTestCase;
import com.permissionnanny.data.AppPermissionManager;
import com.permissionnanny.lib.Nanny;
import com.permissionnanny.lib.NannyException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static com.permissionnanny.common.test.AndroidMatchers.*;
import static com.permissionnanny.common.test.Mockingbird.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(NannyAppTestRunner.class)
public class ClientPermissionManifestReceiverTest extends NannyTestCase {

    @ClassRule public static final RuleChain CLASS_RULES = NannyAppTestRunner.newClassRules();
    @Rule public final RuleChain TEST_RULES = NannyAppTestRunner.newTestRules(this);

    ClientPermissionManifestReceiver target;
    Intent intent;
    Bundle bundle;
    ArrayList<String> manifest;
    @Mock Context context;
    @Mock AppPermissionManager mgr;
    @Mock PendingIntent sender;
    @Captor ArgumentCaptor<Intent> responseCaptor;

    @Before
    public void setUp() throws Exception {
        target = new ClientPermissionManifestReceiver();
        target.mAppManager = mgr;
        intent = new Intent();
        bundle = new Bundle();
        manifest = new ArrayList<>();
        when(context.getApplicationContext()).thenReturn(new RoboApp());
    }

    @Test
    public void onReceiveShouldSaveClientPermissionManifest() throws Exception {
        intent.putExtra(Nanny.CLIENT_ADDRESS, "123");
        intent.putExtra(Nanny.ENTITY_BODY, bundle);
        bundle.putParcelable(Nanny.SENDER_IDENTITY, sender);
        mockPendingIntent(sender, "com.3rd.party.app");
        bundle.putStringArrayList(Nanny.PERMISSION_MANIFEST, manifest);

        target.onReceive(context, intent);

        verify(target.mAppManager).readPermissionManifest("com.3rd.party.app", manifest);
        verify(context).sendBroadcast(responseCaptor.capture());
        assertThat(responseCaptor.getValue(), equalToIntent(AppTestUtil.new200Response("123")));
    }

    @Test
    public void onReceiveShouldReturn400WhenEntityBodyIsMissing() throws Exception {
        intent.putExtra(Nanny.CLIENT_ADDRESS, "123");

        target.onReceive(context, intent);

        verify(context).sendBroadcast(responseCaptor.capture());
        assertThat(responseCaptor.getValue(), equalToIntent(new400Response("123", Err.NO_ENTITY)));
    }

    @Test
    public void onReceiveShouldReturn400WhenSenderIdentityIsMissing() throws Exception {
        intent.putExtra(Nanny.CLIENT_ADDRESS, "123");
        intent.putExtra(Nanny.ENTITY_BODY, bundle);

        target.onReceive(context, intent);

        verify(context).sendBroadcast(responseCaptor.capture());
        assertThat(responseCaptor.getValue(), equalToIntent(new400Response("123", Err.NO_SENDER_IDENTITY)));
    }

    @Test
    public void onReceiveShouldReturn400WhenPermissionManifestIsMissing() throws Exception {
        intent.putExtra(Nanny.CLIENT_ADDRESS, "123");
        intent.putExtra(Nanny.ENTITY_BODY, bundle);
        bundle.putParcelable(Nanny.SENDER_IDENTITY, sender);
        mockPendingIntent(sender, "com.3rd.party.app");

        target.onReceive(context, intent);

        verify(context).sendBroadcast(responseCaptor.capture());
        assertThat(responseCaptor.getValue(), equalToIntent(new400Response("123", Err.NO_PERMISSION_MANIFEST)));
    }

    private Intent new400Response(String clientAddr, String error) {
        return AppTestUtil.new400Response(clientAddr, Nanny.PERMISSION_MANIFEST_SERVICE, new NannyException(error));
    }
}
