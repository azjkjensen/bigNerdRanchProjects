package info.jkjensen.beatbox

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment

class BeatBoxActivity : SingleFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beat_box)
    }

    override fun createFragment(): Fragment {
        return BeatBoxFragment.newInstance()
    }
}
