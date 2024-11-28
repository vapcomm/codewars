package online.vapcom.codewars.puzzles

import kotlin.test.assertEquals
import kotlin.test.Test

class KingInCheckTest {
    /*
     * Additional tests
     */
    @Test
    fun aloneKingInCorners() {
        // check edge King's coordinates
        val boardA1 = arrayOf(
            arrayOf("♔", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(false, isCheck(boardA1))

        val boardH8 = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", "♔")
        )
        assertEquals(false, isCheck(boardH8))

        val boardA8 = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", "♔"),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(false, isCheck(boardA8))

        val boardH1 = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf("♔", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(false, isCheck(boardH1))
    }

    @Test
    fun pawns() {
        var board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♟", " ", "♟", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf("♟", " ", "♟", " ", " ", " ", " ", " "),
            arrayOf(" ", "♔", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", "♟", " ", "♟"),
            arrayOf(" ", " ", " ", " ", " ", " ", "♔", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♟", " ", " ", " ", " ", " ", " "),
            arrayOf("♔", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", "♟", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", "♔"),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))
    }

    @Test
    fun bishops() {
        // hidden by pawn
        var board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", "♟", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", "♝", " ", " ")
        )
        assertEquals(false, isCheck(board))

        // +1 +1
        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", "♝", " ", " ")
        )
        assertEquals(true, isCheck(board))

        // -1 +1
        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", "♝", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        // -1 -1
        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf("♝", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        // +1 -1
        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf("♝", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", "♝", " ", " ", " ", " ", " ", " "),
            arrayOf("♝", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))
    }

    @Test
    fun rooks() {
        // hidden by pawn
        var board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♔", "♟", " ", " ", "♜", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(false, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♔", " ", " ", " ", " ", " ", "♜"),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♜", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♔", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♔", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♜", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf("♜", "♔", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf("♔", "♜", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf("♜", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf("♔", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf("♔", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf("♜", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", "♔"),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", "♜")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", "♜"),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", "♔"),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf("♜", " ", " ", " ", " ", " ", " ", "♔"),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf("♜", " ", " ", " ", " ", " ", " ", "♔")
        )
        assertEquals(true, isCheck(board))
    }

    @Test
    fun knights() {
        var board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♞", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf("♞", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf("♞", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♞", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", "♞", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", "♞", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", "♞", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", "♞", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf("♔", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♞", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf("♔", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♞", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", "♞", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", "♔")
        )
        assertEquals(true, isCheck(board))

        board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", "♞", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", "♔")
        )
        assertEquals(true, isCheck(board))
    }


    /*
     * Kata example tests
     *
     *NOTE: in kata tests expected and actual values are swapped, I've corrected it.
     */
    @Test
    fun shouldWorkWithCheckByPawn() {
        val board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", "♟", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))
    }

    @Test
    fun shouldWorkWithPawnDirectlyAbove() {
        val board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♟", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(false, isCheck(board))
    }

    @Test
    fun shouldWorkWithCheckByBishop() {
        val board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", "♔", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", "♝", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))
    }

    @Test
    fun shouldWorkWithCheckByBishop2() {
        val board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", "♝"),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf("♔", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))
    }

    @Test
    fun shouldWorkWithCheckByRook() {
        val board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♔", " ", " ", " ", "♜", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))
    }

    @Test
    fun shouldWorkWithCheckByKnight() {
        val board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♔", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf("♞", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))
    }

    @Test
    fun shouldWorkWithCheckByQueen() {
        val board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♔", " ", " ", "♛", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(true, isCheck(board))
    }

    @Test
    fun shouldWorkWithKingAlone() {
        val board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♔", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(false, isCheck(board))
    }

    @Test
    fun shouldWorkWhenNoCheck() {
        val board = arrayOf(
            arrayOf("♛", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", "♞", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", "♛"),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♔", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(false, isCheck(board))
    }

    @Test
    fun shouldWorkWhenAPieceIsBlockingLineOfSight() {
        val board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", "♔", " ", "♞", "♛", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(false, isCheck(board))
    }

    @Test
    fun noCheck() {
        val board = arrayOf(
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf("♔", " ", " ", " ", " ", " ", " ", "♞"),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " "),
            arrayOf(" ", " ", " ", " ", " ", " ", " ", " ")
        )
        assertEquals(false, isCheck(board))
    }
}