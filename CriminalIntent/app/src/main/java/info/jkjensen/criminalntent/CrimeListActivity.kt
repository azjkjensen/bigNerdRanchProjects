package info.jkjensen.criminalntent

import android.support.v4.app.Fragment

/**
 * Created by jk on 6/15/17.
 */

class CrimeListActivity : SingleFragmentActivity(){
    override fun createFragment(): Fragment {
        return CrimeListFragment()
    }
}