/*
 * This file is part of Butter.
 *
 * Butter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Butter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Butter. If not, see <http://www.gnu.org/licenses/>.
 */

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