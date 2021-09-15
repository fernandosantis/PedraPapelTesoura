package br.edu.ifsp.scl.sdm.pedrapapeltesoura

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.sdm.pedrapapeltesoura.databinding.ActivityMainBinding
import kotlin.random.Random
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), View.OnClickListener {
    // Binding
    private lateinit var binding: ActivityMainBinding
    private lateinit var opcoesActivityResultLauncher: ActivityResultLauncher<Intent>
    private var corP = R.color.design_default_color_primary

    // Integer corPV = R.color.design_default_color_primary_variant;
    private var escolha: Int = 0

    // Valores: Pedra=0, Papel=1, Tesoura=2
    private var maos = arrayOf(R.drawable.img_pedra, R.drawable.img_papel, R.drawable.img_tesoura)

    private var p0 = 0
    private var p1 = 0
    private var p2 = 0

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
        binding.btnJogar.setOnClickListener(this)

/*
        savedInstanceState?.getString(PLAYERS).takeIf { it != null }.apply { PLAYERS = this.toString() }
        savedInstanceState?.getString(JOGADAS).takeIf { it != null }.apply { JOGADAS = this.toString() }
*/

        opcoesActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
                if (resultado?.resultCode == RESULT_OK) {
                    with(resultado) {
                        data?.getIntExtra("PLAYERS", 1).takeIf { it != null }
                            .let { PLAYERS = it ?: 1 }
                        data?.getIntExtra("JOGADAS", 1).takeIf { it != null }
                            .let { JOGADAS = it ?: 1 }
                    }
                }
                limpaCampos()
            }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imbtPedra -> escolha = 0
            R.id.imbtPapel -> escolha = 1
            R.id.imbtTesoura -> escolha = 2
            R.id.btn_jogar -> {
                view.visibility = View.INVISIBLE
                binding.llJogar.visibility = View.VISIBLE
                limpaCampos()
                return
            }
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
                opcoesIntent.putExtra("PLAYERS", PLAYERS)
                opcoesIntent.putExtra("JOGADAS", JOGADAS)
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
        placar()
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
        placar()
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
        when (compara(v1, v2)) {
            1 -> {
                p0++
                return "Você venceu o outro jogador!"
            }
            -1 -> {
                p1++
                return "Você perdeu de seu oponente!"
            }
            else -> return "Vocês Empataram!"
        }
    }

    private fun resultado2(v1: Int, v2: Int, v3: Int): String {
        val v1v2: Int = compara(v1, v2) * 3
        val v1v3: Int = compara(v1, v3) * 7
        when (v1v2 + v1v3) {
            0 -> {
                return "Empate: Todos Jogaram igual."
            }
            7 -> {
                p0++
                p1++
                return "Você e o Jogador 1 diviram o prêmio de vencedor."
            }
            -7 -> {
                p2++
                p2++
                return "Você empatou com o Jogador 1 mas perderam do Jogador 2, que foi o vencedor!"
            }
            3 -> {
                p0++
                p2++
                return "Você Ganhou do Jogador 1 e dividiu o prêmio com o Jogador 2."
            }
            10 -> {
                p0++
                p0++
                return "Parabéns! Você venceu a todos os oponentes"
            }
            -3 -> {
                p1++
                p1++
                return "O Jogador 1 venceu! Você e o Jogador 2 empataram."
            }
            -10 -> {
                p1++
                p2++
                return "Você levou uma lavada e perdeu de ambos."
            }
            else -> {
                return "Todos jogaram diferente. Então não houve vencedores."
            }
        }
    }

    private fun limpaCampos() {

        binding.llResultadoOponentes.visibility = View.INVISIBLE
        binding.imgOponente2.visibility = View.VISIBLE
        binding.llResultadosJogador.visibility = View.INVISIBLE
        binding.imbtPedra.setBackgroundColor(getColor(corP))
        binding.imbtPapel.setBackgroundColor(getColor(corP))
        binding.imbtTesoura.setBackgroundColor(getColor(corP))
        zera()
        placar()
    }

    private fun placar() {
        when (PLAYERS) {
            1 -> {
                binding.lblJogadas.text = "Jogo contra 1 jogador: Vence quem fizer $JOGADAS pontos"
                if (p0 == JOGADAS) {
                    binding.lblPontos.text = "Você Venceu"
                    zera(2)
                } else if (p1 == JOGADAS) {
                    binding.lblPontos.text = "Jogador 1 Venceu"
                    zera(2)
                } else {
                    binding.lblPontos.text = "$p0 x $p1"
                }
            }
            2 -> {
                binding.lblJogadas.text = "Jogo contra 2 jogadores: Vence quem fizer ${JOGADAS * 2} pontos"
                if (p0 >= JOGADAS * 2) {
                    binding.lblPontos.text = "Você Venceu"
                    zera(2)
                } else if (p1 >= JOGADAS * 2) {
                    binding.lblPontos.text = "Jogador 1 Venceu"
                    zera(2)
                } else if (p2 >= JOGADAS * 2) {
                    binding.lblPontos.text = "Jogador 2 Venceu"
                    zera(2)
                } else {
                    binding.lblPontos.text = "$p0 x $p1 x $p2"
                }
            }
            else -> {
                binding.lblPontos.text = ""
            }
        }
    }
    private fun zera(jgar: Int = 0) {
        p0 = 0
        p1 = 0
        p2 = 0
        binding.lblResultado.text = ""
        if (jgar> 0) {
            binding.llJogar.visibility = View.GONE
            binding.btnJogar.visibility = View.VISIBLE
        }

    }
}
