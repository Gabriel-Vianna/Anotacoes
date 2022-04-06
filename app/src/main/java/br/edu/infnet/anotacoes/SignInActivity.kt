package br.edu.infnet.anotacoes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.infnet.anotacoes.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*

const val RC_SIGN_IN = 123

class SignInActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignInBinding
    companion object {
        lateinit var auth: FirebaseAuth;
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if(account != null) {
            goToHomeActivity()
        }
        binding.signInButton.visibility = View.VISIBLE

        binding.signInButton.setOnClickListener{
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val senha = binding.editTextPassword.text.toString()

            if(email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Todos os campos precisam estar preenchidos", Toast.LENGTH_SHORT).show()
            }else {
                auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener {
                    if(it.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Não foi possível realizar login no App", Toast.LENGTH_LONG).show()
                }
            }
        }

        btnSignUp.setOnClickListener {
            goToSignUpActivity()
        }
    }

    private fun goToSignUpActivity() {
        var signUpActivity = Intent(this, SignUpActivity::class.java)
        startActivity(signUpActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null) {
            goToHomeActivity()
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            goToHomeActivity()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
            alert("Não foi possível realizar o login")
        }
    }

    private fun alert(stringAlert: String) {
        Toast.makeText(this, stringAlert, Toast.LENGTH_LONG).show()
    }

    private fun goToHomeActivity() {
        var servicosActivity = Intent(this, MainActivity::class.java)
        startActivity(servicosActivity)
        finish()
    }
}