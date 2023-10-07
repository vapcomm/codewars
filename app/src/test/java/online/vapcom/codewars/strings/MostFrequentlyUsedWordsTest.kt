package online.vapcom.codewars.strings

import org.junit.Test
import kotlin.test.assertEquals

class MostFrequentlyUsedWordsTest {

    @Test
    fun sampleTests() {
        assertEquals(listOf("e", "d", "a"), top3("a a a  b  c c  d d d d  e e e e e"))
        assertEquals(listOf("e", "ddd", "aa"), top3("e e e e DDD ddd DdD: ddd ddd aa aA Aa, bb cc cC e e e"))
        assertEquals(listOf("won't", "wont"), top3("  //wont won't won't "))
        assertEquals(listOf("e"), top3("  , e   .. "))
        assertEquals(emptyList(), top3("  ...  "))
        assertEquals(emptyList(), top3("  '  "))
        assertEquals(emptyList(), top3("  '''  "))
        assertEquals(listOf("a", "of", "on"), top3(sequenceOf(
            "In a village of La Mancha, the name of which I have no desire to call to",
            "mind, there lived not long since one of those gentlemen that keep a lance",
            "in the lance-rack, an old buckler, a lean hack, and a greyhound for",
            "coursing. An olla of rather more beef than mutton, a salad on most",
            "nights, scraps on Saturdays, lentils on Fridays, and a pigeon or so extra",
            "on Sundays, made away with three-quarters of his income."
        ).joinToString("\n")))
    }

    @Test
    fun attemptTests() {
        assertEquals(listOf("vqvnzl", "vjxznvsier", "ctllzlbwem"),
            top3("CYYdyZVsG CYYdyZVsG cTllZlbweM CYYdyZVsG?vQVNZl VjXznVSIER CYYdyZVsG SmAiQTOXWm vQVNZl RtLIQ,VjXznVSIER " +
                    "CYYdyZVsG CYYdyZVsG VjXznVSIER cTllZlbweM RUlI,aNdydm cTllZlbweM cTllZlbweM VjXznVSIER CYYdyZVsG.vQVNZl:vQVNZl " +
                    "VjXznVSIER RUlI:VjXznVSIER vQVNZl cTllZlbweM,vQVNZl RtLIQ-VjXznVSIER cTllZlbweM CYYdyZVsG,vQVNZl!SmAiQTOXWm " +
                    "vQVNZl cTllZlbweM aNdydm;SmAiQTOXWm.PNz?RUlI vQVNZl RtLIQ/vQVNZl:vQVNZl SRUIZE aNdydm_VjXznVSIER,RtLIQ,CYYdyZVsG " +
                    "VjXznVSIER:cTllZlbweM VjXznVSIER/VjXznVSIER aNdydm cTllZlbweM vQVNZl RtLIQ cTllZlbweM/SmAiQTOXWm,VjXznVSIER;" +
                    "vQVNZl!SmAiQTOXWm_SRUIZE SRUIZE RtLIQ vQVNZl VjXznVSIER;vQVNZl/cTllZlbweM_PNz SmAiQTOXWm cTllZlbweM_vQVNZl/VjXznVSIER " +
                    "vQVNZl?SmAiQTOXWm aNdydm!CYYdyZVsG cTllZlbweM VjXznVSIER RtLIQ:cTllZlbweM aNdydm/cTllZlbweM!vQVNZl vQVNZl aNdydm:RtLIQ!VjXznVSIER " +
                    "VjXznVSIER cTllZlbweM!RtLIQ CYYdyZVsG!vQVNZl:SmAiQTOXWm CYYdyZVsG/cTllZlbweM aNdydm SmAiQTOXWm:VjXznVSIER;vQVNZl,vQVNZl " +
                    "aNdydm!VjXznVSIER SRUIZE VjXznVSIER aNdydm?aNdydm.vQVNZl,aNdydm!VjXznVSIER.vQVNZl VjXznVSIER-cTllZlbweM;cTllZlbweM!SmAiQTOXWm " +
                    "vQVNZl-VjXznVSIER RtLIQ-aNdydm SmAiQTOXWm,SmAiQTOXWm!SmAiQTOXWm/VjXznVSIER vQVNZl CYYdyZVsG-VjXznVSIER?aNdydm/SmAiQTOXWm cTllZlbweM " +
                    "PNz VjXznVSIER,RtLIQ;SmAiQTOXWm vQVNZl!RtLIQ/"))

        assertEquals(listOf("xwj", "uuxrnrdwck", "ayldtsm"),
            top3("tWZgjT.tWZgjT;kuuKa kZhrAqlIE/RpvrIkkbJ Uuxrnrdwck kZhrAqlIE PsslmkhlVa:wehJlljZQW:PsslmkhlVa.k'bxN_kuuKa uCyHPkF " +
                    "Uuxrnrdwck kZhrAqlIE_wehJlljZQW.GivKEajQLm?k'bxN;AYLdTsm ysObh XwJ XwJ wehJlljZQW?Uuxrnrdwck kuuKa!mAtTtrZ XwJ;mnxva " +
                    "mAtTtrZ kZhrAqlIE,AYLdTsm.kZhrAqlIE kuuKa-kZhrAqlIE tWZgjT mnxva.XwJ uCyHPkF;mnxva;XwJ GivKEajQLm;XwJ:kZhrAqlIE " +
                    "GivKEajQLm GivKEajQLm?GivKEajQLm wehJlljZQW tWZgjT Uuxrnrdwck RpvrIkkbJ.GivKEajQLm wehJlljZQW wehJlljZQW,k'bxN tWZgjT," +
                    "AYLdTsm Uuxrnrdwck uCyHPkF;mAtTtrZ!kuuKa kuuKa,mnxva Uuxrnrdwck mAtTtrZ.wehJlljZQW k'bxN wehJlljZQW,Uuxrnrdwck.tWZgjT " +
                    "kuuKa mnxva!XwJ uCyHPkF;Uuxrnrdwck?tWZgjT.AYLdTsm wehJlljZQW kuuKa/kuuKa uCyHPkF/XwJ uCyHPkF wehJlljZQW wehJlljZQW.wehJlljZQW/" +
                    "GivKEajQLm!uCyHPkF mnxva kuuKa;mAtTtrZ Uuxrnrdwck PsslmkhlVa;RpvrIkkbJ:k'bxN/tWZgjT?Uuxrnrdwck_mAtTtrZ.AYLdTsm RpvrIkkbJ " +
                    "kZhrAqlIE GivKEajQLm ysObh,kZhrAqlIE.AYLdTsm:uCyHPkF mnxva:kuuKa?uCyHPkF wehJlljZQW mnxva tWZgjT tWZgjT GivKEajQLm kuuKa " +
                    "mnxva/XwJ:kZhrAqlIE,uCyHPkF_kuuKa_AYLdTsm kuuKa AYLdTsm kuuKa wehJlljZQW?uCyHPkF:AYLdTsm_RpvrIkkbJ/Uuxrnrdwck-XwJ " +
                    "mAtTtrZ,mAtTtrZ uCyHPkF/XwJ:wehJlljZQW:Uuxrnrdwck tWZgjT Uuxrnrdwck?kuuKa kuuKa,Uuxrnrdwck XwJ uCyHPkF?tWZgjT.tWZgjT,k'bxN;" +
                    "tWZgjT,kZhrAqlIE?k'bxN/AYLdTsm Uuxrnrdwck;wehJlljZQW_mAtTtrZ PsslmkhlVa/uCyHPkF uCyHPkF?mnxva:mnxva tWZgjT mAtTtrZ " +
                    "XwJ_uCyHPkF-AYLdTsm-mnxva;tWZgjT RpvrIkkbJ tWZgjT/PsslmkhlVa k'bxN uCyHPkF Uuxrnrdwck!AYLdTsm wehJlljZQW mnxva wehJlljZQW " +
                    "Uuxrnrdwck k'bxN/AYLdTsm,kuuKa kZhrAqlIE XwJ,k'bxN Uuxrnrdwck,Uuxrnrdwck AYLdTsm k'bxN Uuxrnrdwck wehJlljZQW wehJlljZQW " +
                    "XwJ AYLdTsm/kZhrAqlIE,XwJ k'bxN!RpvrIkkbJ GivKEajQLm_XwJ XwJ XwJ-uCyHPkF tWZgjT k'bxN AYLdTsm,k'bxN_k'bxN tWZgjT,mnxva k'bxN " +
                    "kZhrAqlIE kZhrAqlIE/ysObh AYLdTsm?uCyHPkF XwJ-mAtTtrZ/uCyHPkF!wehJlljZQW:AYLdTsm kZhrAqlIE!PsslmkhlVa?ysObh_RpvrIkkbJ mnxva " +
                    "PsslmkhlVa:tWZgjT?kuuKa GivKEajQLm.uCyHPkF k'bxN-AYLdTsm-uCyHPkF tWZgjT AYLdTsm_tWZgjT PsslmkhlVa wehJlljZQW/k'bxN!wehJlljZQW!" +
                    "kuuKa k'bxN-Uuxrnrdwck Uuxrnrdwck kuuKa,AYLdTsm XwJ/mnxva mAtTtrZ;kuuKa,kuuKa uCyHPkF,uCyHPkF;k'bxN wehJlljZQW,kZhrAqlIE:" +
                    "kZhrAqlIE/kuuKa wehJlljZQW kZhrAqlIE mAtTtrZ!k'bxN XwJ Uuxrnrdwck tWZgjT Uuxrnrdwck-ysObh-kuuKa:kuuKa RpvrIkkbJ ysObh " +
                    "AYLdTsm,AYLdTsm/uCyHPkF kuuKa wehJlljZQW kZhrAqlIE-Uuxrnrdwck GivKEajQLm!mnxva_ysObh;GivKEajQLm Uuxrnrdwck.AYLdTsm kuuKa " +
                    "k'bxN:XwJ:XwJ k'bxN RpvrIkkbJ-k'bxN/PsslmkhlVa-XwJ ysObh,k'bxN?AYLdTsm XwJ PsslmkhlVa Uuxrnrdwck-mnxva k'bxN,AYLdTsm;" +
                    "Uuxrnrdwck tWZgjT mnxva:PsslmkhlVa:GivKEajQLm:mnxva:Uuxrnrdwck!uCyHPkF_mnxva XwJ wehJlljZQW AYLdTsm.RpvrIkkbJ mnxva " +
                    "uCyHPkF AYLdTsm/mnxva_Uuxrnrdwck-kZhrAqlIE:kZhrAqlIE,RpvrIkkbJ?XwJ?wehJlljZQW k'bxN XwJ XwJ-mAtTtrZ AYLdTsm uCyHPkF.mAtTtrZ-mnxva " +
                    "PsslmkhlVa GivKEajQLm."))
    }

}
