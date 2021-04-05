package com.example.fl0wer

import com.github.terrakok.cicerone.Cicerone

object CiceroneHolder {
    private val cicerone = Cicerone.create()
    val router = cicerone.router
    val navigatorHolder = cicerone.getNavigatorHolder()
}
