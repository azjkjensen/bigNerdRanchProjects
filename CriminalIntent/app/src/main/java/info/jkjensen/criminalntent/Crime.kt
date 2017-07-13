package info.jkjensen.criminalntent

import java.util.Date
import java.util.UUID

/**
 * Created by jk on 6/9/17.
 */

data class Crime(
        val id: UUID,
        var title: String,
        val date: Date,
        var solved: Boolean){
    constructor(): this(UUID.randomUUID(), String(), Date(), false)
}