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
    @SerializedName("fechaAbono")  val fechaAbono: String
)


data class AbonoSendResponse(

    val idAbonoo:   Number,
    val idTarjeta:  Number,
    val numCuota:   Number,
    val valorAbono: Float ,
    val fechaAbono: String
)

data class AbonoModifyResponse(
    val numCuota:   Number,
    val valorAbono: Float
)



data class AbonoSendModifyResponse(
    val idAbono:   Number,
    val idTarjeta:  Number,
    val numCuota:   Number,
    val valorAbono: Float ,
    val fechaAbono: String
)

data class AbonoDeleteResponse(
    val response: String
)

data class AbonoMovementResponse(
   val idMovimiento : Number,
   val entrada      : Float?,
   val salida       : Float,
   val tipMvto      : String,
   val idTarjeta    : Number,
   val idCliente    : Number,
   val fecMvto      : String,
   val mcaAjuste    : Number

)

data class AbonoModifyTarjeta(
    val valorTotal : Float?,
    val numCuotas: Number,
    val fecActu: String

)



data class AbonoRequestData(
    val abonoData: AbonoItemResponse,
    val movimientoData: AbonoMovementResponse,
    val tarjetaData: AbonoModifyTarjeta
)

data class AbonoModifyRequestData(

    val abonoData: AbonoModifyResponse,
    val tarjetaData:AbonoModifyTarjeta,
    val movimientoData: AbonoMovementResponse


)