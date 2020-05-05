package com.byfrunze.memshelper.helpers

object Results {
    data class MotivationQuotes(val quoteText: String, val quoteAuthor: String)
    data class TranslateQuotes(val text: ArrayList<String>)
    data class ChackNorrisQuotes(val icon_url: String, val value: String)

    data class AdviceSlipQuotes(val slip: Slip)
    data class Slip(val advice: String, val slip_id: String)

    data class ProgrammingQuotes(val en: String, val author: String)

}