package br.edu.ifsp.scl.sdm.pedrapapeltesoura

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.sdm.pedrapapeltesoura.MainActivity.Companion.JOGADAS
import br.edu.ifsp.scl.sdm.pedrapapeltesoura.databinding.ActivityOpcoesBinding
import com.google.android.material.slider.Slider

class OpcoesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOpcoesBinding
    private var corP = R.color.design_default_color_primary

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opcoes)
        binding = ActivityOpcoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ActionBar
        supportActionBar?.title = "Opções"
        binding.lblOponentes.setTextColor(getColor(corP))

        // Switch Players

        binding.swtQtdOponentes.isChecked = MainActivity.PLAYERS != 1

        binding.swtQtdOponentes.setOnCheckedChangeListener { optPlay, _ ->
            if (optPlay.isChecked) {
                binding.lblUmOponente.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                binding.lblDoisOponentes.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                MainActivity.PLAYERS = 2
            } else {
                binding.lblUmOponente.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                binding.lblDoisOponentes.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                MainActivity.PLAYERS = 1
            }
        }
        binding.sldJogadas.value = JOGADAS.toFloat()
        // Switch Rodadas
        binding.sldJogadas.addOnChangeListener(
            Slider.OnChangeListener { _, value, _ ->
                MainActivity.JOGADAS = value.toInt()
            }
        )

        binding.btnSalvar.setOnClickListener {
            val retornoIntent: Intent = Intent()
            with(binding) {
                retornoIntent.putExtra("PLAYERS", MainActivity.PLAYERS)

                retornoIntent.putExtra("JOGADAS", MainActivity.JOGADAS)
            }
            setResult(RESULT_OK, retornoIntent)
            finish()
        }
    }
}
