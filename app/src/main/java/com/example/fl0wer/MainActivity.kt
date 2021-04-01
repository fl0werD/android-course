package com.example.fl0wer

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.fl0wer.Const.NOTICE_BIRTHDAY_EXTRA_CONTACT_ID

fun interface ConnectionListener {
    fun onServiceConnected()
}

fun interface ContactClickListener {
    fun openContact(contactId: String)
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
    private val readContactsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            bindContactService()
            startNavigation()
        } else {
            showPermissionRationaleDialog()
        }
    }
    var contactService: IContactService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readContactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onDestroy() {
        unbindContactService()
        super.onDestroy()
    }

    private fun startNavigation() {
        openContactList()
        handleIntent(intent)
    }

    private fun openContactList() {
        val contactListFragment = ContactListFragment.newInstance()
        supportFragmentManager.commit {
            replace(R.id.fragments_container, contactListFragment)
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.permission_text))
            .setPositiveButton(getString(R.string.request_permission)) { _, _ ->
                readContactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
            .setCancelable(false)
            .create()
            .show()
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

    private fun handleIntent(intent: Intent) {
        val contactId = intent.getStringExtra(NOTICE_BIRTHDAY_EXTRA_CONTACT_ID) ?: return
        openContact(contactId)
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

    override fun openContact(contactId: String) {
        val contactDetailsFragment = ContactDetailsFragment.newInstance(contactId)
        supportFragmentManager.commit {
            replace(R.id.fragments_container, contactDetailsFragment)
            addToBackStack(null)
        }
    }
}
