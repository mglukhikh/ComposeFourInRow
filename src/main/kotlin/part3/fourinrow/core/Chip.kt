package part3.fourinrow.core

enum class Chip {
    YELLOW, RED;

    fun opposite(): Chip {
        return if (this == YELLOW) RED else YELLOW
    }
}