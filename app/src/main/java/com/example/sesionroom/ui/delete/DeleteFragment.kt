package com.example.sesionroom.ui.delete

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sesionroom.R
import com.example.sesionroom.SesionROOM
import com.example.sesionroom.model.remote.DeudorRemote
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_delete.*


class DeleteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_delete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BT_eliminar.setOnClickListener {
            val nombre = ET_nombre.text.toString()

            //borrarRoom(nombre)
            borrarFirebase(nombre)
        }

    }

    private fun borrarFirebase(nombre: String) {
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
                        val alertDialog: AlertDialog? = activity?.let {
                            val builder = AlertDialog.Builder(it)
                            builder.apply {
                                setMessage("Desea eliminar el Deudor: $nombre")
                                setPositiveButton("Aceptar") { dialog, id ->
                                    myRef.child(deudor.id!!).removeValue()
                                }
                                setNegativeButton("Cancelar") { dialog, id ->

                                }
                            }
                            builder.create()
                        }
                        alertDialog?.show()
                    }
                }
                if (!deudorExiste) {
                    Toast.makeText(requireContext(), "Deudor no existe", Toast.LENGTH_SHORT).show()
                }
            }
        }
        myRef.addValueEventListener(postListener)
    }

    private fun borrarRoom(nombre: String) {
        val deudorDAO = SesionROOM.database.DeudorDAO()
        val deudor = deudorDAO.buscarDeudor(nombre)

        if (deudor != null) {
            deudorDAO.borrarDeudor(deudor)
            ET_nombre.setText("")
            Toast.makeText(context, "Deudor eliminado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Deudor no existe", Toast.LENGTH_SHORT).show()
        }
    }


}