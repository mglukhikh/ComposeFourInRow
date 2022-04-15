package part3.fourinrow.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import part3.fourinrow.core.*

private const val COLUMNS = 7

private const val ROWS = 6

private val WIN_YELLOW = Color(192, 128, 0)
private val WIN_RED = Color(192, 0, 0)

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun FrameWindowScope.App() {
    val board = Board(COLUMNS, ROWS)
    val colors = List(COLUMNS * ROWS) { remember { mutableStateOf(Color.Gray) } }

    val listener = BoardListener { turnCell ->
        val newWinningCombo = board.winningCombo()

        fun setColor(cell: Cell) {
            colors[(ROWS - 1 - cell.y) * COLUMNS + cell.x].value = when {
                newWinningCombo != null && cell in newWinningCombo -> {
                    if (newWinningCombo.winner == Chip.YELLOW) WIN_YELLOW else WIN_RED
                }
                else -> {
                    when (board[cell]) {
                        Chip.YELLOW -> Color.Yellow
                        Chip.RED -> Color.Red
                        null -> Color.Gray
                    }
                }
            }
        }
        val column = turnCell.x
        for (row in 0 until ROWS) {
            val cell = Cell(column, row)
            setColor(cell)
        }
        if (newWinningCombo != null) {
            var cell = newWinningCombo.startCell
            while (true) {
                setColor(cell)
                if (cell == newWinningCombo.endCell) break
                cell += newWinningCombo.direction
            }
        }
    }
    board.registerListener(listener)

    MenuBar {
        Menu("Actions") {
            Item("Restart", onClick = {
                board.clear()
                for (i in 0 until COLUMNS * ROWS) {
                    colors[i].value = Color.Gray
                }
            })
        }
    }

    MaterialTheme {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            cells = GridCells.Fixed(COLUMNS)
        ) {
            items(COLUMNS * ROWS) { index ->
                OutlinedButton(
                    shape = CircleShape,
                    colors = ButtonDefaults.outlinedButtonColors(backgroundColor = colors[index].value),
                    onClick = {
                        val column = index % COLUMNS
                        if (board.winner() == null) {
                            board.makeTurn(column)
                        }
                    }
                ) {
                    Text(when (colors[index].value) {
                        Color.Red, WIN_RED -> "X"
                        Color.Yellow, WIN_YELLOW -> "O"
                        else -> ""
                    })

                }
            }
        }
    }
}

fun main() = application {
    Window(
        title = "Four in Row",
        state = rememberWindowState(width = 350.dp, height = 400.dp),
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
