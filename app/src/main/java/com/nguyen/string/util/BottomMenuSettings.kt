package com.nguyen.string.util

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nguyen.string.R


object BottomMenuSettings{

    private var dialog: BottomSheetDialog? = null
    private var view: View? = null
    private var close: ImageView? = null
    private var shareLink: TextView? = null
    private var copy: TextView? = null
    private var report: TextView? = null

    private var link: String? = null

    fun create(context: Context, layoutInflater: LayoutInflater){
        dialog = BottomSheetDialog(context)
        view = layoutInflater.inflate(R.layout.bottom_menu_settings, null)


        closeButtonInit(context)
        shareButtonInit(context)
        copyButtonInit(context)
        reportButtonInit(context)

        dialog!!.setContentView(view!!)
    }



    private fun closeButtonInit(context: Context){
        close = view!!.findViewById(R.id.bsm_close_button)
        close!!.setOnClickListener {
            dialog!!.dismiss()
        }
    }


    private fun shareButtonInit(context: Context){
        shareLink = view!!.findViewById(R.id.share_button)
        shareLink!!.setOnClickListener {
            if(link == null){
                Toast.makeText(context, "Cannot share this post", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, link)
            sendIntent.type = "text/plain"
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    }

    private fun copyButtonInit(context: Context){
        copy = view!!.findViewById(R.id.copy_link_button)
        copy!!.setOnClickListener {
            if(link == null){
                Toast.makeText(context, "Cannot copy this link", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val clipboard: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("Link copied", link)
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(context, "Link copied!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun reportButtonInit(context: Context){
        report = view!!.findViewById(R.id.report_button)
        report!!.setOnClickListener {
            val mailIntent = Intent(Intent.ACTION_MAIN)
            mailIntent.addCategory(Intent.CATEGORY_APP_EMAIL)
            context.startActivity(mailIntent)
        }
    }



    fun show(_link: String?){
        if(dialog != null && view != null){
            link = _link
            Log.d("Share", "Link $link")
            dialog!!.show()
        }
    }
}