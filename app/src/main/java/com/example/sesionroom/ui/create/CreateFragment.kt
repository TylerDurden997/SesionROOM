package com.example.sesionroom.ui.create

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_create.*
import java.io.ByteArrayOutputStream
import java.sql.Types.NULL

class CreateFragment : Fragment() {

    private val REQUEST_IMAGE_CAPTURE = 1234

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mostrarMensajeBienvenida()

        IV_foto.setOnClickListener {
            dispatchTakePictureIntent()
        }

        BT_guardar.setOnClickListener {
            val nombre = ET_nombre.text.toString()
            val telefono = ET_telefono.text.toString()
            val cantidad = ET_cantidad.text.toString().toLong()

            //guardarEnRoom(nombre, telefono, cantidad)

            guardarEnFireBase(nombre, telefono, cantidad)

            ET_nombre.setText("")
            ET_telefono.setText("")
            ET_cantidad.setText("")
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            IV_foto.setImageBitmap(imageBitmap)
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

        val mStorage = FirebaseStorage.getInstance()
        val photoRef = mStorage.reference.child(id!!)
        var urlPhoto = ""

        // Get the data from an ImageView as bytes
        IV_foto.isDrawingCacheEnabled = true
        IV_foto.buildDrawingCache()
        val bitmap = (IV_foto.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = photoRef.putBytes(data)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            photoRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                urlPhoto = task.result.toString()

                val deudor = DeudorRemote(
                    id,
                    nombre,
                    telefono,
                    cantidad,
                    urlPhoto
                )
                myRef.child(id).setValue(deudor)
            } else {
                // Handle failures
                // ...
            }
        }

/*        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }*/


/*        val deudor = DeudorRemote(
            id,
            nombre,
            telefono,
            cantidad,
            urlPhoto
        )
        myRef.child(id!!).setValue(deudor)*/
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