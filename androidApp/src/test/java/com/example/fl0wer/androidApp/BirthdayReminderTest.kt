package com.example.fl0wer.androidApp

import com.example.fl0wer.androidApp.domain.contacts.Contact
import com.example.fl0wer.androidApp.domain.contacts.ReminderInteractorImpl
import com.example.fl0wer.androidApp.domain.contacts.ReminderRepository
import com.example.fl0wer.androidApp.ui.core.dispatchers.DispatchersProvider
import com.example.fl0wer.androidApp.ui.core.dispatchers.DispatchersProviderImpl
import java.util.GregorianCalendar
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.argWhere
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class BirthdayReminderTest {
    private val reminderRepository: ReminderRepository = mock()
    private val dispatchersProvider: DispatchersProvider = DispatchersProviderImpl

    private val ivanIvanovich = Contact(
        1,
        "abc",
        0,
        "Иван Иванович",
        "", "", "", "",
        GregorianCalendar.SEPTEMBER, 8,
        "",
    )
    private val pavelPavlovich = Contact(
        2,
        "xyz",
        0,
        "Павел Павлович",
        "", "", "", "",
        GregorianCalendar.FEBRUARY, 29,
        "",
    )

    @Test
    fun add_CurrentDateAfterBirthdayWithoutReminder_ToNextYear() = runBlocking {
        val currentDate = GregorianCalendar().apply {
            set(1999, GregorianCalendar.SEPTEMBER, 9)
        }
        val reminderInteractor = ReminderInteractorImpl(
            reminderRepository,
            currentDate,
            dispatchersProvider
        )

        reminderInteractor.addBirthdayReminder(ivanIvanovich)

        verify(reminderRepository).addBirthdayReminder(
            eq(ivanIvanovich),
            argWhere {
                it[GregorianCalendar.YEAR] == 2000 &&
                    it[GregorianCalendar.MONTH] == GregorianCalendar.SEPTEMBER &&
                    it[GregorianCalendar.DAY_OF_MONTH] == 8
            }
        )
    }

    @Test
    fun add_CurrentDateBeforeBirthdayWithoutReminder_ToCurrentYear() = runBlocking {
        val currentDate = GregorianCalendar().apply {
            set(1999, GregorianCalendar.SEPTEMBER, 7)
        }
        val reminderInteractor = ReminderInteractorImpl(
            reminderRepository,
            currentDate,
            dispatchersProvider
        )

        reminderInteractor.addBirthdayReminder(ivanIvanovich)

        verify(reminderRepository).addBirthdayReminder(
            eq(ivanIvanovich),
            argWhere {
                it[GregorianCalendar.YEAR] == 1999 &&
                    it[GregorianCalendar.MONTH] == GregorianCalendar.SEPTEMBER &&
                    it[GregorianCalendar.DAY_OF_MONTH] == 8
            }
        )
    }

    @Test
    fun remove_WithReminder() = runBlocking {
        val currentDate = GregorianCalendar().apply {
            set(GregorianCalendar.YEAR, 1999)
        }
        val reminderInteractor = ReminderInteractorImpl(
            reminderRepository,
            currentDate,
            dispatchersProvider
        )

        reminderInteractor.addBirthdayReminder(ivanIvanovich)
        reminderInteractor.removeBirthdayReminder(ivanIvanovich)

        verify(reminderRepository).removeBirthdayReminder(eq(ivanIvanovich))
    }

    @Test
    fun add_29FebruaryWithoutReminder() = runBlocking {
        val currentDate = GregorianCalendar().apply {
            set(1999, GregorianCalendar.MARCH, 2)
        }
        val reminderInteractor = ReminderInteractorImpl(
            reminderRepository,
            currentDate,
            dispatchersProvider
        )

        reminderInteractor.addBirthdayReminder(pavelPavlovich)

        verify(reminderRepository).addBirthdayReminder(
            eq(pavelPavlovich),
            argWhere {
                it[GregorianCalendar.YEAR] == 2000 &&
                    it[GregorianCalendar.MONTH] == GregorianCalendar.FEBRUARY &&
                    it[GregorianCalendar.DAY_OF_MONTH] == 29
            }
        )
    }

    @Test
    fun add_29FebruaryLeapYearWithoutReminder_In4Years() = runBlocking {
        val currentDate = GregorianCalendar().apply {
            set(2000, GregorianCalendar.MARCH, 1)
        }
        val reminderInteractor = ReminderInteractorImpl(
            reminderRepository,
            currentDate,
            dispatchersProvider
        )

        reminderInteractor.addBirthdayReminder(pavelPavlovich)

        verify(reminderRepository).addBirthdayReminder(
            eq(pavelPavlovich),
            argWhere {
                it[GregorianCalendar.YEAR] == 2004 &&
                    it[GregorianCalendar.MONTH] == GregorianCalendar.FEBRUARY &&
                    it[GregorianCalendar.DAY_OF_MONTH] == 29
            }
        )
    }
}
