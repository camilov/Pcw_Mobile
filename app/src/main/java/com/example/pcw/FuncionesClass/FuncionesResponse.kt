package com.example.pcw.FuncionesClass

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

class FuncionesResponse {


    companion object{
        const val CREATE_ID        = 0
        const val VALOR_PRESTADO   = 0
        const val NUM_CUOTA        = 0
        const val CREATE_ESTADO_ID = 1
        const val CLOSE_ESTADO_ID  = 1
        const val INTERES          = 0
        const val UNO              = 1
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentDate():String{

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val fechaActual = Date()
        val fechaFormateada = dateFormat.format(fechaActual)

        return fechaFormateada

    }

}