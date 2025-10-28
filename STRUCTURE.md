Personnel and Events Dashboard/
├── .kotlin/
│   └── sessions/
├── app/
│   ├── docs/
│   │   ├── D424 Commit History.pdf
│   │   ├── Design Document.docx
│   │   ├── PED Architecture Diagram.png
│   │   ├── PED Class Diagram.png
│   │   ├── Setup and Maintenance Guide.docx
│   │   ├── Unit Test Summary.docx
│   │   └── User Operation Guide.docx
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │   │   └── example/
│   │   │   │   │   │   └── personneleventsdashboard/
│   │   │   │   │   │   │   ├── data/
│   │   │   │   │   │   │   │   ├── management/
│   │   │   │   │   │   │   │   │   ├── DataSeeder.kt
│   │   │   │   │   │   │   │   │   ├── PersonnelDataManager.kt
│   │   │   │   │   │   │   │   │   ├── TailNumberManagementDialog.kt
│   │   │   │   │   │   │   │   │   └── TailNumberManager.kt
│   │   │   │   │   │   │   │   ├── AppDatabase.kt
│   │   │   │   │   │   │   │   ├── AppDatabaseProvider.kt
│   │   │   │   │   │   │   │   ├── DateConverters.kt
│   │   │   │   │   │   │   │   ├── EventDao.kt
│   │   │   │   │   │   │   │   ├── EventRepository.kt
│   │   │   │   │   │   │   │   ├── PersonDao.kt
│   │   │   │   │   │   │   │   ├── PersonRepository.kt
│   │   │   │   │   │   │   │   ├── ShopDao.kt
│   │   │   │   │   │   │   │   ├── TailNumberDao.kt
│   │   │   │   │   │   │   │   └── TailNumberRepository.kt
│   │   │   │   │   │   │   ├── model/
│   │   │   │   │   │   │   │   ├── CalendarModels.kt
│   │   │   │   │   │   │   │   ├── EventEntities.kt
│   │   │   │   │   │   │   │   ├── Person.kt
│   │   │   │   │   │   │   │   ├── Shop.kt
│   │   │   │   │   │   │   │   └── TailNumber.kt
│   │   │   │   │   │   │   ├── ui/
│   │   │   │   │   │   │   │   ├── components/
│   │   │   │   │   │   │   │   │   ├── events/
│   │   │   │   │   │   │   │   │   │   ├── dialogs/
│   │   │   │   │   │   │   │   │   │   │   ├── AddEventDialog.kt
│   │   │   │   │   │   │   │   │   │   │   ├── CustomEventDialog.kt
│   │   │   │   │   │   │   │   │   │   │   ├── DatePickerDialog.kt
│   │   │   │   │   │   │   │   │   │   │   ├── EditEventDialog.kt
│   │   │   │   │   │   │   │   │   │   │   ├── EventDetailsDialog.kt
│   │   │   │   │   │   │   │   │   │   │   ├── PresetEventDialog.kt
│   │   │   │   │   │   │   │   │   │   │   └── ShowAllEventsDialog.kt
│   │   │   │   │   │   │   │   │   │   ├── CalendarComponents.kt
│   │   │   │   │   │   │   │   │   │   ├── EventHelpers.kt
│   │   │   │   │   │   │   │   │   │   └── EventIndicator.kt
│   │   │   │   │   │   │   │   │   ├── management/
│   │   │   │   │   │   │   │   │   │   ├── AddShopDialog.kt
│   │   │   │   │   │   │   │   │   │   ├── EditShopDialog.kt
│   │   │   │   │   │   │   │   │   │   ├── SettingsDialog.kt
│   │   │   │   │   │   │   │   │   │   └── ShopManagementDialog.kt
│   │   │   │   │   │   │   │   │   ├── personnel/
│   │   │   │   │   │   │   │   │   │   ├── AddPersonDialog.kt
│   │   │   │   │   │   │   │   │   │   ├── EditPersonDialog.kt
│   │   │   │   │   │   │   │   │   │   ├── FilterTypeSelector.kt
│   │   │   │   │   │   │   │   │   │   ├── FilterUtils.kt
│   │   │   │   │   │   │   │   │   │   ├── FilterValueSelector.kt
│   │   │   │   │   │   │   │   │   │   ├── PersonDetailsMenuContent.kt
│   │   │   │   │   │   │   │   │   │   ├── PersonPill.kt
│   │   │   │   │   │   │   │   │   │   ├── PersonPillWithMenu.kt
│   │   │   │   │   │   │   │   │   │   └── PersonSelectorDialog.kt
│   │   │   │   │   │   │   │   │   └── shops/
│   │   │   │   │   │   │   │   │   │   ├── ShopColumn.kt
│   │   │   │   │   │   │   │   │   │   └── ShopHelpers.kt
│   │   │   │   │   │   │   │   └── theme/
│   │   │   │   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   │   │   │   ├── Theme.kt
│   │   │   │   │   │   │   │   │   └── Type.kt
│   │   │   │   │   │   │   ├── viewmodel/
│   │   │   │   │   │   │   │   ├── EventViewModel.kt
│   │   │   │   │   │   │   │   ├── PersonViewModel.kt
│   │   │   │   │   │   │   │   ├── PersonViewModelFactory.kt
│   │   │   │   │   │   │   │   ├── ShopViewModel.kt
│   │   │   │   │   │   │   │   └── TailNumberViewModel.kt
│   │   │   │   │   │   │   └── MainActivity.kt
│   │   │   ├── res/
│   │   │   │   ├── mipmap-hdpi/
│   │   │   │   │   └── ic_launcher.webp
│   │   │   │   ├── mipmap-mdpi/
│   │   │   │   │   └── ic_launcher.webp
│   │   │   │   ├── mipmap-xhdpi/
│   │   │   │   │   └── ic_launcher.webp
│   │   │   │   ├── mipmap-xxhdpi/
│   │   │   │   │   └── ic_launcher.webp
│   │   │   │   ├── mipmap-xxxhdpi/
│   │   │   │   │   └── ic_launcher.webp
│   │   │   │   └── values/
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── themes.xml
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   │   │   └── java/
│   │   │   │   └── com/
│   │   │   │   │   └── example/
│   │   │   │   │   │   └── personneleventsdashboard/
│   │   │   │   │   │   │   ├── PersonStatusFilteringTest.kt
│   │   │   │   │   │   │   └── PersonValidationTest.kt
│   ├── .gitignore
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── docs/
│   └── index.html
├── gradle/
│   ├── wrapper/
│   │   ├── gradle-wrapper.jar
│   │   └── gradle-wrapper.properties
│   └── libs.versions.toml
├── releases/
│   └── personnel-events-dashboard-v1.0.0.apk
├── .gitignore
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
├── personnel-events-keystore.jks
├── Powershell Script for updating STRUCTUREmd file.txt
├── README.md
├── settings.gradle.kts
└── STRUCTURE.md
