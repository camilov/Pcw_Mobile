package com.example.pcw


import com.google.gson.annotations.SerializedName

data class ClientesDataResponse(
    @SerializedName("Lista") val Lista: List<ClienteItemResponse>
)

data class ClienteItemResponse(
    @SerializedName("idCliente") val id: Number,
    @SerializedName("nombre") val name: String
)

data class ClienteSendDataResponse(
    @SerializedName("idCliente") val userId: Int?,
    @SerializedName("nombre") val name: String
)

data class ClienteSendResponse(
    val idCliente: Int,
    val nombre: String
)

data class ClienteModifyResponse(
    val nombre: String
)

data class ClienteSendModifyResponse(
    val idCliente: Int,
    val nombre: String
)
