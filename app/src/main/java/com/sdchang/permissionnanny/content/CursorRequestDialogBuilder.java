package com.sdchang.permissionnanny.content;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.sdchang.permissionnanny.BaseDialogBuilder;
import com.sdchang.permissionnanny.C;
import com.sdchang.permissionnanny.R;
import com.sdchang.permissionnanny.ResponseBundle;
import com.sdchang.permissionnanny.lib.Nanny;
import com.sdchang.permissionnanny.lib.request.RequestParams;
import com.sdchang.permissionnanny.lib.request.content.CursorEvent;
import timber.log.Timber;

import java.security.SecureRandom;

/**
 *
 */
public class CursorRequestDialogBuilder extends BaseDialogBuilder<RequestParams> {

    private ContentOperation mOperation;

    @InjectView(R.id.tvReason) TextView tvReason;

    public CursorRequestDialogBuilder(Activity activity, Bundle args) {
        super(activity, args);
        mOperation = ContentOperation.getOperation(mRequest.uri0);
    }

    @Override
    protected CharSequence buildDialogTitle(CharSequence appLabel) {
        SpannableStringBuilder boldAppLabel = new SpannableStringBuilder(appLabel);
        boldAppLabel.setSpan(new StyleSpan(Typeface.BOLD), 0, appLabel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return boldAppLabel.append(C.SPACE).append(mActivity.getText(mOperation.mDialogTitle));
    }

    @Override
    public void inflateViewStub(ViewStub stub) {
        stub.setLayoutResource(R.layout.dialog_text);
        View view = stub.inflate();
        ButterKnife.inject(this, view);
        tvReason.setText(mReason);
    }

    @Override
    protected ResponseBundle onAllowRequest() {
        long nonce = new SecureRandom().nextLong();
        Timber.wtf("nonce=" + nonce);

        // cache request params
        CursorContentProvider.approvedRequests.put(nonce, mRequest);

        // return nonce to client
        Bundle response = new Bundle();
        response.putLong(CursorEvent.NONCE, nonce);
        return newAllowResponse()
                .connection(Nanny.CLOSE)
                .contentEncoding(Nanny.ENCODING_BUNDLE)
                .body(response);
    }
}
