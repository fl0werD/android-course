package com.example.fl0wer

interface IContactService {
    fun getFirstContact(resultHandler: (Contact?) -> Unit)
    fun getContactById(lookupKey: String, resultHandler: (Contact?) -> Unit)
}
