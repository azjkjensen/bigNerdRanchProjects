package info.jkjensen.criminalntent

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import info.jkjensen.criminalntent.utils.getScaledBitmap
import kotlinx.android.synthetic.main.dialog_image_detail.*

/**
 * Created by jk on 7/20/17.
 */
class ImageDetailFragment: DialogFragment() {

    companion object {
        val ARG_PATH = "ImageDetailFragment_path"

        fun newInstance(path: String): ImageDetailFragment {
            val args: Bundle = Bundle()
            args.putSerializable(ARG_PATH, path)
            val fragment = ImageDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var path:String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        path = arguments.getSerializable(ARG_PATH) as String

        val v: View = LayoutInflater.from(activity).inflate(R.layout.dialog_image_detail, null)

        val dialog: Dialog =  AlertDialog.Builder(activity)
                .setView(v)
//                .setTitle(null)
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
//                    return dialog
                }
                .create()

        val bm: Bitmap? = getScaledBitmap(path, activity)
        val imageView = v.findViewById(R.id.detailImageView) as ImageView
        imageView.layoutParams.height =  (bm!!.height + bm!!.height) ?: imageView.layoutParams.height
        imageView.requestLayout()
        imageView.setImageBitmap(bm)

        return dialog
    }

}