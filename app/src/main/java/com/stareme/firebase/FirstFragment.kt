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
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.*
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
//            mDocRef.set(User(nameView.text.toString(), ageView.text.toString().toInt()))
//                .addOnSuccessListener {
//                    Log.d("FirstFragment", "save success......")
//                    Toast.makeText(context, "save success", Toast.LENGTH_LONG).show()
//                }
//                .addOnFailureListener {
//                    Log.d("FirstFragment", "save failed......$it")
//                    Toast.makeText(context, "save failed", Toast.LENGTH_LONG).show()
//                }
            mDocRef.update("age", FieldValue.increment(10))
        }

        val docRef = FirebaseFirestore.getInstance().collection("michat").document("nearby")
        fetchBtn.setOnClickListener {
            docRef.get().addOnSuccessListener {
                if (it.exists()) {
                    val result = it.toObject(NearBy::class.java)
                    Log.d("FirstFragment", "fetch success......$result")
                    desTv.text = "fetch result:$result"
                    Toast.makeText(context, "fetch success", Toast.LENGTH_LONG).show()
                }
            }
                .addOnFailureListener {
                    Log.d("FirstFragment", "fetch failed......$it")
                    Toast.makeText(context, "fetch failed", Toast.LENGTH_LONG).show()
                }
        }

        // 监听某一document的变化
        docRef.addSnapshotListener(MetadataChanges.EXCLUDE, object : EventListener<DocumentSnapshot>{
            override fun onEvent(documentSnapshot: DocumentSnapshot?, exception: FirebaseFirestoreException?) {
                val result = documentSnapshot?.toObject(NearBy::class.java)
                Log.d("FirstFragment", "onEvent listener result:$result")
                desTv.text = "fetch result:$result"
            }

        })

        // 查询方式获得结果
        FirebaseFirestore.getInstance().collection("michat")
            .whereEqualTo("alias", "test").get()
            .addOnSuccessListener {
                Log.d("FirstFragment", "query success result:$it")
                it.forEach { queryDocumentSnapshot ->
                    Log.d("FirstFragment", "query success result:$queryDocumentSnapshot")
                }
            }
            .addOnFailureListener {
                Log.d("FirstFragment", "query failed result:$it")
            }

        // 设置文档
        FirebaseFirestore.getInstance().collection("michat2").document("test")
            .set(User("lady", 18))
    }
}
