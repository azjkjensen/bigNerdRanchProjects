package info.jkjensen.criminalntent

import android.support.v4.app.Fragment
import android.content.ClipData.newIntent
import android.content.Intent
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity



/**
 * Created by jk on 6/15/17.
 */

class CrimeListActivity : SingleFragmentActivity(), CrimeListFragment.Callbacks{

    override fun createFragment(): Fragment {
        return CrimeListFragment()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_masterdetail
    }

    override fun onCrimeSelected(crime: Crime) {

        if (findViewById(R.id.detailFragmentContainer) == null) {
            startActivity<CrimePagerActivity>(CrimePagerActivity.EXTRA_CRIME_ID to crime.id)
        } else {
            val newDetail = CrimeFragment.newInstance(crime.id)

            supportFragmentManager.beginTransaction()
                    .replace(R.id.detailFragmentContainer, newDetail)
                    .commit()
        }
    }
}