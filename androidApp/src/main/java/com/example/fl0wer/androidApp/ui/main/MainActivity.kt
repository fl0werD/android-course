package com.example.fl0wer.androidApp.ui.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fl0wer.androidApp.di.App
import com.example.fl0wer.androidApp.util.Const
import com.example.fl0wer.R
import com.example.fl0wer.androidApp.ui.core.navigation.Screens
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.android.ModoRender
import com.github.terrakok.modo.android.init
import com.github.terrakok.modo.android.saveState
import com.github.terrakok.modo.back
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    @Inject
    lateinit var modo: Modo
    private val modoRender by lazy { ModoRender(this, R.id.fragments_container) }
    private val viewModel: MainViewModel by viewModels()

    private val readContactsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            handleIntent(intent)
        } else {
            showPermissionRationaleDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        modo.init(savedInstanceState, Screens.ContactList())
        if (savedInstanceState == null) {
            readContactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        modo.saveState(outState)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        modo.render = modoRender
    }

    override fun onPause() {
        modo.render = null
        super.onPause()
    }

    override fun onBackPressed() {
        modo.back()
    }

    private fun handleIntent(intent: Intent) {
        val contactId = intent.getStringExtra(Const.NOTICE_BIRTHDAY_EXTRA_CONTACT_ID) ?: return
        viewModel.openContact(contactId)
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
}
