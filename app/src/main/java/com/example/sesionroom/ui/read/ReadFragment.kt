package com.example.sesionroom.ui.read

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sesionroom.R
import com.example.sesionroom.SesionROOM
import com.example.sesionroom.model.local.DeudorDAO
import com.example.sesionroom.model.remote.DeudorRemote
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_read.*

class ReadFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_read, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BT_buscar.setOnClickListener {
            val nombre = ET_nombre.text.toString()

            //buscarEnRoom(nombre)

            buscarEnFirebase(nombre)
        }
    }

    private fun buscarEnFirebase(nombre: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("deudores")
        var deudorExiste = false

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val deudor = datasnapshot.getValue(DeudorRemote::class.java)
                    if (deudor?.nombre == nombre) {
                        deudorExiste = true
                        mostrarDeudor(deudor.nombre, deudor.telefono, deudor.cantidad)
                    }
                }
                if (!deudorExiste) {
                    Toast.makeText(requireContext(), "Deudor no existe", Toast.LENGTH_SHORT).show()
                }
            }
        }
        myRef.addValueEventListener(postListener)
    }

    private fun mostrarDeudor(
        nombre: String,
        telefono: String,
        cantidad: Long
    ) {
        TV_resultado.text =
            "Nombre: $nombre\n" +
                    "Telefono: $telefono\n" +
                    "Cantidad: $cantidad"
    }

    private fun buscarEnRoom(nombre: String) {
        val deudorDAO: DeudorDAO = SesionROOM.database.DeudorDAO()

        val deudor = deudorDAO.buscarDeudor(nombre)

        if (deudor != null) {
            mostrarDeudor(deudor.nombre, deudor.telefono, deudor.cantidad)
        } else {
            Toast.makeText(context, "Deudor no existe", Toast.LENGTH_SHORT).show()
        }
    }
}