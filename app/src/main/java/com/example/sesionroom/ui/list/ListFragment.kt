package com.example.sesionroom.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sesionroom.R
import com.example.sesionroom.model.remote.DeudorRemote
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment() {

    private val deudoresList: MutableList<DeudorRemote> = mutableListOf()

    private lateinit var deudoresAdapter: DeudoresRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        cargarDeudores()

        RV_deudores.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )
        RV_deudores.setHasFixedSize(true)

        deudoresAdapter = DeudoresRVAdapter(deudoresList as ArrayList<DeudorRemote>)

        RV_deudores.adapter = deudoresAdapter
    }

    private fun cargarDeudores() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("deudores")

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children) {
                    val deudor = datasnapshot.getValue(DeudorRemote::class.java)
                    deudoresList.add(deudor!!)
                }
                deudoresAdapter.notifyDataSetChanged()
            }
        }
        myRef.addValueEventListener(postListener)
    }

}