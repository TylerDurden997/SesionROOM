package com.example.sesionroom.ui.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sesionroom.R
import com.example.sesionroom.SesionROOM
import com.example.sesionroom.model.Deudor
import com.example.sesionroom.model.DeudorDAO
import kotlinx.android.synthetic.main.fragment_update.*

class UpdateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_update, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ET_telefono.visibility = View.GONE
        ET_cantidad.visibility = View.GONE
        BT_actualizar.visibility = View.GONE

        var idDeudor = 0
        val deudorDAO : DeudorDAO = SesionROOM.database.DeudorDAO()


        BT_buscar.setOnClickListener {
            val nombre = ET_nombre.text.toString()
            val deudor = deudorDAO.buscarDeudor(nombre)

            if(deudor != null){
                idDeudor = deudor.Id
                ET_telefono.visibility = View.VISIBLE
                ET_cantidad.visibility = View.VISIBLE
                ET_telefono.setText(deudor.telefono)
                ET_cantidad.setText(deudor.cantidad.toString())
                BT_buscar.visibility = View.GONE
                BT_actualizar.visibility = View.VISIBLE
            }
        }
        BT_actualizar.setOnClickListener {
            val deudor = Deudor(
                idDeudor,
                ET_nombre.text.toString(),
                ET_telefono.text.toString(),
                ET_cantidad.text.toString().toLong()
            )
            deudorDAO.actualizarDeudor(deudor)
            ET_telefono.visibility = View.GONE
            ET_cantidad.visibility = View.GONE
            BT_buscar.visibility = View.VISIBLE
            BT_actualizar.visibility = View.GONE

        }
    }
}