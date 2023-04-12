package com.example.pcw.DataResponse

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TarjetasDataResponse(
    @SerializedName("Lista") val lista: List<TarjetasItemResponse>
)

data class TarjetasItemResponse(
    @SerializedName("idTarjeta")     val idTarjeta:     Int  ,
    @SerializedName("idCliente")     val idCliente:     Int  ,
    @SerializedName("valorPrestado") val valorPrestado: Float,
    @SerializedName("valorTotal")    val valorTotal:    Float,
    @SerializedName("numCuotas")     val numCuotas:     Int  ,
    @SerializedName("idEstado")      val idEstado:      Int  ,
    @SerializedName("interes")       val interes:       Float,
    @SerializedName("valorDefecto")  val valorDefecto:  Int  ,
    @SerializedName("fechaPrestamo") val fechaPrestamo: Date
)