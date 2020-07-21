package com.example.sesionroom.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sesionroom.R
import com.example.sesionroom.model.remote.DeudorRemote
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.deudor_item.view.*

class DeudoresRVAdapter(
    var deudoresList: ArrayList<DeudorRemote>
) : RecyclerView.Adapter<DeudoresRVAdapter.DeudoresViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeudoresViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.deudor_item, parent, false)

        return DeudoresViewHolder(itemView)
    }

    override fun getItemCount(): Int = deudoresList.size


    override fun onBindViewHolder(holder: DeudoresViewHolder, position: Int) {
        val deudor = deudoresList[position]
        holder.bindDeudor(deudor)
    }


    class DeudoresViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bindDeudor(deudor: DeudorRemote) {

            itemView.TV_nombre.text = deudor.nombre.toString()
            itemView.TV_cantidad.text = deudor.cantidad.toString()
            if (!deudor.urlPhoto.isNullOrEmpty()) {
                Picasso.get().load(deudor.urlPhoto).into(itemView.IV_foto)
            }

        }

    }


}