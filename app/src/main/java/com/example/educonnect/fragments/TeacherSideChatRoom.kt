package com.example.educonnect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.educonnect.R
import com.example.educonnect.adapter.MessageAdapter
import com.example.educonnect.dataclass.Message
import com.example.educonnect.dataclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TeacherSideChatRoom : Fragment() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList:ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    var receiverRoom:String?=null
    var senderRoom:String?=null

    companion object {
        private const val ARG_STUDENT_NAME = "student_name"
        private const val ARG_STUDENT_EMAIL = "student_email"
        private const val ARG_STUDENT_SUBJECT = "student_subject"
        private const val ARG_STUDENT_ROLE = "student_role"
        private const val ARG_STUDENT_UID = "student_uid"

        fun newInstance(student: User): TeacherSideChatRoom {
            val fragment = TeacherSideChatRoom()
            val args = Bundle()
            args.putString(ARG_STUDENT_NAME, student.name)
            args.putString(ARG_STUDENT_EMAIL,student.email)
            args.putString(ARG_STUDENT_SUBJECT,student.subject)
            args.putString(ARG_STUDENT_ROLE,student.role)
            args.putString(ARG_STUDENT_UID,student.uid)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_teacher_side_chat_room, container, false)

        val name=arguments?.getString(ARG_STUDENT_NAME)
        val receiverUid=arguments?.getString(ARG_STUDENT_UID)

        val senderUid= FirebaseAuth.getInstance().currentUser?.uid
        mDbRef= FirebaseDatabase.getInstance().getReference()

        senderRoom=receiverUid+senderUid
        receiverRoom= senderUid+receiverUid

        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            show()
            title = name
        }


        chatRecyclerView=view.findViewById(R.id.chatRecyclerView)
        messageBox=view.findViewById(R.id.messageBox)
        sendButton=view.findViewById(R.id.sentButton)
        messageList=ArrayList()
        messageAdapter=MessageAdapter(messageList)

        chatRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        chatRecyclerView.adapter=messageAdapter


        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children){
                        val message=postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })


        sendButton.setOnClickListener{
            val message=messageBox.text.toString()
            if (message.isBlank()) {
                Toast.makeText(context, "Message cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val messageObject= senderUid?.let { it1 -> Message(it1,message) }

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")
        }


        return view
    }

}