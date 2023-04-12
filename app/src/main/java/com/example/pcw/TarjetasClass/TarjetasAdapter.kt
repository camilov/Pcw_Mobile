package com.example.pcw.TarjetasClass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pcw.ClienteItemResponse
import com.example.pcw.ClienteSendResponse
import com.example.pcw.DataResponse.TarjetasItemResponse
import com.example.pcw.R

class TarjetasAdapter(
    var clienteList:List<ClienteItemResponse> = emptyList(),
    private val onItemSelected: (ClienteSendResponse) -> Unit
): RecyclerView.Adapter<TarjetasViewHolder>() {


    fun updateList(clienteList: List<ClienteItemResponse>) {
        this.clienteList = clienteList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarjetasViewHolder {
        return TarjetasViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cliente_tarjeta,parent,false)
        )
    }

    override fun onBindViewHolder(holder: TarjetasViewHolder, position: Int) {
        val item = clienteList[position]
        holder.bind(item,onItemSelected)
    }

    override fun getItemCount(): Int =clienteList.size


}