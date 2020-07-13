package com.example.sesionroom.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sesionroom.R
import com.example.sesionroom.SesionROOM
import com.example.sesionroom.model.local.Deudor
import com.example.sesionroom.model.local.DeudorDAO
import com.example.sesionroom.model.remote.DeudorRemote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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

        mostrarMensajeBienvenida()



        BT_guardar.setOnClickListener {
            val nombre = ET_nombre.text.toString()
            val telefono = ET_telefono.text.toString()
            val cantidad = ET_cantidad.text.toString().toLong()

            guardarEnRoom(nombre, telefono, cantidad)

            guardarEnFireBase(nombre, telefono, cantidad)

            ET_nombre.setText("")
            ET_telefono.setText("")
            ET_cantidad.setText("")
        }
    }

    private fun mostrarMensajeBienvenida() {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

        val user: FirebaseUser? = mAuth.currentUser
        val correo = user?.email

        Toast.makeText(requireContext(), "Bienvenido usuario:$correo", Toast.LENGTH_SHORT).show()
    }

    private fun guardarEnFireBase(nombre: String, telefono: String, cantidad: Long) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("deudores")
        val id: String? = myRef.push().key
        val deudor = DeudorRemote(
            id,
            nombre,
            telefono,
            cantidad
        )
        myRef.child(id!!).setValue(deudor)
    }

    private fun guardarEnRoom(
        nombre: String,
        telefono: String,
        cantidad: Long
    ) {
        val deudor = Deudor(
            NULL,
            nombre,
            telefono,
            cantidad
        )
        val deudorDAO: DeudorDAO = SesionROOM.database.DeudorDAO()

        deudorDAO.crearDeudor(deudor)
    }
}