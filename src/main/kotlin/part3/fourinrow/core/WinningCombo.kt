package part3.fourinrow.core

class WinningCombo(val winner: Chip, val startCell: Cell, val endCell: Cell, val direction: Cell) {
    operator fun contains(cell: Cell): Boolean {
        var current = startCell
        while (true) {
            if (current == cell) return true
            if (current == endCell) return false
            current += direction
        }
    }
}