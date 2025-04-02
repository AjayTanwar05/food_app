package com.ajaytanwar.foodapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ajaytanwar.foodapp.databinding.ActivitySignupBinding
import com.ajaytanwar.foodapp.moodle.UserMoodle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class signupActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var username: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient


    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        //initialize Firebase Auth
        auth = Firebase.auth


        // initialize Firebase database
        database = Firebase.database.reference

        // initialize Firebase-database
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)


        binding.createbutton.setOnClickListener {
            username = binding.Username.text.toString()
            email = binding.UserEmail.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (email.isBlank() || password.isBlank() || username.isBlank()) {
                Toast.makeText(this, "please Fill all detail", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }
        }

        binding.loginpage.setOnClickListener {
            val intent = Intent(this, loginactivity  ::class.java)
            startActivity(intent)
        }

        binding.googlebutton2.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }

    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            Toast.makeText(this, "Successfully sign in with google", Toast.LENGTH_SHORT).show()
                            finish()

                        } else {
                            Toast.makeText(this, "Sign In Failed🥲", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else {
                Toast.makeText(this, "Sign In Failed🥲", Toast.LENGTH_SHORT).show()
            }
        }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "account is create successfully", Toast.LENGTH_SHORT).show()
                saveUserData()

                startActivity(Intent(this, loginactivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "account Creation is failed", Toast.LENGTH_SHORT).show()
                Log.d("account", "create account : Failure", task.exception)
            }

        }
    }

    private fun saveUserData() {
        // retrieve data from impute filed
        username = binding.Username.text.toString()
        password = binding.password.text.toString()
        email = binding.UserEmail.text.toString()


        val user = UserMoodle(username, email, password)
        val userId :String = FirebaseAuth.getInstance().currentUser!!.uid

        //save data to Firebase
        database.child("user").child(userId).setValue(user)
    }
}