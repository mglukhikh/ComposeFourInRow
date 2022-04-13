package part3.fourinrow.core

class Board constructor(val width: Int = 7, val height: Int = 6) {
    private val chips: MutableMap<Cell, Chip> = HashMap()
    var turn = Chip.YELLOW
        private set
    private var listener: BoardListener? = null

    fun clear() {
        chips.clear()
        turn = Chip.YELLOW
    }

    fun registerListener(listener: BoardListener) {
        this.listener = listener
    }

    operator fun get(x: Int, y: Int): Chip? {
        return get(Cell(x, y))
    }

    operator fun get(cell: Cell): Chip? {
        return chips[cell]
    }

    fun makeTurn(x: Int): Cell? {
        return makeTurn(x, true)
    }

    fun makeTurnNoEvent(x: Int): Cell? {
        return makeTurn(x, false)
    }

    private fun makeTurn(x: Int, withEvent: Boolean): Cell? {
        if (x < 0 || x >= width) return null
        for (y in 0 until height) {
            val cell = Cell(x, y)
            if (!chips.containsKey(cell)) {
                chips[cell] = turn
                turn = turn.opposite()
                if (listener != null && withEvent) {
                    listener!!.turnMade(cell)
                }
                return cell
            }
        }
        return null
    }

    fun hasFreeCells(): Boolean {
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (get(x, y) == null) return true
            }
        }
        return false
    }

    fun correct(cell: Cell): Boolean {
        return cell.x in 0 until width && cell.y >= 0 && cell.y < height
    }

    fun winner(): Chip? {
        val combo = winningCombo() ?: return null
        return combo.winner
    }

    fun winningCombo(): WinningCombo? {
        for (x in 0 until width) {
            for (y in 0 until height) {
                val cell = Cell(x, y)
                val startChip = chips[cell] ?: continue
                // Vector-style
                for (dir in DIRECTIONS) {
                    var current = cell
                    var length = 1
                    while (length < TO_WIN_LENGTH) {
                        current = current.plus(dir)
                        if (get(current) != startChip) break
                        length++
                    }
                    if (length == TO_WIN_LENGTH) return WinningCombo(startChip, cell, current, dir)
                }
                // Straight-forward style
//                int i;
//                for (i = 1; i < TO_WIN_LENGTH; i++) {
//                    if (get(x + i, y) != startChip) break;
//                }
//                if (i == TO_WIN_LENGTH) return startChip;
//                for (i = 1; i < TO_WIN_LENGTH; i++) {
//                    if (get(x, y + i) != startChip) break;
//                }
//                if (i == TO_WIN_LENGTH) return startChip;
//                for (i = 1; i < TO_WIN_LENGTH; i++) {
//                    if (get(x + i, y + i) != startChip) break;
//                }
//                if (i == TO_WIN_LENGTH) return startChip;
//                for (i = 1; i < TO_WIN_LENGTH; i++) {
//                    if (get(x + i, y - i) != startChip) break;
//                }
//                if (i == TO_WIN_LENGTH) return startChip;
            }
        }
        return null
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (y in height - 1 downTo 0) {
            for (x in 0 until width) {
                val chip = get(x, y)
                if (chip == null) {
                    sb.append("- ")
                    continue
                }
                when (chip) {
                    Chip.YELLOW -> sb.append("Y ")
                    Chip.RED -> sb.append("r ")
                }
            }
            sb.append("\n")
        }
        return sb.toString()
    }

    fun takeTurnBack(x: Int) {
        for (y in height - 1 downTo 0) {
            val cell = Cell(x, y)
            val chip = get(cell)
            if (chip != null) {
                chips.remove(cell)
                turn = turn.opposite()
                return
            }
        }
    }

    companion object {
        const val TO_WIN_LENGTH = 4
        private val DIRECTIONS = arrayOf(
            Cell(0, 1), Cell(1, 0),
            Cell(1, 1), Cell(1, -1)
        )
    }
}