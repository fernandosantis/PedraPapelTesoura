package br.edu.ifsp.scl.sdm.pedrapapeltesoura;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import br.edu.ifsp.scl.sdm.pedrapapeltesoura.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Binding
    ActivityMainBinding binding;
    Integer qtdOponentes = 1;
    Integer corP = R.color.design_default_color_primary;
    //Integer corPV = R.color.design_default_color_primary_variant;
    Integer escolha;
    // Valores: Pedra=0, Papel=1, Tesoura=2

    Integer[] maos = {R.drawable.img_pedra, R.drawable.img_papel, R.drawable.img_tesoura};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflando o Layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        limpaCampos();

        // Switch - QtdOponentes
        binding.swtQtdOponentes.setChecked(false);
        binding.swtQtdOponentes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    binding.lblUmOponente.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    binding.lblDoisOponentes.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    qtdOponentes = 2;
                } else {
                    binding.lblUmOponente.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    binding.lblDoisOponentes.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    qtdOponentes = 1;
                }
                limpaCampos();
            }
        });

        binding.imbtPedra.setOnClickListener(this);
        binding.imbtPapel.setOnClickListener(this);
        binding.imbtTesoura.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Valores: Pedra=0, Papel=1, Tesoura=2
            case R.id.imbtPedra:
                escolha = 0;
                break;
            case R.id.imbtPapel:
                escolha = 1;
                break;
            case R.id.imbtTesoura:
                escolha = 2;
                break;
        }

        binding.imgJogador.setImageResource(maos[escolha]);
        // Checa Qtd de Oponentes
        if (qtdOponentes == 1) {
            jogar1Op();
        } else {
            jogar2Op();
        }
    }


    public void jogar1Op() {
        Random random = new Random(System.currentTimeMillis());
        int escolhaOp1 = random.nextInt(3);

        // Ajusta visualização dos Elementos
        binding.imgOponente1.setImageResource(maos[escolhaOp1]);
        binding.llResultadoOponentes.setVisibility(View.VISIBLE);
        binding.imgOponente1.setRotation(180);
        binding.imgOponente2.setVisibility(View.GONE);
        binding.llResultadosJogador.setVisibility(View.VISIBLE);
        binding.lblResultado.setText(resultado1(escolha, escolhaOp1));
        binding.lblResultado.setVisibility(View.VISIBLE);
    }


    public void jogar2Op() {
        Random random = new Random(System.currentTimeMillis());
        int escolhaOp1 = random.nextInt(3);
        int escolhaOp2 = random.nextInt(3);

        binding.imgOponente1.setImageResource(maos[escolhaOp1]);
        binding.imgOponente2.setImageResource(maos[escolhaOp2]);
        binding.llResultadoOponentes.setVisibility(View.VISIBLE);
        binding.imgOponente1.setRotation(135);
        binding.imgOponente2.setRotation(225);
        binding.llResultadosJogador.setVisibility(View.VISIBLE);
        binding.lblResultado.setText(resultado2(escolha, escolhaOp1, escolhaOp2));
        binding.lblResultado.setVisibility(View.VISIBLE);
    }

    private Integer compara(Integer v1, Integer v2) {
        if (v1 == v2) {
            return 0; // Empatou
        } else if ((v1 - v2 == -2) || (v1 - v2 == 1)) {
            return 1; // Venceu
        } else {
            return -1; // Perdeu
        }
    }

    private String resultado1(Integer v1, Integer v2) {
        Integer v1v2;
        v1v2 = compara(v1, v2);
        switch (v1v2) {
            case 1: return "Você venceu seu oponente!";
            case -1: return "Você perdeu de seu oponente!";
            default: return "Você e seu oponente empataram!";
        }

    }


    private String resultado2(Integer v1, Integer v2, Integer v3) {
        Integer v1v2;
        v1v2 = compara(v1, v2) * 3;

        Integer v1v3;
        v1v3 = compara(v1, v3) * 7;

        Integer soma;
        soma = v1v2 + v1v3;



        switch (soma) {
            case 0: return "Empate: Todos Jogaram igual.";
            case 7: return "Você e o Oponente 1 diviram o prêmio de vencedor.";
            case -7: return "Você empatou com o Oponente 1 mas perderam do Oponente 2, que foi o vencedor!";
            case 3: return "Você Ganhou do Oponente 1 e dividiu o prêmio com o Oponente 2.";
            case 10: return "Parabéns! Você venceu a todosos oponentes";
            case -3: return "O Oponente 1 venceu! Você e o Oponente 2 empataram.";
            case -10: return "Você levou uma lavada e perdeu de ambos.";
            default: return "Todos jogaram diferente. Então não houve vencedores.";
        }
    }


    public void limpaCampos() {

        binding.llResultadoOponentes.setVisibility(View.INVISIBLE);
        binding.imgOponente2.setVisibility(View.VISIBLE);
        binding.llResultadosJogador.setVisibility(View.INVISIBLE);
        binding.lblResultado.setVisibility(View.INVISIBLE);
        binding.lblResultado.setText("");
        binding.lblOponentes.setTextColor(getColor(corP));
        binding.imbtPedra.setBackgroundColor(getColor(corP));
        binding.imbtPapel.setBackgroundColor(getColor(corP));
        binding.imbtTesoura.setBackgroundColor(getColor(corP));
    }

}
