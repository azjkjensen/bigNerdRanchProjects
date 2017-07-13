package info.jkjensen.criminalntent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_crime_list.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * Created by jk on 6/15/17.
 * Crime list.
 */
class CrimeListFragment: Fragment() {
    var adapter: CrimeAdapter? = null
    var selectedItem: Int? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater?.inflate(R.layout.fragment_crime_list, container, false)

        val crimeRecyclerView: RecyclerView = view?.findViewById(R.id.crimeRecyclerView) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(activity)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI(){
        val crimeLab: CrimeLab? = CrimeLab.get(activity)
        val crimes = crimeLab?.crimes

        if(adapter == null) {
            adapter = CrimeAdapter(crimes)
            crimeRecyclerView.adapter = adapter
        } else {
            if(selectedItem !== null){
                adapter?.notifyItemChanged(selectedItem as Int)
            }
        }
    }

    inner class CrimeAdapter(var crimes: MutableList<Crime>?): RecyclerView.Adapter<CrimeHolder>() {
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
            selectedItem = adapterPosition
            startActivity<CrimePagerActivity>(CrimePagerActivity.EXTRA_CRIME_ID to (crime?.id ?: 0))
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