package br.edu.infnet.anotacoes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.infnet.anotacoes.databinding.ActivitySignUpBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        binding.buttonSignUp.setOnClickListener {
            val email = binding.editTextEmail.editText?.text.toString()
            val senha = binding.editTextPassword.editText?.text.toString()
            val confirmaSenha = binding.editTextConfirmPassword.editText?.text.toString()

            if(senha != confirmaSenha) {
                Toast.makeText(this, "Senha e confirmar senha não são iguais", Toast.LENGTH_SHORT).show()
            }
            if(email.isEmpty() || senha.isEmpty() || confirmaSenha.isEmpty()) {
                Toast.makeText(this, "Todos os campos precisam estar preenchidos", Toast.LENGTH_SHORT).show()
            }else {
                auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener {
                    if(it.isSuccessful) {
                        startActivity(Intent(this, SignInActivity::class.java))
                        finish()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Não foi possível criar um usuário", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}