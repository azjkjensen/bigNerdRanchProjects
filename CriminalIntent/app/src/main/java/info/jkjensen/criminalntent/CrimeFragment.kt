package info.jkjensen.criminalntent

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_crime.*
import java.text.DateFormat
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class CrimeFragment : Fragment() {
    var crime: Crime? = null

    companion object {
        private val ARG_CRIME_ID = "crime_id"
        private val DIALOG_DATE = "DialogDate"
        private val REQUEST_DATE = 0

        public fun newInstance(crimeID: UUID):CrimeFragment{
            val args: Bundle = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeID)
            val fragment = CrimeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
//        val crimeID: UUID = this.activity.intent.getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID) as UUID
        val crimeID = arguments.getSerializable(ARG_CRIME_ID) as UUID
        crime = CrimeLab.get(this.activity)?.getCrimeByID(crimeID)
//        crime = Crime()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_crime, container, false)

        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crimeTitleEditText.setText(crime?.title)
        crimeSolvedCheckbox.isChecked = crime?.solved ?: false
        updateDate()

        crimeSolvedCheckbox.setOnCheckedChangeListener{ buttonView, isChecked ->
            crime?.solved = true
        }

        crimeTitleEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i("CrimeFragment", "Text changed!")
                crime?.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        crimeDateButton.setOnClickListener {
            val dialog: DatePickerFragment = DatePickerFragment.newInstance(crime?.date)
            dialog.setTargetFragment(this, REQUEST_DATE);
            dialog.show(fragmentManager, DIALOG_DATE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode != Activity.RESULT_OK){
            return
        }
        if(requestCode == REQUEST_DATE){
            crime?.date = data?.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
            updateDate()
        }
    }

    private fun updateDate() {
        crimeDateButton.text = DateFormat.getDateInstance().format(crime?.date)
    }
}
