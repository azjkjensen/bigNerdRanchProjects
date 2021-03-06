package info.jkjensen.criminalntent

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_crime_list.*
import org.jetbrains.anko.support.v4.startActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log


/**
 * Created by jk on 6/15/17.
 * Crime list.
 */
class CrimeListFragment: Fragment() {
    var adapter: CrimeAdapter? = null
    var selectedItem: Int? = null
    var subtitleVisible = false
    private var callbacks:Callbacks? = null

    companion object {
        private val SAVED_SUBTITLE_VISIBLE = "subtitle"
    }

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onCrimeSelected(crime: Crime)
        fun onCrimeSwiped(crime: Crime)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater?.inflate(R.layout.fragment_crime_list, container, false)

        val crimeRecyclerView: RecyclerView = view?.findViewById(R.id.crimeRecyclerView) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(activity)
        if (savedInstanceState != null) {
            subtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()


        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.e("CLF", "Did it!")
                callbacks!!.onCrimeSwiped((viewHolder as CrimeHolder).crime!!)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(crimeRecyclerView)
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_crime, menu)
        val subtitleItem = menu?.findItem(R.id.show_subtitle)
        if(subtitleVisible){
            subtitleItem?.setTitle(R.string.hide_subtitle)
        } else{
            subtitleItem?.setTitle(R.string.show_subtitle)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.new_crime -> {
                val crime = Crime()
                CrimeLab.get(activity)?.addCrime(crime)
                updateUI()
                callbacks!!.onCrimeSelected(crime)
                return true
            }
            R.id.show_subtitle ->{
                subtitleVisible = !subtitleVisible
                activity.invalidateOptionsMenu()
                updateSubtitle()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putBoolean(SAVED_SUBTITLE_VISIBLE, subtitleVisible)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    fun updateUI(){
        val crimeLab: CrimeLab? = CrimeLab.get(activity)
        val crimes = crimeLab?.getCrimes()

        if(crimes?.size == 0){
            crimeRecyclerView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            noContentTextView.visibility = View.VISIBLE
        } else{
            crimeRecyclerView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            noContentTextView.visibility = View.GONE
        }

        if(adapter == null) {
            adapter = CrimeAdapter(crimes)
            crimeRecyclerView.adapter = adapter
        } else {
            adapter!!.crimes = crimes
            adapter?.notifyDataSetChanged()
//            if(selectedItem !== null){
//                adapter?.notifyItemChanged(selectedItem as Int)
//            }
        }
        updateSubtitle()
    }

    private fun updateSubtitle(){
        val crimeLab = CrimeLab.get(activity)
        val crimeCount = crimeLab?.getCrimes()?.size
        var subtitle = resources.getQuantityString(R.plurals.subtitle_plural, crimeCount ?: 0)

        if(!subtitleVisible){
            subtitle = null
        }

        val activity = activity as AppCompatActivity
        activity.supportActionBar!!.subtitle = subtitle

    }

    inner class CrimeAdapter(var crimes: List<Crime>?): RecyclerView.Adapter<CrimeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CrimeHolder {
            val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
            val view: View = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }

        override fun getItemCount(): Int {
            return crimes!!.size
        }

        override fun onBindViewHolder(holder: CrimeHolder?, position: Int) {
            val crime: Crime? = crimes?.get(position)
            holder?.bindCrime(crime)
        }
    }

    inner class CrimeHolder(itemView: View?) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var crime: Crime? = null
        var titleTextView: TextView = itemView?.findViewById(R.id.listItemCrimeTitleTextView) as TextView
        var dateTextView: TextView = itemView?.findViewById(R.id.listItemCrimeDateTextView) as TextView
//        var solvedCheckBox: CheckBox = itemView?.findViewById(R.id.listItemCrimeSolvedCheckBox) as CheckBox
        var solvedImageView: ImageView = itemView?.findViewById(R.id.solvedImageView) as ImageView

        init{
            itemView?.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
//            toast(crime?.title + " clicked!")
//            selectedItem = adapterPosition
//            startActivity<CrimePagerActivity>(CrimePagerActivity.EXTRA_CRIME_ID to (crime?.id ?: 0))

            callbacks!!.onCrimeSelected(crime!!)
        }


        fun bindCrime(crimeIn: Crime?){
            crime = crimeIn
            titleTextView.text = crime?.title
            dateTextView.text = crime?.date.toString()
//            solvedCheckBox.isChecked = crime?.solved ?: false
            solvedImageView.visibility = if (crime?.solved ?: false) View.VISIBLE else View.GONE
        }
    }
}