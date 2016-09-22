package de.sneak.sneakpeek.ui.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import de.sneak.sneakpeek.R;

public class AboutDialogFragment extends DialogFragment {

    public static final String TAG = AboutDialogFragment.class.getSimpleName();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_about, null);

        ImageView githubLogo = (ImageView) layout.findViewById(R.id.dialog_about_github);

        githubLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/whoww/SneakPeek";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        builder.setCancelable(true)
                .setView(layout);

        return builder.create();
    }
}
