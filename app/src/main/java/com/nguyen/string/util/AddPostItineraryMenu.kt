package com.nguyen.string.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nguyen.string.R


object AddPostItineraryMenu{

    private var dialog: BottomSheetDialog? = null
    private var view: View? = null
    private var close: ImageView? = null
    private var camera: ImageView? = null
    private var map: ImageView? = null


    fun create(context: Context, layoutInflater: LayoutInflater){
        dialog = BottomSheetDialog(context)
        view = layoutInflater.inflate(R.layout.add_menu, null)

        camera = view?.findViewById(R.id.camera_ic)
        map = view?.findViewById(R.id.map_ic)

        closeButtonInit()

        dialog!!.setContentView(view!!)
    }



    private fun closeButtonInit(){
        close = view!!.findViewById(R.id.bsm_close_button)
        close!!.setOnClickListener {
            dialog!!.dismiss()
        }
    }


    fun onClickPost(clickFunction : () -> Unit){
        camera?.setOnClickListener {
            clickFunction.invoke()
            dialog!!.dismiss()
        }

    }

    fun onClickItinerary(clickFunction : () -> Unit){
        map?.setOnClickListener {
            clickFunction.invoke()
            dialog!!.dismiss()
        }

    }

    fun show(){
        if(dialog != null && view != null){
            dialog!!.show()
        }
    }
}