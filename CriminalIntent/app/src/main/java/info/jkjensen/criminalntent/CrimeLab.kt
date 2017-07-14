package info.jkjensen.criminalntent

import android.content.Context
import java.util.*

/**
 * Created by jk on 6/12/17.
 */

class CrimeLab private constructor(context: Context) {
    companion object{
        var crimeLab: CrimeLab? = null
        public fun get(context: Context? = null):CrimeLab?{
            if(crimeLab == null && context != null){
                crimeLab = CrimeLab(context)
            }
            return crimeLab
        }
    }

    public var crimes: MutableList<Crime> = mutableListOf()

    init {
//        for (i in 0..100) {
//            var crime: Crime = Crime()
//            crime.title = "Crime #" + i
//            crime.solved = i%2 == 0
//            crimes.add(crime)
//        }
    }

    fun getCrimeByID(id: UUID): Crime?{
        for(crime: Crime in crimes){
            if(crime.id.equals(id)){
                return crime
            }
        }
        return null
    }

    fun addCrime(c:Crime){
        crimes.add(c)
    }

    fun removeCrimeById(id: UUID?) {
        var indexToRemove:Int? = null
        for((index, crime: Crime) in crimes.withIndex()){
            if(crime.id.equals(id)){
                indexToRemove = index
                break
            }
        }
        if(indexToRemove != null) {
            crimes.removeAt(indexToRemove)
        }
    }
}