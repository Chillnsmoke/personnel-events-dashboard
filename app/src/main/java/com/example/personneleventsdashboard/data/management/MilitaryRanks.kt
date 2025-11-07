package com.example.personneleventsdashboard.data.management

/**
 * Contains comprehensive lists of all possible military ranks
 * for Coast Guard aviation maintenance personnel
 */
object MilitaryRanks {

    /**
     * All possible Officer ranks in order of seniority (highest to lowest)
     */
    val OFFICER_RANKS = listOf(
        "CAPT",    // Captain
        "CDR",     // Commander
        "LCDR",    // Lieutenant Commander
        "LT",      // Lieutenant
        "LTJG",    // Lieutenant Junior Grade
        "ENS",     // Ensign
        "CWO"      // Chief Warrant Officer
    )

    /**
     * All possible Enlisted ranks in order of seniority (highest to lowest)
     */
    val ENLISTED_RANKS = listOf(
        // Senior Chiefs (E-8/E-9)
        "AMTCM",
        "AETCM",
        "AMTCS",
        "AETCS",
        "AMTC",
        "AETC",
        "AMT1",
        "AET1",
        "AMT2",
        "AET2",
        "AMT3",
        "AET3",
        "AN",
    )

    /**
     * All possible duty sections
     */
    val DUTY_SECTIONS = listOf(
        "Days",
        "Nights",
        "1",
        "2",
        "3",
        "4",
        "AMO"
    )

    /**
     * All possible qualifications (commonly used abbreviations)
     */
    val QUALIFICATIONS = listOf(
        "NQ",
        "BA",
        "BAT",
        "LM",
        "LMI",
        "LMT",
        "LMX",
        "DM",
        "DMI",
        "DMT",
        "DMX",
        "WC",
        "LP",
        "MSO",
        "MSOI",
        "MSOT",
        "MSOX",
        "LOX",
        "LOXI",
        "HP",
        "HP-X",
        "HAZ",
        "LC"
    )

    /**
     * Returns all ranks combined (Officers first, then Enlisted)
     */
    fun getAllRanks(): List<String> {
        return OFFICER_RANKS + ENLISTED_RANKS
    }

    /**
     * Returns all ranks in order of seniority (highest to lowest)
     */
    fun getRanksByOrder(): List<String> {
        return getAllRanks()
    }

    /**
     * Returns all ranks sorted alphabetically
     */
    fun getRanksAlphabetical(): List<String> {
        return getAllRanks().sorted()
    }

    /**
     * Determines if a rank is an officer rank
     */
    fun isOfficerRank(rank: String): Boolean {
        return OFFICER_RANKS.contains(rank)
    }

    /**
     * Determines if a rank is an enlisted rank
     */
    fun isEnlistedRank(rank: String): Boolean {
        return ENLISTED_RANKS.contains(rank)
    }

    /**
     * Gets the rank order value (lower number = higher rank)
     */
    fun getRankOrder(rank: String): Int {
        return getAllRanks().indexOf(rank).takeIf { it >= 0 } ?: Int.MAX_VALUE
    }
}