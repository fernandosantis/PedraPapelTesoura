package br.edu.ifsp.scl.sdm.pedrapapeltesoura

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.sdm.pedrapapeltesoura.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity(), View.OnClickListener {
    // Binding
    private lateinit var binding: ActivityMainBinding
    private lateinit var opcoesActivityResultLauncher: ActivityResultLauncher<Intent>
    private var corP = R.color.design_default_color_primary

    // Integer corPV = R.color.design_default_color_primary_variant;
    private var escolha: Int = 0

    // Valores: Pedra=0, Papel=1, Tesoura=2
    private var maos = arrayOf(R.drawable.img_pedra, R.drawable.img_papel, R.drawable.img_tesoura)

    companion object {
        var JOGADAS = 1
        var PLAYERS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflando o Layout
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
        supportActionBar?.title = "Pedra, Papel e Tesoura"
        limpaCampos()

        // Switch - QtdOponentes

        binding.imbtPedra.setOnClickListener(this)
        binding.imbtPapel.setOnClickListener(this)
        binding.imbtTesoura.setOnClickListener(this)

/*
        savedInstanceState?.getString(PLAYERS).takeIf { it != null }.apply { PLAYERS = this.toString() }
        savedInstanceState?.getString(JOGADAS).takeIf { it != null }.apply { JOGADAS = this.toString() }
*/

        opcoesActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
                if (resultado?.resultCode == RESULT_OK) {
                    with(resultado) {
                        data?.getIntExtra("PLAYERS",1).takeIf { it != null }
                            .let { PLAYERS= it ?: 1 }
                        data?.getIntExtra("JOGADAS",1).takeIf { it != null }
                            .let { JOGADAS = it ?: 1 }
                    }
                }
                Toast.makeText(this@MainActivity, "Players $PLAYERS : Rodadas: $JOGADAS", Toast.LENGTH_SHORT).show()
                limpaCampos()
            }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imbtPedra -> escolha = 0
            R.id.imbtPapel -> escolha = 1
            R.id.imbtTesoura -> escolha = 2
        }
        binding.imgJogador.setImageResource(maos[escolha])
        // Checa Qtd de Oponentes
        if (PLAYERS == 1) {
            jogar1Op()
        } else {
            jogar2Op()
        }
    }

    // Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    // Opcao Menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.opt_opcoes -> {
                val opcoesIntent = Intent(this, OpcoesActivity::class.java)
                with(binding) {
                    opcoesIntent.putExtra("PLAYERS", PLAYERS)

                    opcoesIntent.putExtra("JOGADAS", JOGADAS)
                }
                opcoesActivityResultLauncher.launch(opcoesIntent)
                true
            }
            else -> {
                false
            }
        }
    }

    private fun jogar1Op() {
        val random = Random(System.currentTimeMillis())
        val escolhaOp1 = random.nextInt(3)

        // Ajusta visualização dos Elementos
        binding.imgOponente1.setImageResource(maos[escolhaOp1])
        binding.llResultadoOponentes.visibility = View.VISIBLE
        binding.imgOponente1.rotation = 180f
        binding.imgOponente2.visibility = View.GONE
        binding.llResultadosJogador.visibility = View.VISIBLE
        binding.lblResultado.text = resultado1(escolha, escolhaOp1)
        binding.lblResultado.visibility = View.VISIBLE
    }

    private fun jogar2Op() {
        val random = Random(System.currentTimeMillis())
        val escolhaOp1 = random.nextInt(3)
        val escolhaOp2 = random.nextInt(3)
        binding.imgOponente1.setImageResource(maos[escolhaOp1])
        binding.imgOponente2.setImageResource(maos[escolhaOp2])
        binding.llResultadoOponentes.visibility = View.VISIBLE
        binding.imgOponente1.rotation = 135f
        binding.imgOponente2.rotation = 225f
        binding.llResultadosJogador.visibility = View.VISIBLE
        binding.lblResultado.text = resultado2(escolha, escolhaOp1, escolhaOp2)
        binding.lblResultado.visibility = View.VISIBLE
    }

    private fun compara(v1: Int, v2: Int): Int {
        return if (v1 == v2) {
            0 // Empatou
        } else if (v1 - v2 == -2 || v1 - v2 == 1) {
            1 // Venceu
        } else {
            -1 // Perdeu
        }
    }

    private fun resultado1(v1: Int, v2: Int): String {
        return when (compara(v1, v2)) {
            1 -> "Você venceu o outro jogador!"
            -1 -> "Você perdeu de seu oponente!"
            else -> "Vocês Empataram!"
        }
    }

    private fun resultado2(v1: Int, v2: Int, v3: Int): String {
        val v1v2: Int = compara(v1, v2) * 3
        val v1v3: Int = compara(v1, v3) * 7
        return when (v1v2 + v1v3) {
            0 -> "Empate: Todos Jogaram igual."
            7 -> "Você e o Jogador 1 diviram o prêmio de vencedor."
            -7 -> "Você empatou com o Jogador 1 mas perderam do Jogador 2, que foi o vencedor!"
            3 -> "Você Ganhou do Jogador 1 e dividiu o prêmio com o Jogador 2."
            10 -> "Parabéns! Você venceu a todos os oponentes"
            -3 -> "O Jogador 1 venceu! Você e o Jogador 2 empataram."
            -10 -> "Você levou uma lavada e perdeu de ambos."
            else -> "Todos jogaram diferente. Então não houve vencedores."
        }
    }

    private fun limpaCampos() {
        binding.llResultadoOponentes.visibility = View.INVISIBLE
        binding.imgOponente2.visibility = View.VISIBLE
        binding.llResultadosJogador.visibility = View.INVISIBLE
        binding.lblResultado.visibility = View.INVISIBLE
        binding.lblResultado.text = ""
        binding.imbtPedra.setBackgroundColor(getColor(corP))
        binding.imbtPapel.setBackgroundColor(getColor(corP))
        binding.imbtTesoura.setBackgroundColor(getColor(corP))
    }
}
