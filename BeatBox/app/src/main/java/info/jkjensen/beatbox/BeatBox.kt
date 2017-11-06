package info.jkjensen.beatbox

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import java.io.IOException

/**
 * Created by jk on 7/27/17.
 */
class BeatBox(context: Context) {
    companion object {
        val SOUNDS_FOLDER = "sample_sounds"
    }

    val assets: AssetManager? = context.assets

    init {
        loadSounds()
    }

    private fun loadSounds(){
        val soundNames: Array<String>
        try{
            soundNames = assets!!.list(SOUNDS_FOLDER)
            Log.i(BeatBox.javaClass.simpleName, "Found " + soundNames.size + " assets.")
        }catch (ioe: IOException){
            Log.e(BeatBox.javaClass.simpleName, "Could not list assets.", ioe)
            return
        }
    }
}