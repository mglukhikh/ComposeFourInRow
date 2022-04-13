package part3.fourinrow.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import part3.fourinrow.core.Board
import part3.fourinrow.core.BoardListener
import part3.fourinrow.core.Cell
import part3.fourinrow.core.Chip

private const val COLUMNS = 7

private const val ROWS = 6

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {
    val board = Board(COLUMNS, ROWS)
    val colors = List(COLUMNS * ROWS) { remember { mutableStateOf(Color.Gray) } }
    val listener = BoardListener { cell ->
        val column = cell.x
        for (row in 0 until ROWS) {
            colors[(ROWS - 1 - row) * COLUMNS + column].value = when (board[Cell(column, row)]) {
                Chip.YELLOW -> Color.Yellow
                Chip.RED -> Color.Red
                null -> Color.Gray
            }
        }
    }
    board.registerListener(listener)

    MaterialTheme {
        LazyVerticalGrid(
            cells = GridCells.Fixed(COLUMNS)
        ) {
            items(COLUMNS * ROWS) { index ->
                OutlinedButton(
                    shape = CircleShape,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colors[index].value),
                    onClick = {
                        val column = index % COLUMNS
                        board.makeTurn(column)
                    }
                ) {
                    Text(when (colors[index].value) {
                        Color.Red -> "XXX"
                        Color.Yellow -> "YYY"
                        else -> ""
                    })

                }
            }
        }
    }
}

fun main() = application {
    Window(title = "Four in Row", onCloseRequest = ::exitApplication) {
        App()
    }
}
