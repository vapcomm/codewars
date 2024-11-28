package online.vapcom.codewars.puzzles

/**
 * #30 One line task: Is the King in check? - 3 kyu
 *
 * https://www.codewars.com/kata/5e320fe3358578001e04ad55
 *
 */
fun isCheck(b: Array<Array<String>>)=Array(1){Array(24){i->IntArray(8)+IntArray(8){j->if(i in 8..15&&b[i-8][j]!=" ")9824-b[i-8][j][0].code else 0}+IntArray(8)}}.any{B->
// 131

/*
    0x265B	9819	BLACK CHESS QUEEN	♛ - 5
    0x265C	9820	BLACK CHESS ROOK	♜ - 4
    0x265D	9821	BLACK CHESS BISHOP	♝ - 3
    0x265E	9822	BLACK CHESS KNIGHT	♞ - 2
    0x265F	9823	BLACK CHESS PAWN	♟ - 1

 */

        for(r in 8..15)for(c in 8..15)if(B[r][c] == 12){    // 48
            println("King at ${r-8},${c-8}")
            // pawns
            if(B[r-1][c-1]==1||B[r-1][c+1]==1)return true   // 45

            // knights
//            -2 to 1,
//            -2 to -1,
//
//            -1 to 2
//            -1 to -2,
//
//            1 to 2,
//            1 to -2,
//
//            2 to 1,
//            2 to -1,


            arrayOf(-2,-2,-1,-1,1,1,2,2,1,-1,2,-2,2,-2,1,-1)
            if(arrayOf(1 to 2,2 to 1,2 to -1,1 to -2,-1 to -2,-2 to -1,-2 to 1,-1 to 2).any{B[r+it.first][c+it.second]==2})return true

        }
        false

}


fun isCheckBig(b: Array<Array<String>>): Boolean {
    //??? for(r in 0..7)
    b.forEachIndexed { r, row ->
        row.forEachIndexed { c, f ->
            if (f == "♔") {
                println("King at $r,$c")
                // pawns
                // | p      p p     p |
                // |K        K       K|
                if(r>0&&((c>0 && b[r-1][c-1]=="♟")||(c<7 && b[r-1][c+1]=="♟"))) return true

                // bishop|queen
                // | b      b b    b |
                // |K        K      K|
                // | b      b b    b |
                for (i in 1..7) {
                    if(r+i in 0..7 && c+i in 0..7) {    // +1 +1
                        when(b[r+i][c+i]){
                            "♝","♛"->return true
                            "♟","♜","♞"->break
                        }
                    }
                }

                for (i in 1..7) {
                    if(r-i in 0..7 && c+i in 0..7) {    // -1 +1
                        when(b[r-i][c+i]){
                            "♝","♛"->return true
                            "♟","♜","♞"->break
                        }
                    }
                }

                for (i in 1..7) {
                    if(r-i in 0..7 && c-i in 0..7) {    // -1 -1
                        when(b[r-i][c-i]){
                            "♝","♛"->return true
                            "♟","♜","♞"->break
                        }
                    }
                }

                for (i in 1..7) {
                    if(r+i in 0..7 && c-i in 0..7) {    // +1 -1
                        when(b[r+i][c-i]){
                            "♝","♛"->return true
                            "♟","♜","♞"->break
                        }
                    }
                }

                // rook|queen
                // |r       r     r|
                // |K   r   K  r  K|
                // |r       r     r|
                for (j in c+1..7) {
                    when(b[r][j]){
                        "♜","♛"->return true
                        "♟","♝","♞"->break
                    }
                }
                for (j in c-1 downTo 0) {
                    when(b[r][j]){
                        "♜","♛"->return true
                        "♟","♝","♞"->break
                    }
                }

                for (i in r+1..7) {
                    when(b[i][c]){
                        "♜","♛"->return true
                        "♟","♝","♞"->break
                    }
                }
                for (i in r-1 downTo 0) {
                    when(b[i][c]){
                        "♜","♛"->return true
                        "♟","♝","♞"->break
                    }
                }

                // knight
                // 01234567
                // 0  k k
                // 1 k   k
                // 2   K
                // 3 k   k
                // 4  k k
                if(arrayOf(1 to 2,2 to 1,2 to -1,1 to -2,-1 to -2,-2 to -1,-2 to 1,-1 to 2).any{p->r+p.first in 0..7&&c+p.second in 0..7&&b[r+p.first][c+p.second]=="♞"})return true
            }
        }
    }

    return false
}

fun isCheckMiddle(b: Array<Array<String>>)=b.any {
    for (r in 0..7)
        for (c in 0..7)
            if (b[r][c]=="♔") {
                if(r>0&&((c>0&&b[r-1][c-1]=="♟")||(c<7&&b[r-1][c+1]=="♟"))) return true
                else
                    // +1 +1
//                    if((1..7).toList().sumOf { i ->
                    if(Array(7){it+1}.sumOf { i ->
                            if(r+i in 0..7&&c+i in 0..7){when(b[r+i][c+i]){"♝","♛"->1 "♟","♜","♞"->-1 else->0}shl 8-i}else 0
                        } > 0) return true

/*
                for (i in 1..7) {
                    if(r+i in 0..7 && c+i in 0..7) {    // +1 +1
                        when(b[r+i][c+i]){
                            "♝","♛"->return true
                            "♟","♜","♞"->break
                        }
                    }
                }
*/
                for (i in 1..7) {
                    if(r-i in 0..7 && c+i in 0..7) {    // -1 +1
                        when(b[r-i][c+i]){
                            "♝","♛"->return true
                            "♟","♜","♞"->break
                        }
                    }
                }

                for (i in 1..7) {
                    if(r-i in 0..7 && c-i in 0..7) {    // -1 -1
                        when(b[r-i][c-i]){
                            "♝","♛"->return true
                            "♟","♜","♞"->break
                        }
                    }
                }

                for (i in 1..7) {
                    if(r+i in 0..7 && c-i in 0..7) {    // +1 -1
                        when(b[r+i][c-i]){
                            "♝","♛"->return true
                            "♟","♜","♞"->break
                        }
                    }
                }
            }
    return false
}


/*

    b.forEachIndexed { r, row ->
        row.forEachIndexed { c, f ->
            if (f == "♔") {
                println("King at $r,$c")
                // pawns
                // | p      p p     p |
                // |K        K       K|
                if(r>0&&((c>0 && b[r-1][c-1]=="♟")||(c<7 && b[r-1][c+1]=="♟"))) return true

                // bishop|queen
                // | b      b b    b |
                // |K        K      K|
                // | b      b b    b |
                for (i in 1..7) {
                    if(r+i in 0..7 && c+i in 0..7) {    // +1 +1
                        when(b[r+i][c+i]){
                            "♝","♛"->return true
                            "♟","♜","♞"->break
                        }
                    }
                }

                for (i in 1..7) {
                    if(r-i in 0..7 && c+i in 0..7) {    // -1 +1
                        when(b[r-i][c+i]){
                            "♝","♛"->return true
                            "♟","♜","♞"->break
                        }
                    }
                }

                for (i in 1..7) {
                    if(r-i in 0..7 && c-i in 0..7) {    // -1 -1
                        when(b[r-i][c-i]){
                            "♝","♛"->return true
                            "♟","♜","♞"->break
                        }
                    }
                }

                for (i in 1..7) {
                    if(r+i in 0..7 && c-i in 0..7) {    // +1 -1
                        when(b[r+i][c-i]){
                            "♝","♛"->return true
                            "♟","♜","♞"->break
                        }
                    }
                }

                // rook|queen
                // |r       r     r|
                // |K   r   K  r  K|
                // |r       r     r|
                for (j in c+1..7) {
                    when(b[r][j]){
                        "♜","♛"->return true
                        "♟","♝","♞"->break
                    }
                }
                for (j in c-1 downTo 0) {
                    when(b[r][j]){
                        "♜","♛"->return true
                        "♟","♝","♞"->break
                    }
                }

                for (i in r+1..7) {
                    when(b[i][c]){
                        "♜","♛"->return true
                        "♟","♝","♞"->break
                    }
                }
                for (i in r-1 downTo 0) {
                    when(b[i][c]){
                        "♜","♛"->return true
                        "♟","♝","♞"->break
                    }
                }

                // knight
                // 01234567
                // 0  k k
                // 1 k   k
                // 2   K
                // 3 k   k
                // 4  k k
                if(arrayOf(1 to 2,2 to 1,2 to -1,1 to -2,-1 to -2,-2 to -1,-2 to 1,-1 to 2).any{p->r+p.first in 0..7&&c+p.second in 0..7&&b[r+p.first][c+p.second]=="♞"})return true
            }
        }
    }

    return false
}


 */