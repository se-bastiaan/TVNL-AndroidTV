package eu.se_bastiaan.tvnl.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.WindowManager;

import eu.se_bastiaan.tvnl.R;

public class ProgressOverlayDialogFragment extends DialogFragment {

    private static final String FRAGMENT_TAG = "ProgressOverlayDialogFragment";

    private DialogInterface.OnCancelListener onCancelListener;

    public ProgressOverlayDialogFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_TVNL_Dialog_FullScreen);
        dialog.setContentView(R.layout.dialog_overlay_progress);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);

        return dialog;
    }

    public static ProgressOverlayDialogFragment showLoadingProgress(FragmentManager fm) {
        dismissLoadingProgress(fm);
        ProgressOverlayDialogFragment fragment = new ProgressOverlayDialogFragment();
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