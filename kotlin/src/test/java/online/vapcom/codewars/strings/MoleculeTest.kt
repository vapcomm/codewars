package online.vapcom.codewars.strings

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertFailsWith

class MoleculeTest {

    @Test
    fun empty() {
        assertFailsWith<IllegalArgumentException>("Your function should throw an IllegalArgumentException for empty formula") {
            getAtoms("")
        }
    }

    @Test
    fun digitFirst() {
        assertFailsWith<IllegalArgumentException>("Your function should throw an IllegalArgumentException for wrong formula") {
            getAtoms("2C4")
        }
    }

    @Test
    fun testNoDigits() {
        assertEquals("{S=1}", getAtoms("S").toString())
        assertEquals("{He=1}", getAtoms("He").toString())
        assertEquals("{Pb=1, S=1}", getAtoms("PbS").toString())
    }


    @Test
    fun testSimple() {
        assertEquals("{H=2, O=1}", getAtoms("H2O").toString())
        assertEquals("{B=2, H=6}", getAtoms("B2H6").toString())
        assertEquals("{C=6, H=12, O=6}", getAtoms("C6H12O6").toString())
    }

    @Test
    fun testCompound() {
        assertEquals("{Mg=1, O=2, H=2}", getAtoms("Mg(OH)2").toString())
        assertEquals("{Cu=1, O=1, H=1}", getAtoms("Cu(OH)").toString()) // in kata's description: Index after the braces is optional.
        assertEquals("{Cu=1, O=5, H=1, S=1}", getAtoms("Cu(OH)SO4").toString()) // some artificial formula to check () processing
    }

    @Test
    fun testDoubleCompound() {
        assertEquals("{Na=2, Zn=1, O=4, H=4}", getAtoms("Na2[Zn(OH)4]").toString())
        assertEquals("{K=4, O=14, N=2, S=4}", getAtoms("K4[ON(SO3)2]2").toString())
    }

    @Test
    fun testComplexCompound() {
        // from kata's attempt tests
        assertEquals("{C=8, H=8, Fe=1, O=2}", getAtoms("(C5H5)Fe(CO)2CH3").toString())
        assertEquals("{Co=4, N=12, H=42, O=18, S=3}", getAtoms("{[Co(NH3)4(OH)2]3Co}(SO4)3").toString())
    }

    @Test
    fun testCrazyCompound() {
        // from kata's attempt tests
        assertEquals("{H=2, O=1}", getAtoms("{((H)2)[O]}").toString())
    }


}

