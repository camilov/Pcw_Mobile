package com.example.pcw.TarjetasClass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pcw.ClienteItemResponse
import com.example.pcw.ClienteSendResponse
import com.example.pcw.DataResponse.TarjetasItemResponse
import com.example.pcw.R

class TarjetasClienteAdapter(

    var tarjetasList:List<TarjetasItemResponse> = emptyList(),
    private val onItemSelected: (TarjetasItemResponse) -> Unit
):RecyclerView.Adapter<TarjetasClienteViewHolder>() {

    fun updateList(tarjetasList: List<TarjetasItemResponse>) {
        this.tarjetasList = tarjetasList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarjetasClienteViewHolder {
        return TarjetasClienteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_tarjeta_cliente,parent,false)
        )
    }

    override fun onBindViewHolder(holder: TarjetasClienteViewHolder, position: Int) {
        val item = tarjetasList[position]
        holder.bind(item,onItemSelected)
    }

    override fun getItemCount(): Int = tarjetasList.size
}