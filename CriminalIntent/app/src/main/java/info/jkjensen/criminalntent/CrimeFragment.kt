package info.jkjensen.criminalntent

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.fragment_crime.*
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.provider.ContactsContract
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.support.v4.app.ShareCompat
import android.support.v4.content.FileProvider
import java.io.File
import android.content.pm.ResolveInfo




/**
 * A placeholder fragment containing a simple view.
 */
class CrimeFragment : Fragment() {
    var crime: Crime? = null

    companion object {
        private val ARG_CRIME_ID = "crime_id"
        private val DIALOG_DATE = "DialogDate"
        private val REQUEST_DATE = 0
        private val REQUEST_CONTACT = 1
        private val REQUEST_PHOTO = 2

        public fun newInstance(crimeID: UUID):CrimeFragment{
            val args: Bundle = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeID)
            val fragment = CrimeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val crimeID = arguments.getSerializable(ARG_CRIME_ID) as UUID
        crime = CrimeLab.get(this.activity)?.getCrimeByID(crimeID)
        setHasOptionsMenu(true)
        photoFile = CrimeLab.get(this.activity)?.getPhotoFile(crime)
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

        crimeSolvedCheckbox.setOnCheckedChangeListener{ _, isChecked ->
            crime?.solved = isChecked
            CrimeLab.get(activity.baseContext)?.updateCrime(crime!!)
        }

        crimeTitleEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i("CrimeFragment", "Text changed!")
                crime!!.title = s.toString()
                CrimeLab.get(activity.baseContext)?.updateCrime(crime!!)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        crimeDateButton.setOnClickListener {
            val dialog: DatePickerFragment = DatePickerFragment.newInstance(crime?.date)
            dialog.setTargetFragment(this, REQUEST_DATE);
            dialog.show(fragmentManager, DIALOG_DATE)
        }

        deleteCrimeButton.setOnClickListener {
            deleteCrime()
        }

        crimeReportButton.setOnClickListener {
            val i = ShareCompat.IntentBuilder
                    .from(activity)
                    .setType("text/plain")
                    .setText(getCrimeReport())
                    .setSubject(getString(R.string.crime_report_subject))
                    .setChooserTitle(getString(R.string.send_report))
                    .createChooserIntent()
            startActivity(i)
        }

        val pickContact = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)


        val packageManager = activity.packageManager
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            crimeSuspectButton.isEnabled = false
        }else {
            crimeSuspectButton.setOnClickListener {
                startActivityForResult(pickContact, REQUEST_CONTACT)
            }
        }

        if(crime?.suspect != null && crime?.suspect != ""){
            crimeSuspectButton.text = crime?.suspect
        }

        val captureImage: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val canTakePhoto = photoFile != null && captureImage.resolveActivity(packageManager) != null
        crimeCameraButton.isEnabled = canTakePhoto
        crimeCameraButton.setOnClickListener{
            val uri:Uri = FileProvider.getUriForFile(activity,
                    "info.jkjensen.criminalintent.fileprovider",
                    photoFile)
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            val cameraActivities: List<ResolveInfo> = activity
                    .packageManager.queryIntentActivities(captureImage,
                    PackageManager.MATCH_DEFAULT_ONLY)

            for (activity in cameraActivities) {
                getActivity().grantUriPermission(activity.activityInfo.packageName,
                        uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }

            startActivityForResult(captureImage, REQUEST_PHOTO)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.delete_crime -> {
                deleteCrime()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode != Activity.RESULT_OK){
            return
        }
        when(requestCode) {
            REQUEST_DATE -> {
                crime!!.date = data?.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
                CrimeLab.get(activity.baseContext)?.updateCrime(crime!!)
                updateDate()
            }
            REQUEST_CONTACT -> {
                if(data != null){
                    val contactUri: Uri? = data.getData()
                    val queryFields: Array<String> = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

                    val c = activity.contentResolver.query(contactUri, queryFields, null, null, null)

                    try{
                        // Double-check that you actually got results
                        if (c.count == 0) {
                            return
                        }

                        // Pull out the first column of the first row of data -
                        // that is your suspect's name
                        c.moveToFirst()
                        val suspect:String = c.getString(0)
                        crime?.suspect = suspect
                        crimeSuspectButton.text = suspect
                        CrimeLab.get(activity.baseContext)?.updateCrime(crime!!)
                    } finally {
                        c.close()
                    }
                }
            }
        }
    }

    private fun updateDate() {
        crimeDateButton.text = SimpleDateFormat("dd MMM").format(crime?.date)
    }

    private fun deleteCrime(){
        CrimeLab.get(activity)?.removeCrimeById(crime?.id)
        activity.finish()
    }

    private fun getCrimeReport(): String? {
        var solvedString: String
        if(crime!!.solved){
            solvedString = getString(R.string.crime_report_solved)
        } else{
            solvedString = getString(R.string.crime_report_unsolved)
        }

        val dateFormat = "EEE, MMM dd"
        val dateString = DateFormat.format(dateFormat, crime!!.date)

        var suspect = crime?.suspect
        if(suspect == null){
            suspect = getString(R.string.crime_report_no_suspect)
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect)
        }

        val report = getString(R.string.crime_report, crime!!.title, dateString, solvedString, suspect)

        return report
    }
}
