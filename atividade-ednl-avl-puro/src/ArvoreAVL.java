import java.util.ArrayList;
import java.util.List;

public class ArvoreAVL {
    private static final int ESQUERDA = 1;
    private static final int DIREITA = -1;

    private static final class NoAVL {
        int chave;
        int altura;
        int fatorBalanceamento;
        NoAVL esquerda;
        NoAVL direita;

        NoAVL(int chave) {
            this.chave = chave;
            this.altura = 1;
            this.fatorBalanceamento = 0;
        }
    }

    private NoAVL raiz;
    private int tamanho;
    private boolean alterado;

    public boolean inserir(int chave) {
        alterado = false;
        raiz = inserir(raiz, chave);
        if (alterado) {
            tamanho++;
        }
        return alterado;
    }

    public boolean remover(int chave) {
        alterado = false;
        raiz = remover(raiz, chave);
        if (alterado) {
            tamanho--;
        }
        return alterado;
    }

    public boolean contem(int chave) {
        NoAVL atual = raiz;
        while (atual != null) {
            if (chave < atual.chave) {
                atual = atual.esquerda;
            } else if (chave > atual.chave) {
                atual = atual.direita;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean estaVazia() {
        return raiz == null;
    }

    public int tamanho() {
        return tamanho;
    }

    public int altura() {
        return altura(raiz);
    }

    public void mostrar() {
        if (raiz == null) {
            System.out.println("Arvore vazia.");
            return;
        }

        int altura = altura();
        int larguraCelula = Math.max(maiorRotulo(raiz) + 2, 8);
        List<NoAVL> nivelAtual = new ArrayList<NoAVL>();
        nivelAtual.add(raiz);

        for (int nivel = 1; nivel <= altura; nivel++) {
            int unidadesAntes = potenciaDeDois(altura - nivel) - 1;
            int unidadesEntre = potenciaDeDois(altura - nivel + 1) - 1;
            StringBuilder linha = new StringBuilder();

            linha.append(repetir(" ", unidadesAntes * larguraCelula));

            List<NoAVL> proximoNivel = new ArrayList<NoAVL>();
            for (int i = 0; i < nivelAtual.size(); i++) {
                NoAVL no = nivelAtual.get(i);
                linha.append(centralizar(no == null ? "" : formatarNo(no), larguraCelula));

                if (i < nivelAtual.size() - 1) {
                    linha.append(repetir(" ", Math.max(unidadesEntre * larguraCelula, 1)));
                }

                if (no == null) {
                    proximoNivel.add(null);
                    proximoNivel.add(null);
                } else {
                    proximoNivel.add(no.esquerda);
                    proximoNivel.add(no.direita);
                }
            }

            System.out.println(removerEspacosFinais(linha.toString()));
            nivelAtual = proximoNivel;
        }
    }

    private NoAVL inserir(NoAVL no, int chave) {
        if (no == null) {
            alterado = true;
            return new NoAVL(chave);
        }

        if (chave < no.chave) {
            no.esquerda = inserir(no.esquerda, chave);
        } else if (chave > no.chave) {
            no.direita = inserir(no.direita, chave);
        } else {
            return no;
        }

        return balancear(no);
    }

    private NoAVL remover(NoAVL no, int chave) {
        if (no == null) {
            return null;
        }

        if (chave < no.chave) {
            no.esquerda = remover(no.esquerda, chave);
        } else if (chave > no.chave) {
            no.direita = remover(no.direita, chave);
        } else {
            alterado = true;

            if (no.esquerda == null || no.direita == null) {
                return no.esquerda != null ? no.esquerda : no.direita;
            }

            NoAVL sucessor = menorNo(no.direita);
            no.chave = sucessor.chave;
            no.direita = remover(no.direita, sucessor.chave);
        }

        return balancear(no);
    }

    private NoAVL balancear(NoAVL no) {
        atualizarNo(no);

        if (no.fatorBalanceamento > 1) {
            if (fatorBalanceamento(no.esquerda) < 0) {
                no.esquerda = rotacionar(no.esquerda, ESQUERDA);
            }
            return rotacionar(no, DIREITA);
        }

        if (no.fatorBalanceamento < -1) {
            if (fatorBalanceamento(no.direita) > 0) {
                no.direita = rotacionar(no.direita, DIREITA);
            }
            return rotacionar(no, ESQUERDA);
        }

        return no;
    }

    private NoAVL rotacionar(NoAVL no, int direcao) {
        NoAVL novoTopo = direcao == ESQUERDA ? no.direita : no.esquerda;
        NoAVL subArvoreIntermediaria = direcao == ESQUERDA ? novoTopo.esquerda : novoTopo.direita;

        if (direcao == ESQUERDA) {
            novoTopo.esquerda = no;
            no.direita = subArvoreIntermediaria;
        } else {
            novoTopo.direita = no;
            no.esquerda = subArvoreIntermediaria;
        }

        atualizarNo(no);
        atualizarNo(novoTopo);
        return novoTopo;
    }

    private NoAVL menorNo(NoAVL no) {
        while (no.esquerda != null) {
            no = no.esquerda;
        }
        return no;
    }

    private void atualizarNo(NoAVL no) {
        if (no == null) {
            return;
        }
        no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita));
        no.fatorBalanceamento = altura(no.esquerda) - altura(no.direita);
    }

    private int altura(NoAVL no) {
        return no == null ? 0 : no.altura;
    }

    private int fatorBalanceamento(NoAVL no) {
        return no == null ? 0 : no.fatorBalanceamento;
    }

    private int maiorRotulo(NoAVL no) {
        if (no == null) {
            return 0;
        }

        int comprimentoAtual = formatarNo(no).length();
        int maiorEsquerda = maiorRotulo(no.esquerda);
        int maiorDireita = maiorRotulo(no.direita);
        return Math.max(comprimentoAtual, Math.max(maiorEsquerda, maiorDireita));
    }

    private String formatarNo(NoAVL no) {
        return no.chave + " [" + no.fatorBalanceamento + "]";
    }

    private int potenciaDeDois(int expoente) {
        return 1 << Math.max(expoente, 0);
    }

    private String centralizar(String texto, int largura) {
        if (texto.length() >= largura) {
            return texto;
        }

        int espacosTotais = largura - texto.length();
        int espacosEsquerda = espacosTotais / 2;
        int espacosDireita = espacosTotais - espacosEsquerda;
        return repetir(" ", espacosEsquerda) + texto + repetir(" ", espacosDireita);
    }

    private String repetir(String trecho, int vezes) {
        if (vezes <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(trecho.length() * vezes);
        for (int i = 0; i < vezes; i++) {
            sb.append(trecho);
        }
        return sb.toString();
    }

    private String removerEspacosFinais(String texto) {
        int fim = texto.length();
        while (fim > 0 && Character.isWhitespace(texto.charAt(fim - 1))) {
            fim--;
        }
        return texto.substring(0, fim);
    }
}
