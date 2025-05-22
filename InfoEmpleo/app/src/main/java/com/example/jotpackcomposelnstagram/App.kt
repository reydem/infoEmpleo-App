package com.example.jotpackcomposelnstagram

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // Aquí va TODO lo que antes estuviera en TodoApp y JetpackComposeApp.
        // Por ejemplo, si en JetpackComposeApp hacías:
        //    configurarLogger()
        //    iniciarAnalíticas()
        //
        // y en TodoApp:
        //    inicializarBaseDeDatos()
        //    registrarWorkManager()
        //
        // pásalo todo dentro de este método, manteniendo la inicialización clara y
        // en un único lugar.
    }
}