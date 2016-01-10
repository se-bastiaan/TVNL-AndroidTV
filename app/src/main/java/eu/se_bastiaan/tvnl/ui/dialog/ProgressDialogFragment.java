package eu.se_bastiaan.tvnl.ui.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

public class ProgressDialogFragment extends DialogFragment {

    private static final String FRAGMENT_TAG = "ProgressDialogFragment";
    private static final String MESSAGE_ARG = "message";
    private static final String CANCELABLE_ARG = "cancelable";

    private DialogInterface.OnCancelListener onCancelListener;

    public ProgressDialogFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());

        dialog.setMessage(getArguments().getString(MESSAGE_ARG));
        dialog.setIndeterminate(true);
        dialog.setCancelable(getArguments().getBoolean(CANCELABLE_ARG, false));

        return dialog;
    }

    public static ProgressDialogFragment showLoadingProgress(FragmentManager fm, String message, Boolean cancelable) {
        dismissLoadingProgress(fm);
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        Bundle args = new Bundle();
        args.putString(MESSAGE_ARG, message);
        args.putBoolean(CANCELABLE_ARG, cancelable);
        fragment.setArguments(args);
        fragment.show(fm, FRAGMENT_TAG);
        return fragment;
    }

    public static void dismissLoadingProgress(FragmentManager fm) {
        DialogFragment frag = (DialogFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (frag != null) {
            frag.dismiss();
        }
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if(onCancelListener != null)
            onCancelListener.onCancel(dialog);
    }
}