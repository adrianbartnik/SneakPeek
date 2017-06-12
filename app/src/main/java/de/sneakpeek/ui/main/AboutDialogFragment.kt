package de.sneakpeek.ui.main


import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.ImageView
import android.widget.TextView
import de.sneakpeek.BuildConfig
import de.sneakpeek.R

class AboutDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        val layout = activity.layoutInflater.inflate(R.layout.dialog_about, null)
        
        val text = layout.findViewById(R.id.dialog_about_text) as TextView
        text.text = context.getString(R.string.dialog_about_text, BuildConfig.VERSION_NAME)

        val githubLogo = layout.findViewById(R.id.dialog_about_github) as ImageView

        githubLogo.setOnClickListener {
            val url = "https://github.com/whoww/SneakPeek"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        builder.setCancelable(true)
                .setTitle(context.getString(R.string.dialog_about_title))
                .setView(layout)

        return builder.create()
    }

    companion object {
        val TAG = AboutDialogFragment::class.java.simpleName
    }
}
