package com.example.fl0wer

interface IContactServiceListener {
    fun contactService(): IContactService?
    fun subscribeContactService(listener: ConnectionListener)
    fun unsubscribeContactService(listener: ConnectionListener)
}
