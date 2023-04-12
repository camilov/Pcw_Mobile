package com.example.pcw.DataResponse


import com.google.gson.annotations.SerializedName
import java.util.*

data class AbonosDataResponse(
    @SerializedName("Lista") val Lista: List<AbonoItemResponse>
)

data class AbonoItemResponse(

    @SerializedName("idAbono")    val idAbono:    Number,
    @SerializedName("idTarjeta")  val idTarjeta:  Number,
    @SerializedName("numCuota")   val numCuota:   Number,
    @SerializedName("valorAbono") val valorAbono: Float ,
    @SerializedName("fechaAbono")  val fechaAbono: Date
)