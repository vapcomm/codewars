package online.vapcom.codewars.algorithms


/**
 * #28 Plants and Zombies - 3 kyu
 *
 * https://www.codewars.com/kata/5a5db0f580eba84589000979
 *
 * @param zombies [i,row,hp] - where i is the move number (0-based) when it appears,
 * row is the row the zombie walks down, and hp is the initial health point value of the zombie.
 */
fun plantsAndZombies(lawn: Array<String>, zombies: Array<IntArray>): Int? {

    class BattleField(lawn: Array<String>) {
        private abstract inner class Creature
        private open inner class Shooter(val row: Int, val column: Int) : Creature()

        private inner class NumShooter(row: Int, column: Int, val power: Int) : Shooter(row, column) {
            override fun toString(): String = "N$power"
        }

        private inner class SuperShooter(row: Int, column: Int) : Shooter(row, column) {
            override fun toString(): String = "SS"
        }

        private inner class Zombie(var hp: Int) : Creature() {
            fun takeHit() {
                if (hp > 0) hp--
            }

            fun isDead(): Boolean = hp <= 0

            override fun toString(): String = "$hp".padStart(2, ' ')
        }

        private inner class Empty : Creature() {
            override fun toString(): String = "  "
        }

        val field: Array<Array<Creature>> = Array(lawn.size) { Array(lawn[0].length) { Empty() } }
        val numShooters = mutableListOf<NumShooter>()
        val superShooters = mutableListOf<SuperShooter>()

        init {
            lawn.forEachIndexed { i, str ->
                str.forEachIndexed { j, c ->
                    when (c) {
                        'S' -> {
                            val ss = SuperShooter(i, j)
                            field[i][j] = ss
                            superShooters.add(ss)
                        }

                        in '0'..'9' -> {
                            val ns = NumShooter(i, j, c.digitToInt())
                            field[i][j] = ns
                            numShooters.add(ns)
                        }
                    }
                }
            }

            //DOC : S-shooters fire their shots in order from right to left, then top to bottom
            // so sort them out
            superShooters.sortWith { s1, s2 ->
                if (s1.column == s2.column) {
                    s1.row.compareTo(s2.row)
                } else {
                    -(s1.column.compareTo(s2.column))
                }
            }
        }

        val maxColumn = field[0].lastIndex
        val maxRow = field.lastIndex

        override fun toString(): String {
            val sb = StringBuilder()

            sb.append("   ")
            repeat(field[0].size) { sb.append(" "); sb.append(it % 100) }
            sb.append("\n")

            field.forEachIndexed { index, creatures ->
                sb.append(index)
                sb.append(":'")
                sb.append(creatures.joinToString("") { it.toString() })
                sb.append("'\n")
            }

            return sb.toString()
        }

        fun addZombies(newZombies: List<IntArray>) {
            println("add zombies: ${newZombies.size}")
            val zombieStart = field[0].lastIndex
            newZombies.forEach { zombie -> // [i, row, hp]
                field[zombie[1]][zombieStart] = Zombie(zombie[2])
            }
        }

        fun moveZombies() {
            field.forEachIndexed { index, row ->
                for (i in 1..row.lastIndex) {
                    if (row[i] is Zombie) {
                        if (row[i - 1] is Shooter) {
                            eliminateShooter(index, i - 1)
                        }
                        row[i - 1] = row[i]
                        row[i] = Empty()
                    }
                }
            }
        }

        private fun eliminateShooter(row: Int, column: Int) {
            println("shooter [$row,${column}] ELIMINATED")

            if (!numShooters.removeIf { it.row == row && it.column == column }) {
                superShooters.removeIf { it.row == row && it.column == column }
            }
        }

        fun shoot() {
            //DOC: The numbered shooters fire all their shots in a cluster
            // shoot straight (to the right) a given number of times per move.
            numShooters.forEach { shooter ->
                println("num shooter[${shooter.row},${shooter.column}] fire ${shooter.power} times from ${shooter.column + 1} to $maxColumn")
                repeat(shooter.power) {
                    for (i in (shooter.column + 1)..maxColumn) {
                        if(fireTo(shooter.row, i))
                            break
                    }
                }
            }

            //DOC: S-shooters shoot straight, and diagonally upward and downward (ie. three directions simultaneously) once per move.
            superShooters.forEach { ss ->
                println("super shooter[${ss.row},${ss.column}]")
                fireLine(ss.row, ss.column, -1) // up diagonal
                fireLine(ss.row, ss.column, 0)  // horizontal
                fireLine(ss.row, ss.column, 1)  // down diagonal
            }
        } // shoot()

        private fun fireLine(shooterRow: Int, shooterColumn: Int, rowDelta: Int) {
            var row = shooterRow + rowDelta
            var column = shooterColumn + 1
            while (row in 0..maxRow && column <= maxColumn) {
                if(fireTo(row, column))
                    break
                row += rowDelta
                column++
            }
        }

        /**
         * Try to hit zombie, if hit something, return true
         */
        private fun fireTo(row: Int, column: Int): Boolean {
            val creature = field[row][column]
            if (creature is Zombie) {
                creature.takeHit()
                println("zombie [$row,$column] hit, hp left: ${creature.hp}")

                if (creature.isDead()) {
                    //DOC: once a zombie's health reaches 0 it drops immediately and does not absorb any additional shooter pellets.
                    println("zombie [$row,$column] DEAD")
                    field[row][column] = Empty()
                }
                return true
            }

            return false
        }

        fun zombiesWon(): Boolean = field.find { it[0] is Zombie } != null
        fun shootersWon(): Boolean = numberOfZombies() <= 0
        fun numberOfZombies(): Int = field.sumOf { row -> row.sumOf { if (it is Zombie) 1.toInt() else 0 } }
    }

    println("lawn: '${lawn.joinToString { "\"$it\"" }}'")
    val zb = zombies.joinToString { "intArrayOf(${it[0]}, ${it[1]}, ${it[2]})" }
    println("zombies: '$zb'")

    printLawn(lawn)

    val bf = BattleField(lawn)

    println("Start field:")
    println(bf)

    val maxZombiesStep: Int = zombies.maxByOrNull { it[0] }?.get(0) ?: 0
    var step = 0
    do {
        println("--------- Step: $step ---------")
        bf.moveZombies()
        bf.addZombies(zombies.filter { it[0] == step })
        println("----- Moved/Added zombies -----")
        print(bf)
        bf.shoot()

        step++
        print(bf)
        println("num shooters: ${bf.numShooters}, super: ${bf.superShooters}")
        println("zombies won: ${bf.zombiesWon()}, shooters won: ${bf.shootersWon()}")
    } while (!bf.zombiesWon() && (step <= maxZombiesStep || !bf.shootersWon()))

    return if (bf.zombiesWon()) step else null
}

fun printLawn(lawn: Array<String>) {
    val width = lawn[0].length
    print("   ")
    repeat(width) { print(it % 10) }
    println()

    lawn.forEachIndexed { index, str ->
        println("$index:'$str'")
    }
}

// version for codewars without debug logs
fun plantsAndZombiesNoLogs(lawn: Array<String>, zombies: Array<IntArray>): Int? {

    class BattleField(lawn: Array<String>) {
        private abstract inner class Creature
        private open inner class Shooter(val row: Int, val column: Int) : Creature()
        private inner class NumShooter(row: Int, column: Int, val power: Int) : Shooter(row, column)
        private inner class SuperShooter(row: Int, column: Int) : Shooter(row, column)

        private inner class Zombie(var hp: Int) : Creature() {
            fun takeHit() {
                if (hp > 0) hp--
            }
            fun isDead(): Boolean = hp <= 0
        }

        private inner class Empty : Creature()

        val field: Array<Array<Creature>> = Array(lawn.size) { Array(lawn[0].length) { Empty() } }
        val numShooters = mutableListOf<NumShooter>()
        val superShooters = mutableListOf<SuperShooter>()

        init {
            lawn.forEachIndexed { i, str ->
                str.forEachIndexed { j, c ->
                    when (c) {
                        'S' -> {
                            val ss = SuperShooter(i, j)
                            field[i][j] = ss
                            superShooters.add(ss)
                        }

                        in '0'..'9' -> {
                            val ns = NumShooter(i, j, c.digitToInt())
                            field[i][j] = ns
                            numShooters.add(ns)
                        }
                    }
                }
            }

            //DOC : S-shooters fire their shots in order from right to left, then top to bottom
            // so sort them out
            superShooters.sortWith { s1, s2 ->
                if (s1.column == s2.column) {
                    s1.row.compareTo(s2.row)
                } else {
                    -(s1.column.compareTo(s2.column))
                }
            }
        }

        val maxColumn = field[0].lastIndex
        val maxRow = field.lastIndex

        fun addZombies(newZombies: List<IntArray>) {
            val zombieStart = field[0].lastIndex
            newZombies.forEach { zombie -> // [i, row, hp]
                field[zombie[1]][zombieStart] = Zombie(zombie[2])
            }
        }

        fun moveZombies() {
            field.forEachIndexed { index, row ->
                for (i in 1..row.lastIndex) {
                    if (row[i] is Zombie) {
                        if (row[i - 1] is Shooter) {
                            eliminateShooter(index, i - 1)
                        }
                        row[i - 1] = row[i]
                        row[i] = Empty()
                    }
                }
            }
        }

        private fun eliminateShooter(row: Int, column: Int) {
            if (!numShooters.removeIf { it.row == row && it.column == column }) {
                superShooters.removeIf { it.row == row && it.column == column }
            }
        }

        fun shoot() {
            //DOC: The numbered shooters fire all their shots in a cluster
            // shoot straight (to the right) a given number of times per move.
            numShooters.forEach { shooter ->
                repeat(shooter.power) {
                    for (i in (shooter.column + 1)..maxColumn) {
                        if(fireTo(shooter.row, i))
                            break
                    }
                }
            }

            //DOC: S-shooters shoot straight, and diagonally upward and downward (ie. three directions simultaneously) once per move.
            superShooters.forEach { ss ->
                fireLine(ss.row, ss.column, -1) // up diagonal
                fireLine(ss.row, ss.column, 0)  // horizontal
                fireLine(ss.row, ss.column, 1)  // down diagonal
            }
        } // shoot()

        private fun fireLine(shooterRow: Int, shooterColumn: Int, rowDelta: Int) {
            var row = shooterRow + rowDelta
            var column = shooterColumn + 1
            while (row in 0..maxRow && column <= maxColumn) {
                if(fireTo(row, column))
                    break
                row += rowDelta
                column++
            }
        }

        /**
         * Try to hit zombie, if hit something, return true
         */
        private fun fireTo(row: Int, column: Int): Boolean {
            val creature = field[row][column]
            if (creature is Zombie) {
                creature.takeHit()
                if (creature.isDead()) {
                    //DOC: once a zombie's health reaches 0 it drops immediately and does not absorb any additional shooter pellets.
                    field[row][column] = Empty()
                }
                return true
            }

            return false
        }

        fun zombiesWon(): Boolean = field.find { it[0] is Zombie } != null
        fun shootersWon(): Boolean = numberOfZombies() <= 0
        fun numberOfZombies(): Int = field.sumOf { row -> row.sumOf { if (it is Zombie) 1.toInt() else 0 } }
    }

    val bf = BattleField(lawn)
    val maxZombiesStep: Int = zombies.maxByOrNull { it[0] }?.get(0) ?: 0
    var step = 0
    do {
        bf.moveZombies()
        bf.addZombies(zombies.filter { it[0] == step })
        bf.shoot()
        step++
    } while (!bf.zombiesWon() && (step <= maxZombiesStep || !bf.shootersWon()))

    return if (bf.zombiesWon()) step else null
}
