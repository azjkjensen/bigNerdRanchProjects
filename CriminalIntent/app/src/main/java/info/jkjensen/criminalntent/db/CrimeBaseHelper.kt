package info.jkjensen.criminalntent.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Created by jk on 7/14/17.
 */
class CrimeBaseHelper(context: Context): ManagedSQLiteOpenHelper(context, "crimebase.db"){
    companion object {
        private val VERSION = 1
        private val DATABASE_NAME = "crimeBase.db"
        private var instance: CrimeBaseHelper? = null

        @Synchronized
        fun getInstance(context:Context): CrimeBaseHelper {
            if(instance == null){
                instance = CrimeBaseHelper(context.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.createTable("crime", true,
                "_id" to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                "UUID" to TEXT,
                "title" to TEXT,
                "suspect" to TEXT,
                "date" to TEXT,
                "solved" to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}

// Access property for Context
val Context.database: CrimeBaseHelper
    get() = CrimeBaseHelper.getInstance(applicationContext)
