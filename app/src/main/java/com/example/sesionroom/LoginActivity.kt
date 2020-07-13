package com.example.sesionroom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()

        val user = mAuth.currentUser
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        BT_login_registro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        BT_login_accerder.setOnClickListener {

            val email = ET_login_correo.text.toString()
            val password = ET_login_contrasena.text.toString()

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))

                    } else {

                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                    }
                }
        }
    }
}