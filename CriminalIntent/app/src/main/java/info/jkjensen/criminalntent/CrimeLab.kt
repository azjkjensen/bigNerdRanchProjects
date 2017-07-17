package info.jkjensen.criminalntent

import android.content.Context
import android.util.Log
import org.jetbrains.anko.db.*
import java.util.*

/**
 * Created by jk on 6/12/17.
 */


class CrimeLab private constructor(context: Context) {
    companion object{
        var crimeLab: CrimeLab? = null
        val rowParser = CrimeRowParser()
        public fun get(context: Context? = null):CrimeLab?{
            if(crimeLab == null && context != null){
                crimeLab = CrimeLab(context)
            }
            return crimeLab
        }
    }

    private var context:Context? = null

    init {
        this.context = context
        context.database.use{
            select("crime")
                    .whereArgs("(title = {title}", "title" to "s")
        }
    }

    fun getCrimes():List<Crime>?{
        return context!!.database.use {
            select("crime")
                    .exec {
                        parseList(rowParser)
                    }
        }
//        return listOf()
    }

    fun getCrimeByID(id: UUID): Crime?{
        return context!!.database.use {
            select("crime")
                    .whereArgs("UUID = {id}", "id" to id.toString())
                    .exec {
                        parseSingle(rowParser)
                    }
        }
    }

    fun addCrime(c:Crime){

        context!!.database.use {
            insert("crime",
//                    "_id" to 1,
                    "UUID" to c.id.toString(),
                    "title" to c.title,
                    "date" to c.date.time,
                    "solved" to if(c.solved) 1 else 0
                    )
        }
    }

    fun updateCrime(c: Crime){
        context!!.database.use {
            update("crime",
                    "UUID" to c.id.toString(),
                    "title" to c.title,
                    "date" to c.date.time,
                    "solved" to if(c.solved) 1 else 0
                    )
                    .whereSimple("UUID = ?", c.id.toString())
                    .exec()
        }
    }

    fun removeCrimeById(id: UUID?) {
        if(id != null) {
            context!!.database.use {
                delete("crime", "UUID = ?", arrayOf(id.toString()))
            }
        }
    }

    class CrimeRowParser : RowParser<Crime> {
        override fun parseRow(columns: Array<Any?>): Crime {
//            return Crime((columns[0].toString().toInt()), UUID.fromString(columns[1].toString()), columns[2].toString(), Date(columns[3].toString()), columns[4] as Boolean)
            return Crime((columns[0].toString().toInt()),
                    UUID.fromString(columns[1].toString()),
                    columns[2].toString(),
                    Date(columns[3].toString().toLong()),
                    columns[4].toString().toInt() == 1)

        }
    }
}
