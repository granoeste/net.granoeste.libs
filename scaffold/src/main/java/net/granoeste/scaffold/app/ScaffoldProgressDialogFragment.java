package net.granoeste.scaffold.app;

import static net.granoeste.commons.util.LogUtils.makeLogTag;

import org.apache.commons.lang3.StringUtils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ScaffoldProgressDialogFragment extends ScaffoldDialogFragment {
    private static final String TAG = makeLogTag(ScaffoldProgressDialogFragment.class);

    private static final int STYLE = DialogFragment.STYLE_NORMAL;
    private static final int THEME = 0;

    private static final String ICON_ID = "iconId";
    private static final String MESSAGE = "message";
    private static final String TITLE = "title";
    private static final String CANCELABLE = "cancelable";

    private ProgressDialog mProgressDialog;

    /**
     * newInstance
     *
     * @param iconId
     * @param title
     * @param message
     * @return
     */
    public static ScaffoldProgressDialogFragment newInstance(
            final int iconId, final String title, final String message) {
        final ScaffoldProgressDialogFragment frag = new ScaffoldProgressDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ICON_ID, iconId);
        args.putString(TITLE, title);
        args.putString(MESSAGE, message);
        args.putBoolean(CANCELABLE, false);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE, THEME);

        // TIPS: ProgressDialog#setCancelable() では有効にならない...?
        final boolean cancelable = getArguments().getBoolean(CANCELABLE, true);
        setCancelable(cancelable);
    }
    
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final int iconId = getArguments().getInt(ICON_ID, 0);
        final String title = getArguments().getString(TITLE);
        final String message = getArguments().getString(MESSAGE);
        final boolean cancelable = getArguments().getBoolean(CANCELABLE, true);

        if (mProgressDialog != null) {
            return mProgressDialog;
        }

        mProgressDialog = new ProgressDialog(getActivity());
        if (iconId > 0) {
            mProgressDialog.setIcon(iconId);
        }
        if (StringUtils.isNoneEmpty(title)) {
            mProgressDialog.setTitle(title);
        }
        if (StringUtils.isNoneEmpty(message)) {
            mProgressDialog.setMessage(message);
        }
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(cancelable);
        return mProgressDialog;
    }

    @Override
    public void onDestroy(){
       super.onDestroy();
       mProgressDialog = null;
    }

}
