package eu.se_bastiaan.tvnl.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import eu.se_bastiaan.tvnl.R;
import eu.se_bastiaan.tvnl.TVNLApplication;

public class MessageDialogFragment extends DialogFragment {

    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String CANCELABLE = "cancelable";

    DialogInterface.OnClickListener positiveOnClickListener, negativeOnClickListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (!getArguments().containsKey(TITLE) || !getArguments().containsKey(MESSAGE)) {
            return super.onCreateDialog(savedInstanceState);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getArguments().getString(TITLE))
                .setMessage(getArguments().getString(MESSAGE));

        if(positiveOnClickListener != null)
            builder.setPositiveButton(R.string.ok, positiveOnClickListener);
        if(negativeOnClickListener != null)
            builder.setPositiveButton(R.string.close, negativeOnClickListener);

        if(getArguments().getBoolean(CANCELABLE, true)) {
            setCancelable(true);
        } else {
            setCancelable(false);
        }

        return builder.create();
    }

    public static void show(FragmentManager fm, String title, String message, Boolean cancelable, DialogInterface.OnClickListener positiveOnClickListener, DialogInterface.OnClickListener negativeOnClickListener) {
        MessageDialogFragment dialogFragment = new MessageDialogFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MESSAGE, message);
        args.putBoolean(CANCELABLE, cancelable);
        dialogFragment.setArguments(args);
        dialogFragment.positiveOnClickListener = positiveOnClickListener;
        dialogFragment.negativeOnClickListener = negativeOnClickListener;
        dialogFragment.show(fm, "MessageDialogFragment");
    }

    public static void show(FragmentManager fm, int titleRes, int messageRes, Boolean cancelable, DialogInterface.OnClickListener positiveOnClickListener, DialogInterface.OnClickListener negativeOnClickListener) {
        show(fm, TVNLApplication.get().getString(titleRes), TVNLApplication.get().getString(messageRes), cancelable, positiveOnClickListener, negativeOnClickListener);
    }

    public static void dismiss(FragmentManager fm) {
        DialogFragment dialogFragment = (DialogFragment) fm.findFragmentByTag("MessageDialogFragment");
        if(dialogFragment != null)
            dialogFragment.dismiss();
    }

}