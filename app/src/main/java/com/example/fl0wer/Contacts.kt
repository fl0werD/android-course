package com.example.fl0wer

object Contacts {
    private val myContact = Contact(
        1337,
        R.drawable.ic_contact,
        "Александр Сабитов",
        "+7 951 210-14-21",
        "+7 951 210-14-21",
        "fl0werdxd@gmail.com",
        "fl0werdxd@gmail.com",
        "Яйцо - 1 штука\n" +
                "Сахар - 3 ст. ложки\n" +
                "Молоко - 2 ст. ложки\n" +
                "Масло подсолнечное - 3 ст. ложки\n" +
                "Какао-порошок - 1 ст. ложка\n" +
                "Мука - 4 ст. ложки\n" +
                "Разрыхлитель для теста - 1 чайная ложка \n" +
                "Ванилин - 1 щепотка"
    )

    val contacts = listOf(myContact)

    fun getUserById(id: Int): Contact? {
        return contacts.find { it.id == id }
    }
}
