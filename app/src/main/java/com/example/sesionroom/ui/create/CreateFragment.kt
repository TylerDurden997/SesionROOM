package com.example.sesionroom.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sesionroom.R
import com.example.sesionroom.SesionROOM
import com.example.sesionroom.model.Deudor
import com.example.sesionroom.model.DeudorDAO
import kotlinx.android.synthetic.main.fragment_create.*
import java.sql.Types.NULL

class CreateFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BT_guardar.setOnClickListener {
            val nombre = ET_nombre.text.toString()
            val telefono = ET_telefono.text.toString()
            val cantidad = ET_cantidad.text.toString().toLong()

            val deudor = Deudor(NULL,nombre,telefono,cantidad)

            val deudorDAO : DeudorDAO = SesionROOM.database.DeudorDAO()

            deudorDAO.crearDeudor(deudor)

            ET_nombre.setText("")
            ET_telefono.setText("")
            ET_cantidad.setText("")
        }
    }
}