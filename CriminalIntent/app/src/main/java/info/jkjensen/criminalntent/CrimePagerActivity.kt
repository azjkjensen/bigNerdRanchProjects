package info.jkjensen.criminalntent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_crime_pager.*
import java.util.*


/**
 * Created by jk on 7/12/17.
 */
class CrimePagerActivity: AppCompatActivity() {

    companion object {
        val EXTRA_CRIME_ID: String = "criminalintent_crime_id"
    }

    private var crimes: MutableList<Crime>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)
        crimes = CrimeLab.get(this)?.crimes

        val crimeID = intent.getSerializableExtra(EXTRA_CRIME_ID) as UUID

        crime_view_pager.adapter = (object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                val crime = crimes?.get(position)
                if (crime != null) {
                    return CrimeFragment.newInstance(crime.id)
                } else {
                    throw Exception("Error, crime was null")
                }
            }

            override fun getCount(): Int {
                return crimes?.size ?: 0
            }
        })

    }
}