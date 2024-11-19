package com.example.educonnect.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.educonnect.Login
import com.example.educonnect.R
import com.example.educonnect.dataclass.User
import com.example.educonnect.room.Profile
import com.example.educonnect.room.ProfileDao
import com.example.educonnect.room.ProfileDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {
    private lateinit var edtName:TextView
    private lateinit var edtEmail:TextView
    private lateinit var fabBtn: FloatingActionButton
    private lateinit var imgProfile: ImageView
    private lateinit var btnLogout: Button
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var profileDao: ProfileDao
    private val IMAGE_PICK_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        edtName = view.findViewById(R.id.username)
        edtEmail = view.findViewById(R.id.email)
        imgProfile = view.findViewById(R.id.profile_picture)
        fabBtn = view.findViewById(R.id.edit_profile_fab)
        btnLogout=view.findViewById(R.id.Logout)
        mAuth= FirebaseAuth.getInstance()
        profileDao = ProfileDatabase.getDatabase(requireContext()).profileDao()

        loadProfile()


        val senderUid = mAuth.currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference("Users")


        senderUid?.let { uid ->
            mDbRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {

                        edtName.text = user.name ?: "N/A"
                        edtEmail.text = user.email ?: "N/A"
                    } else {

                        Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                    Toast.makeText(requireContext(), "Failed to fetch user data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        fabBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_REQUEST)
        }

        btnLogout.setOnClickListener{
            mAuth.signOut()
            val intent = Intent(requireContext(), Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val uri = data.data
            if (uri != null) {
                imgProfile.setImageURI(uri)
                saveProfileWithImage(uri)
            }
        }
    }

    private fun saveProfileWithImage(uri: Uri) {
        lifecycleScope.launch {

            val bitmap = (imgProfile.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val imageByteArray = stream.toByteArray()


            val profile = Profile(
                id = 1,
                name = edtName.text.toString(),
                email = edtEmail.text.toString(),
                image = imageByteArray
            )
            profileDao.insert(profile)
            Toast.makeText(requireContext(), "Profile updated!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadProfile() {
        lifecycleScope.launch {
            val profile = profileDao.getProfileById(1)
            if (profile != null) {
                edtName.text = profile.name
                edtEmail.text = profile.email
                profile.image?.let {
                    val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                    imgProfile.setImageBitmap(bitmap)
                }
            }
        }
    }
}