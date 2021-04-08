package com.example.fl0wer.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fl0wer.CiceroneHolder
import com.example.fl0wer.Const
import com.example.fl0wer.R
import com.github.terrakok.cicerone.androidx.AppNavigator

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    private val readContactsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startNavigation()
            handleIntent(intent)
        } else {
            showPermissionRationaleDialog()
        }
    }
    private val navigator = AppNavigator(this, R.id.fragments_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            readContactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        CiceroneHolder.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        CiceroneHolder.navigatorHolder.removeNavigator()
        super.onPause()
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
