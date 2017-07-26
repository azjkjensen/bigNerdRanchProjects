package info.jkjensen.beatbox

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.view.menu.MenuView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_beat_box.*
import kotlinx.android.synthetic.main.list_item_sound.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BeatBoxFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BeatBoxFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BeatBoxFragment : Fragment() {
    companion object {
        fun newInstance(): BeatBoxFragment {
            return BeatBoxFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_beat_box, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = GridLayoutManager(activity, 3)
        recyclerView.adapter = SoundAdapter()
    }

    private inner class SoundHolder(itemView:View): RecyclerView.ViewHolder(itemView)
    private inner class SoundAdapter: RecyclerView.Adapter<SoundHolder>() {
        override fun getItemCount(): Int {
            return 0
        }

        override fun onBindViewHolder(holder: SoundHolder?, position: Int) {

        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SoundHolder {
            val view = LayoutInflater.from(activity).inflate(R.layout.list_item_sound, parent, false)
            return SoundHolder(view)
        }
    }
}
