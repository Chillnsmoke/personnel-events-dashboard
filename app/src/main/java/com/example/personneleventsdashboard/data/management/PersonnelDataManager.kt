package com.example.personneleventsdashboard.data.management

import com.example.personneleventsdashboard.model.Person

class PersonnelDataManager {

    /**
     * Returns real personnel data for the air station
     */
    fun getRealPersonnelData(): List<PersonData> {
        return listOf(
            // LEADERSHIP (add these manually)
            PersonData("LT", "Geyer", "Cory", "C130 AVENG Officer", "", "NQ", "Days"),

            PersonData("CWO", "Basset", "Andrew", "Maintenance Officer", "916-802-3962", "NQ", "Days"),

            PersonData("CWO", "Marsh", "Kelly", "AMO", "281-622-9344", "NQ", "Days"),
            PersonData("AETC", "Blumerick", "Cam", "AMO", "252-202-3059", "NQ", "Days"),

            PersonData("AMTCM", "Peters", "Jesse", "LCPO", "916-281-8603", "NQ", "Days"),

            PersonData("AMTCS", "Herring", "Jon", "Division Managers", "828-434-6150", "NQ", "Days"),
            PersonData("AMTCS", "Hietala", "Derek", "Division Managers", "231-392-6765", "NQ", "Days"),

            PersonData("AMTC", "Burke", "Travis", "Maintenance Control", "907-942-2095", "NQ", "Days"),

            PersonData("AETC", "Culton", "Levi", "Flight Schedules", "727-631-1816", "NQ", "Days"),

            PersonData("AETCS", "Saliba", "Bill", "AVENG Flight Pay", "727-421-6047", "NQ", "Days"),

            PersonData("AMTC", "Knight", "Greg", "Engine", "530-921-1031", "LM", "Days"),

            PersonData("AMTC", "Etheridge", "Drew", "Metal", "252-532-5206", "LMI", "Days"),

            PersonData("AETC", "Friese", "Ryan", "Avionics", "251-422-1344", "NQ", "Days"),

            PersonData("AETC", "Owens", "Dave", "Sensor", "509-969-7976", "NQ", "Days"),

            PersonData("AETC", "Wells", "Kiel", "QA", "508367-2895", "NQ", "Days"),

            PersonData("AMTC", "Peterson", "Stew", "Nights", "253-678-8392", "NQ", "Days"),

            PersonData("AMTC", "Shirley", "Josh", "Prop", "574-265-4113", "NQ", "Days"),

            PersonData("AMTC", "Abbott", "Steve", "Load Cage", "907-942-5980", "NQ", "Days"),

            PersonData("AETC", "O'neil", "Steve", "Tool Room", "707-704-6366", "NQ", "Days"),

            PersonData("AMT2", "Anderson", "Ryan", "Metal", "937-499-3079", "BA, DM", "2"),
            PersonData("AN", "Apo", "Connor", "Line Crew", "", "NQ", "Days"),
            PersonData("AET1", "Arakaki", "Tresen", "Avionics", "808-896-4353", "MSO, WC", "4"),
            PersonData("AET3", "Arnold", "Jakob", "Avionics", "360-852-0910", "MSO", "4"),
            PersonData("AMT3", "Barnes", "Laken", "Metal", "803-410-7708", "BA, DMT, LOX", "1"),
            PersonData("AET3", "Bateman", "Caleb", "Avionics", "863-446-0244", "BA, MSOT", "4"),
            PersonData("AET2", "Beltran", "George", "Sensor", "907-942-3266", "MSOI", "3"),
            PersonData("AET2", "Birk", "Noah", "Avionics", "217-271-9600", "MSO", "2"),
            PersonData("AET2", "Blakley", "Cody", "AMO", "972-754-9700", "MSO, LOX", "AMO"),
            PersonData("AET1", "Brewer", "Kevin", "Maintenance Control", "813-394-1000", "LM, WC, LP", "Days"),
            PersonData("AMT2", "Bryan", "Spencer", "Engine", "910-546-9979", "BAT, DMT", "4"),
            PersonData("AMT3", "Buser", "Mason", "Nights", "309-826-8764", "BA, LMT", "Nights"),
            PersonData("AN", "Campanella", "GianMarco", "Line Crew", "732-239-6884", "NQ", "Days"),
            PersonData("AMT2", "Carter", "Thomas", "Metal", "907-942-4937", "DMI, WC", "2"),
            PersonData("AET3", "Cerice", "Andres", "Avionics", "305-794-0289", "MSO, LOX", "4"),
            PersonData("AET3", "Chartier", "Jessica", "Avionics", "920-489-7184", "MSO", "1"),
            PersonData("AMT2", "Clark", "Rexxie", "Metal", "505-314-3623", "DM, LOX, WC", "2"),
            PersonData("AMT3", "Cole", "Jack", "Nights", "513-601-6660", "DM", "Nights"),
            PersonData("AET1", "Cook", "Brandon", "Avionics", "219-476-5935", "WC", "Days"),
            PersonData("AET1", "Davis", "Spencer", "QA - Nights", "740-604-3204", "WC, MSO", "Nights"),
            PersonData("AET2", "Duffy", "Brennan", "Avionics", "863-532-9035", "BAT, MSOT", "3"),
            PersonData("AET1", "Dyar", "Eric", "Tool Room", "909-921-2447", "WC, MSO", "3"),
            PersonData("AMT1", "Felthoff", "Dalton", "Prop", "352-398-6851", "BA, WC, LM", "4"),
            PersonData("AN", "Fitzsimmons", "Joey", "Line Crew", "253-880-6585", "NQ", "Days"),
            PersonData("AMT3", "Flanagan", "Josiah", "Nights", "810-494-3900", "DM", "Nights"),
            PersonData("AMT2", "Fleming", "Ryan", "Prop", "660-233-9408", "DM, WC", "1"),
            PersonData("AET1", "Floyd", "Matt", "QA", "334-237-2127", "MSO, WC", "4"),
            PersonData("AET3", "Fosbenner", "Ty", "Sensor", "956-336-0144", "MSO", "2"),
            PersonData("AMT1", "Foster", "Brandon", "Load Cage", "404-985-5960", "LMI, WC, HP", "3"),
            PersonData("AN", "Foster", "Micheal", "Line Crew", "", "NQ", "3"),
            PersonData("AET3", "Galao Birkmann", "Daniel", "Nights", "862-206-0501", "MSO", "Nights"),
            PersonData("AET1", "Gallagher", "Rob", "QA", "305-509-9439", "MSO, WC", "3"),
            PersonData("AET1", "Garza", "Kyle", "Avionics", "281-740-8414", "WC, MSO", "2"),
            PersonData("AET2", "Gordon", "Timothy", "Tool Room", "425-281-5913", "BA, MSOT, WC", "1"),
            PersonData("AMT3", "Goszkowicz", "Bryce", "Load Cage", "507-861-1230", "DM, LMT", "2"),
            PersonData("AET2", "Hawthorne", "Joseph", "Nights", "252-622-7115", "MSOX, WC", "Nights"),
            PersonData("AMT3", "Hernandez", "Danilo", "Tool Room", "9079421495", "BAT", "Days"),
            PersonData("AN", "Hickey", "Connor", "Line Crew", "360-670-6526", "LC", "Days"),
            PersonData("AMT2", "Hipes", "Dakota", "AMO", "831-402-5187", "BA, DM", "Days"),
            PersonData("AET3", "Hixson", "Clifford", "Avionics", "252-619-1234", "BA, MSO", "2"),
            PersonData("AET2", "Hollins", "Romeo", "Avionics", "815-908-1447", "BAT", "1"),
            PersonData("AMT2", "Hume", "Kyle", "Nights", "904-589-4694", "DM, WC, HP", "Nights"),
            PersonData("AMT3", "Hutchinson", "Jon", "Nights", "504-913-3900", "DM", "Nights"),
            PersonData("AN", "Irinaka", "Towa", "Line Crew", "", "NQ", "Days"),
            PersonData("AET3", "Johnson", "Carter", "Avionics", "832-844-7773", "BAT", "2"),
            PersonData("AET2", "Lacasse", "Zach", "Sensor", "541-420-4745", "MSO", "1"),
            PersonData("AMT1", "Laflin", "Walker", "Nights", "760-981-9362", "LM, WC, HP", "Nights"),
            PersonData("AN", "Leopoldi", "Robert", "Line Crew", "718-306-4881", "NQ", "Days"),
            PersonData("AMT2", "Liebl", "Bryce", "Nights", "815-501-9887", "DM", "Nights"),
            PersonData("AET1", "Link", "James", "Sensor", "253-720-1849", "MSOX, LOXI, WC", "1"),
            PersonData("AET1", "Lira", "Daniel", "Line Crew", "541-300-9064", "MSO, WC, LOX", "1"),
            PersonData("AMT1", "Lopez", "Vick", "Metal", "915-487-4094", "BAT, LMT", "2"),
            PersonData("AET3", "Lott", "James", "Avionics", "505-920-0870", "BA, MSOT", "3"),
            PersonData("AMT3", "Luke", "Jacob", "Nights", "770-870-8639", "LM", "Nights"),
            PersonData("AMT1", "Lupo-Mack", "Troy", "Metal", "607-343-8882", "BAT", "3"),
            PersonData("AMT3", "Manor", "Elijah", "Engine", "850-417-4685", "LM, LP", "3"),
            PersonData("AET2", "Martinez", "Haz", "Sensor", "786-283-1409", "MSOI, WC", "4"),
            PersonData("AET1", "McCarthy", "Tyler", "Sensor", "334-557-2633", "MSO, WC", "2"),
            PersonData("AET2", "Mckee", "Lucas", "Avionics", "419-603-3545", "BAT", "4"),
            PersonData("AMT2", "Meiggs", "Ken", "Prop", "609-665-6174", "DM", "4"),
            PersonData("AET1", "Merchan", "JonJon", "Avionics", "907-942-4773", "BAT", "3"),
            PersonData("AMT2", "Michael", "Adam", "Load Cage", "972-268-5459", "BA, LMT", "1"),
            PersonData("AET3", "Molenkamp", "Nick", "Nights", "503-953-6097", "MSO", "Nights"),
            PersonData("AMT3", "Mueller", "Drew", "Nights", "361-522-6000", "BA, DMT", "Nights"),
            PersonData("AN", "Myers", "Ben", "Line Crew", "760-458-8427", "LC", "Days"),
            PersonData("AMT2", "Narkis", "Dave", "QA", "413-961-2251", "DMI, WC", "4"),
            PersonData("AMT2", "Neely", "Darien", "Engine", "443-262-2727", "LM", "3"),
            PersonData("AN", "Norris", "Nicholas", "Line Crew", "805-341-8877", "NQ", "Days"),
            PersonData("AMT3", "Odham", "Hunter", "Prop", "252-515-6336", "DM", "3"),
            PersonData("AET2", "Olsen", "Nathan", "Avionics", "813-714-9797", "BA, MSOT", "1"),
            PersonData("AMT2", "Ortiz", "Alyssa", "Nights", "321-917-7786", "LM, LP, LOX", "Nights"),
            PersonData("AN", "Palic", "Kiana", "Line Crew", "208-789-3166", "NQ", "Days"),
            PersonData("AMT3", "Parks", "Morgan", "Metal", "512-635-0253", "BA, DMT", "3"),
            PersonData("AMT1", "Picciolo", "Eric", "Nights", "830-660-3939", "WC", "Nights"),
            PersonData("AMT2", "Portuondo", "Jose", "Engine", "786-269-5065", "LM", "2"),
            PersonData("AET3", "Preciado", "Ares", "Nights", "928-581-9207", "MSO", "Nights"),
            PersonData("AET3", "Price", "Davis", "Sensor", "813-679-7557", "MSO", "3"),
            PersonData("AN", "Pulido", "Andrew", "Line Crew", "951-498-8773", "NQ", "Days"),
            PersonData("AMT3", "Reger", "Aaron", "Metal", "941-725-5756", "BAT", "2"),
            PersonData("AMT2", "Rigterink", "Joshua", "Metal", "808-369-6275", "DMI", "1"),
            PersonData("AET2", "Roberts", "Andrew", "Nights", "740-629-3014", "MSOI", "Nights"),
            PersonData("AMT3", "Roman", "Aaron", "Metal", "703-463-6695", "BA, DMT", "2"),
            PersonData("AET1", "Roscovius", "Brandon", "Nights", "907-854-8058", "MSO", "Nights"),
            PersonData("AN", "Roth", "Samuel", "Line Crew", "949-899-4397", "NQ", "Days"),
            PersonData("AMT3", "Rowe", "Austin", "Metal", "941-243-9466", "BAT", "3"),
            PersonData("AMT2", "Scheck", "Tyler", "Metal", "970-744-9940", "DMX, WC", "3"),
            PersonData("AMT3", "Schmidt", "John", "Prop", "907-942-5266", "DM", "1"),
            PersonData("AMT3", "Setzer", "Darian", "Engine", "916-225-5814", "LM, LOX", "2"),
            PersonData("AET2", "Shannon", "Connor", "Avionics", "360-719-8737", "MSO, WC", "1"),
            PersonData("AMT1", "Sharkey", "Ian", "Metal", "619-997-0270", "LM, WC, HP, HAZ", "1"),
            PersonData("AMT3", "Shier", "Seth", "Nights", "920-680-6498", "LM", "Nights"),
            PersonData("AET3", "Skiba", "Atom", "Nights", "907-953-0601", "MSO", "Nights"),
            PersonData("AET3", "Skourtis", "Nick", "Tool Room", "540-538-4825", "NQ", "Days"),
            PersonData("AMT1", "Smith", "Colby", "Engine", "479-200-6695", "WC, BA, LMT, LP", "4"),
            PersonData("AET2", "Smith", "Hunter", "Load Cage", "334-718-2672", "LMX, WC, HP-X", "2"),
            PersonData("AMT3", "Stewart", "Austin", "Engine", "252-562-8206", "BAT", "days"),
            PersonData("AET2", "Suesens", "Nate", "Sensor", "619-606-4992", "MSO, LOX", "1"),
            PersonData("AET3", "Terrell", "Averre", "Avionics", "407-520-2864", "BAT", "3"),
            PersonData("AET1", "Todd", "Daniel", "MPC Analyst", "908-783-5665", "MSO, WC", "1"),
            PersonData("AET3", "Torres", "Tecolote", "Tool Room", "253-232-3511", "MSO", "4"),
            PersonData("AET3", "Verity", "Branden", "Avionics", "631-764-6823", "MSO", "2"),
            PersonData("AET3", "Vitale", "Dino", "Sensor", "646-660-3810", "MSO", "Days"),
            PersonData("AMT2", "Voigt", "Charles", "QA - Nights", "850-333-2259", "LM, WC, LP", "Nights"),
            PersonData("AMT3", "Walker", "Carson", "Nights", "352-816-4275", "BA, DMT", "Nights"),
            PersonData("AET2", "Warden", "Shamus", "Sensor", "360-213-5717", "MSO", "2"),
            PersonData("AMT1", "Weise", "Claire", "QA", "541-808-1172", "DM, WC", "4"),
            PersonData("AMT3", "Welzig", "James", "Engine", "303-520-2065", "BAT", "4"),
            PersonData("AMT3", "Wilcox", "Claire", "Nights", "541-880-8081", "DM", "Nights"),
            PersonData("AET3", "Wilcox", "Luke", "Avionics", "509-795-4815", "BA, MSOT", "1"),
            PersonData("AET1", "Will", "Kerr", "Avionics", "919-570-4423", "MSOX, WC", "3"),
            PersonData("AMT2", "Williams", "Reece", "Nights", "601-527-5272", "LM", "Nights"),
            PersonData("AET2", "Wilson", "Neil", "Nights", "863-214-0783", "MSOX, WC, LOXI", "Nights"),
            PersonData("AMT1", "Wolfe", "Logan", "Training", "616-914-4617", "LMX, WC, HP-X", "4"),
            PersonData("AET2", "Yoder", "Daniel", "Engine", "812-706-5451", "MSO, WC", "3"),
            PersonData("AMT1", "Zalewski", "Zach", "Prop", "252-548-4201", "LM, WC, HAZ", "4")
        )
    }
    data class PersonData(
        val rank: String,
        val lastName: String,
        val firstName: String,
        val shopName: String,
        val phoneNumber: String,
        val qualifications: String,
        val dutySection: String,
        val status: String = "Normal"
    )

    /**
     * Converts PersonData to database Person entities
     */
    fun convertToPersonEntities(personnelData: List<PersonData>, shopIdMap: Map<String, Int>): List<Person> {
        return personnelData.map { data ->
            Person(
                lastName = data.lastName,
                firstName = data.firstName,
                rank = data.rank,
                shopId = shopIdMap[data.shopName] ?: 1,
                phoneNumber = data.phoneNumber,
                qualifications = data.qualifications,
                dutySection = data.dutySection,
                status = data.status
            )
        }
    }
}