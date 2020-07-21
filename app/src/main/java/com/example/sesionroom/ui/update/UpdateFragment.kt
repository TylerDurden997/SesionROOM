package com.example.sesionroom.ui.update

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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_update.*

class UpdateFragment : Fragment() {

    var idDeudorFirebase: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        esconderEditText()
        var idDeudor = 0
        val deudorDAO: DeudorDAO = SesionROOM.database.DeudorDAO()

        BT_buscar.setOnClickListener {
            val nombre = ET_nombre.text.toString()

            // buscarEnRoom(deudorDAO, nombre, idDeudor)
            buscarEnFirebase(nombre)
        }
        BT_actualizar.setOnClickListener {
            //actualizarENRoom(idDeudor, deudorDAO)
            actualizarEnFirebase()
            habilitarWidgetsBuscar()
        }
    }

    private fun actualizarEnFirebase() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("deudores")

        val childUpdate = HashMap<String, Any>()
        childUpdate["nombre"] = ET_nombre.text.toString()
        childUpdate["telefono"] = ET_telefono.text.toString()
        childUpdate["cantidad"] = ET_cantidad.text.toString().toLong()
        myRef.child(idDeudorFirebase!!).updateChildren(childUpdate)
    }

    private fun actualizarENRoom(
        idDeudor: Int,
        deudorDAO: DeudorDAO
    ) {
        val deudor = Deudor(
            idDeudor,
            ET_nombre.text.toString(),
            ET_telefono.text.toString(),
            ET_cantidad.text.toString().toLong()
        )
        deudorDAO.actualizarDeudor(deudor)
    }

    private fun habilitarWidgetsBuscar() {
        ET_telefono.visibility = View.GONE
        ET_cantidad.visibility = View.GONE
        BT_buscar.visibility = View.VISIBLE
        BT_actualizar.visibility = View.GONE
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
                        habilitarWidgetsActualizar()
                        ET_telefono.setText(deudor.telefono)
                        ET_cantidad.setText(deudor.cantidad.toString())
                        idDeudorFirebase = deudor.id
                    }
                }
                if (!deudorExiste) {
                    Toast.makeText(requireContext(), "Deudor no existe", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //myRef.addValueEventListener(postListener)
        myRef.addListenerForSingleValueEvent(postListener)
    }

    private fun buscarEnRoom(
        deudorDAO: DeudorDAO,
        nombre: String,
        idDeudor: Int
    ) {
        var idDeudor1 = idDeudor
        val deudor = deudorDAO.buscarDeudor(nombre)

        if (deudor != null) {
            idDeudor1 = deudor.Id
            habilitarWidgetsActualizar()
            ET_telefono.setText(deudor.telefono)
            ET_cantidad.setText(deudor.cantidad.toString())
        }
    }


    private fun habilitarWidgetsActualizar() {
        ET_telefono.visibility = View.VISIBLE
        ET_cantidad.visibility = View.VISIBLE
        BT_buscar.visibility = View.GONE
        BT_actualizar.visibility = View.VISIBLE
    }

    private fun esconderEditText() {
        ET_telefono.visibility = View.GONE
        ET_cantidad.visibility = View.GONE
        BT_actualizar.visibility = View.GONE
    }
}