package com.example.fl0wer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.example.fl0wer.Const.NOTICE_BIRTHDAY_EXTRA_CONTACT_ID
import com.example.fl0wer.Const.UNDEFINED_CONTACT_ID

fun interface ConnectionListener {
    fun onServiceConnected()
}

fun interface ContactClickListener {
    fun openContact(contactId: Int)
}

class MainActivity : AppCompatActivity(R.layout.activity_main),
    IContactServiceListener,
    ContactClickListener {
    private val contactServiceListeners = mutableListOf<ConnectionListener>()
    private var boundService = false
    private val contactServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as ContactService.ContactBinder
            contactService = binder.contactService
            boundService = true
            contactServiceListeners.forEach {
                it.onServiceConnected()
            }
        }

        override fun onServiceDisconnected(className: ComponentName) {
            boundService = false
        }
    }
    var contactService: IContactService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContactService()
        if (savedInstanceState == null) {
            openContactList()
            intent.checkNavigation()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.checkNavigation()
    }

    override fun onDestroy() {
        unbindContactService()
        super.onDestroy()
    }

    private fun openContactList() {
        val contactListFragment = ContactListFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragments_container, contactListFragment)
            .commit()
    }

    private fun bindContactService() {
        Intent(this, ContactService::class.java).also { intent ->
            bindService(intent, contactServiceConnection, BIND_AUTO_CREATE)
        }
    }

    private fun unbindContactService() {
        if (boundService) {
            boundService = false
            contactService = null
            unbindService(contactServiceConnection)
        }
    }

    private fun Intent.checkNavigation() {
        val contactId = getIntExtra(NOTICE_BIRTHDAY_EXTRA_CONTACT_ID, UNDEFINED_CONTACT_ID)
        if (contactId != UNDEFINED_CONTACT_ID) {
            openContact(contactId)
        }
    }

    override fun contactService(): IContactService? {
        return contactService
    }

    override fun subscribeContactService(listener: ConnectionListener) {
        contactServiceListeners.add(listener)
    }

    override fun unsubscribeContactService(listener: ConnectionListener) {
        contactServiceListeners.remove(listener)
    }

    override fun openContact(contactId: Int) {
        val contactDetailsFragment = ContactDetailsFragment.newInstance(contactId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragments_container, contactDetailsFragment)
            .addToBackStack(null)
            .commit()
    }
}
