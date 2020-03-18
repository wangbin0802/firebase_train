package com.stareme.firebase

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.stareme.firebase.firestore.NearBy
import com.stareme.firebase.firestore.User

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private lateinit var nameView: EditText
    private lateinit var ageView: EditText
    private lateinit var saveBtn: Button
    private lateinit var fetchBtn: Button

    private val mDocRef = FirebaseFirestore.getInstance().collection("michat").document("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        nameView = view.findViewById(R.id.name)
        ageView = view.findViewById(R.id.age)
        saveBtn = view.findViewById(R.id.save_btn)
        fetchBtn = view.findViewById(R.id.fetch_btn)

        val desTv: TextView = view.findViewById(R.id.textview_first)

        saveBtn.setOnClickListener {
            mDocRef.set(User(nameView.text.toString(), ageView.text.toString().toInt()))
                .addOnSuccessListener {
                    Log.d("FirstFragment", "save success......")
                }
                .addOnFailureListener {
                    Log.d("FirstFragment", "save failed......$it")
                }
        }

        val docRef = FirebaseFirestore.getInstance().collection("michat").document("nearby")
        fetchBtn.setOnClickListener {
            docRef.get().addOnSuccessListener {
                if (it.exists()) {
                    val result = it.toObject(NearBy::class.java)
                    Log.d("FirstFragment", "fetch success......$result")
                    desTv.text = "fetch result:$result"
                }
            }

                .addOnFailureListener {
                    Log.d("FirstFragment", "fetch failed......$it")
                }
        }
    }
}
