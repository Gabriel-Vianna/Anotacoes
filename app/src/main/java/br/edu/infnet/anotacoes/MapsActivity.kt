package br.edu.infnet.anotacoes

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import android.widget.Toast
import br.edu.infnet.anotacoes.databinding.ActivityMapsBinding
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.File
import java.io.FileOutputStream
import java.util.*

class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding

    @SuppressLint("WrongConstant", "ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        val latitude = extras?.get("lat")
        val longitude = extras?.get("long")

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = sdf.format(Date())

        data.setText(currentDate, TextView.BufferType.EDITABLE);
        texto.setText("Latitude: ${latitude}  - Longitude: ${longitude}")

        binding.salvar.setOnClickListener {
            var title = titulo.text.toString()
            var text = texto.text.toString()
            val file = File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "${currentDate}_${title}.txt")
            val fos = FileOutputStream(file)
            fos.write(text.toByteArray())
            fos.close()
            Toast.makeText(applicationContext, "Anotação salva com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }
}