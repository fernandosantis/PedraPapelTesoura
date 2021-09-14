package br.edu.ifsp.scl.sdm.pedrapapeltesoura

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.sdm.pedrapapeltesoura.databinding.ActivityOpcoesBinding

class OpcoesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOpcoesBinding
    private var players = 1
    private var jogadas = 1
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
        binding.swtQtdOponentes.isChecked = false
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

        // Switch Rodadas
        binding.swtQtdJogadas.isChecked = false
        binding.swtQtdJogadas.setOnCheckedChangeListener { optJoga, isChecked ->
            if (optJoga.isChecked) {
                binding.lblTresJogada.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                binding.lblCincoJogadas.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                jogadas = 5
            } else {
                binding.lblTresJogada.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                binding.lblCincoJogadas.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                jogadas = 3
            }
        }

        binding.btnSalvar.setOnClickListener {
            val retornoIntent: Intent = Intent()
            with(binding) {
                retornoIntent.putExtra(MainActivity.PLAYERS, players.toString())
                retornoIntent.putExtra(MainActivity.JOGADAS, jogadas.toString())
            }
            setResult(RESULT_OK, retornoIntent)
            finish()
        }
    }
}
