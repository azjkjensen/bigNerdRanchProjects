package info.jkjensen.criminalntent

import java.util.Date
import java.util.UUID

/**
 * Created by jk on 6/9/17.
 */

data class Crime(
        var _id: Int?,
        val id: UUID,
        var title: String,
        var suspect: String,
        var date: Date,
        var solved: Boolean){
//    constructor(id, title, date, solved)
    constructor(): this(null, UUID.randomUUID(), String(), String(), Date(), false)
}