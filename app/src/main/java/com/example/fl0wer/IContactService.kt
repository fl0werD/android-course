package com.example.fl0wer

interface IContactService {
    fun getFirstContact(resultHandler: (Contact?) -> Unit)
    fun getContactById(id: Int, resultHandler: (Contact?) -> Unit)
}
