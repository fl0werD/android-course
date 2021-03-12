package com.example.fl0wer

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.lang.ref.WeakReference
import java.util.concurrent.Executors

class ContactService : Service() {
    private val binder: IBinder = ContactBinder()
    private val threadExecutor = Executors.newSingleThreadExecutor()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onDestroy() {
        threadExecutor.shutdown()
        super.onDestroy()
    }

    inner class ContactBinder : Binder() {
        val contactService = object : IContactService {
            override fun getFirstContact(resultHandler: (Contact?) -> Unit) {
                val weakReferenceHandler = WeakReference(resultHandler)
                threadExecutor.execute {
                    Thread.sleep(1_000)
                    weakReferenceHandler.get()?.invoke(Contacts.contacts.firstOrNull())
                }
            }

            override fun getContactById(id: Int, resultHandler: (Contact?) -> Unit) {
                val weakReferenceHandler = WeakReference(resultHandler)
                threadExecutor.execute {
                    Thread.sleep(1_000)
                    weakReferenceHandler.get()?.invoke(Contacts.getUserById(id))
                }
            }
        }
    }
}
