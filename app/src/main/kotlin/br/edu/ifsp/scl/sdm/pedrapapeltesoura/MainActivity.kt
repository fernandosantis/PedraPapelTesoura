package br.edu.ifsp.scl.sdm.pedrapapeltesoura

import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.sdm.pedrapapeltesoura.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity(), View.OnClickListener {
    // Binding
    private var binding: ActivityMainBinding? = null
    var qtdOponentes = 1
    var corP = R.color.design_default_color_primary

    // Integer corPV = R.color.design_default_color_primary_variant;
    var escolha: Int = 0

    // Valores: Pedra=0, Papel=1, Tesoura=2
    private var maos = arrayOf(R.drawable.img_pedra, R.drawable.img_papel, R.drawable.img_tesoura)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflando o Layout
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        limpaCampos()

        // Switch - QtdOponentes
        binding!!.swtQtdOponentes.isChecked = false
        binding!!.swtQtdOponentes.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isChecked) {
                binding!!.lblUmOponente.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                binding!!.lblDoisOponentes.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                qtdOponentes = 2
            } else {
                binding!!.lblUmOponente.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                binding!!.lblDoisOponentes.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                qtdOponentes = 1
            }
            limpaCampos()
        }
        binding!!.imbtPedra.setOnClickListener(this)
        binding!!.imbtPapel.setOnClickListener(this)
        binding!!.imbtTesoura.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imbtPedra -> escolha = 0
            R.id.imbtPapel -> escolha = 1
            R.id.imbtTesoura -> escolha = 2
        }
        binding!!.imgJogador.setImageResource(maos[escolha])
        // Checa Qtd de Oponentes
        if (qtdOponentes == 1) {
            jogar1Op()
        } else {
            jogar2Op()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    fun jogar1Op() {
        val random = Random(System.currentTimeMillis())
        val escolhaOp1 = random.nextInt(3)

        // Ajusta visualização dos Elementos
        binding!!.imgOponente1.setImageResource(maos[escolhaOp1])
        binding!!.llResultadoOponentes.visibility = View.VISIBLE
        binding!!.imgOponente1.rotation = 180f
        binding!!.imgOponente2.visibility = View.GONE
        binding!!.llResultadosJogador.visibility = View.VISIBLE
        binding!!.lblResultado.text = resultado1(escolha, escolhaOp1)
        binding!!.lblResultado.visibility = View.VISIBLE
    }

    fun jogar2Op() {
        val random = Random(System.currentTimeMillis())
        val escolhaOp1 = random.nextInt(3)
        val escolhaOp2 = random.nextInt(3)
        binding!!.imgOponente1.setImageResource(maos[escolhaOp1])
        binding!!.imgOponente2.setImageResource(maos[escolhaOp2])
        binding!!.llResultadoOponentes.visibility = View.VISIBLE
        binding!!.imgOponente1.rotation = 135f
        binding!!.imgOponente2.rotation = 225f
        binding!!.llResultadosJogador.visibility = View.VISIBLE
        binding!!.lblResultado.text = resultado2(escolha, escolhaOp1, escolhaOp2)
        binding!!.lblResultado.visibility = View.VISIBLE
    }

    private fun compara(v1: Int, v2: Int): Int {
        return if (v1 === v2) {
            0 // Empatou
        } else if (v1 - v2 == -2 || v1 - v2 == 1) {
            1 // Venceu
        } else {
            -1 // Perdeu
        }
    }

    private fun resultado1(v1: Int, v2: Int): String {
        return when (compara(v1, v2)) {
            1 -> "Você venceu seu oponente!"
            -1 -> "Você perdeu de seu oponente!"
            else -> "Você e seu oponente empataram!"
        }
    }

    private fun resultado2(v1: Int, v2: Int, v3: Int): String {
        val v1v2: Int = compara(v1, v2) * 3
        val v1v3: Int = compara(v1, v3) * 7
        return when (v1v2 + v1v3) {
            0 -> "Empate: Todos Jogaram igual."
            7 -> "Você e o Oponente 1 diviram o prêmio de vencedor."
            -7 -> "Você empatou com o Oponente 1 mas perderam do Oponente 2, que foi o vencedor!"
            3 -> "Você Ganhou do Oponente 1 e dividiu o prêmio com o Oponente 2."
            10 -> "Parabéns! Você venceu a todos os oponentes"
            -3 -> "O Oponente 1 venceu! Você e o Oponente 2 empataram."
            -10 -> "Você levou uma lavada e perdeu de ambos."
            else -> "Todos jogaram diferente. Então não houve vencedores."
        }
    }

    fun limpaCampos() {
        binding!!.llResultadoOponentes.visibility = View.INVISIBLE
        binding!!.imgOponente2.visibility = View.VISIBLE
        binding!!.llResultadosJogador.visibility = View.INVISIBLE
        binding!!.lblResultado.visibility = View.INVISIBLE
        binding!!.lblResultado.text = ""
        binding!!.lblOponentes.setTextColor(getColor(corP))
        binding!!.imbtPedra.setBackgroundColor(getColor(corP))
        binding!!.imbtPapel.setBackgroundColor(getColor(corP))
        binding!!.imbtTesoura.setBackgroundColor(getColor(corP))
    }
}
