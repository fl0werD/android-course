package com.example.fl0wer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            openContactList()
        }
    }

    private fun openContactList() {
        val contactListFragment = ContactListFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragments_container, contactListFragment)
            .commit()
    }
}
