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

    private var jogadas = 1
    private var players = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opcoes)
        binding = ActivityOpcoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ActionBar
        supportActionBar?.title = "Opções"
        binding.lblOponentes.setTextColor(getColor(corP))

        // Switch Players
        jogadas = MainActivity.JOGADAS
        players = MainActivity.PLAYERS
        binding.swtQtdOponentes.isChecked = players  != 1

        binding.swtQtdOponentes.setOnCheckedChangeListener { optPlay, _ ->
            if (optPlay.isChecked) {
                binding.lblUmOponente.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                binding.lblDoisOponentes.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                players = 2
            } else {
                binding.lblUmOponente.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                binding.lblDoisOponentes.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                players = 1
            }
        }
        binding.sldJogadas.value = JOGADAS.toFloat()
        // Switch Rodadas
        binding.sldJogadas.addOnChangeListener(
            Slider.OnChangeListener { _, value, _ ->
                jogadas = value.toInt()
            }
        )

        binding.btnSalvar.setOnClickListener {
            val retornoIntent: Intent = Intent()
            with(binding) {
                retornoIntent.putExtra("PLAYERS", players)

                retornoIntent.putExtra("JOGADAS", jogadas)
            }
            setResult(RESULT_OK, retornoIntent)
            finish()
        }

    }
}
