package part3.fourinrow.core

data class Cell(val x: Int, val y: Int) {
    operator fun plus(other: Cell): Cell {
        return Cell(x + other.x, y + other.y)
    }

    operator fun times(other: Int): Cell {
        return Cell(x * other, y * other)
    }

    override fun toString(): String {
        return "$x:$y"
    }
}