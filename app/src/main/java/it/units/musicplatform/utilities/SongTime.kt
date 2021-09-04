package it.units.musicplatform.utilities

class SongTime {

    companion object {
        @JvmStatic
        fun format(numberOfSeconds: Int): String {
            val seconds = numberOfSeconds % 60
            val minutes = numberOfSeconds / 60
            return String.format("%02d:%02d", minutes, seconds)
        }
    }
}