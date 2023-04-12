package com.example.pcw.TarjetasClass

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.pcw.DataResponse.TarjetasItemResponse
import com.example.pcw.databinding.ItemTarjetaClienteBinding

class TarjetasClienteViewHolder(view: View): RecyclerView.ViewHolder(view)  {
    private val binding = ItemTarjetaClienteBinding.bind(view)

    fun bind(item: TarjetasItemResponse,onItemSelected: (TarjetasItemResponse) -> Unit) {
        binding.tvTarjetaClienteEstado.text = item.idEstado.toString()
        binding.tvTarjetaClienteValorPrestado.text = item.valorPrestado.toString()
        binding.root.setOnClickListener { onItemSelected(TarjetasItemResponse(
                                                                              item.idTarjeta,
                                                                              item.idCliente,
                                                                              item.valorPrestado,
                                                                              item.valorTotal,
                                                                              item.numCuotas,
                                                                              item.idEstado ,
                                                                              item.interes,
                                                                              item.valorDefecto,
                                                                              item.fechaPrestamo))}
    }
}