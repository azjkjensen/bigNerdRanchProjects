package info.jkjensen.criminalntent

/**
 * Created by jk on 7/17/17.
 */
class CrimeDbSchema {
    companion object {
        class CrimeTable {
            companion object {
                val NAME: String = "crimes"

                class Cols {
                    companion object {
                        val UUID: String = "uuid";
                        val TITLE: String = "title";
                        val DATE: String = "date";
                        val SOLVED: String = "solved";
                    }

                }
            }
        }
    }
}