package info.jkjensen.criminalntent

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import org.jetbrains.anko.support.v4.find
import java.util.*
import android.app.Activity


/**
 * Created by jk on 7/13/17.
 */
class DatePickerFragment: DialogFragment() {
    companion object {
        val ARG_DATE = "DatePickerFragment_date"
        val EXTRA_DATE = "com.bignerdranch.android.criminalintent.date"

        fun newInstance(date: Date?):DatePickerFragment{
            val args: Bundle = Bundle()
            args.putSerializable(ARG_DATE, date)
            val fragment = DatePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date: Date = arguments.getSerializable(ARG_DATE) as Date
        val cal:Calendar = Calendar.getInstance()
        cal.time = date
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val v: View = LayoutInflater.from(activity).inflate(R.layout.dialog_date, null)

        val datePicker = v.findViewById(R.id.datePicker) as DatePicker
        datePicker.init(year, month, day, null)

        return AlertDialog.Builder(activity)
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    val year = datePicker.year
                    val month = datePicker.month
                    val day = datePicker.dayOfMonth
                    val date = GregorianCalendar(year, month, day).time
                    sendResult(Activity.RESULT_OK, date)
                }
                .create()
    }

    private fun sendResult(resultCode:Int, date:Date?){
        if(targetFragment == null){
            return
        }
        val intent:Intent = Intent()
        intent.putExtra(EXTRA_DATE, date)
        targetFragment.onActivityResult(targetRequestCode, resultCode, intent)
    }
}