
import java.util.ArrayList;
import java.util.List;

public class ArvoreAVL {
    private NoAVL raiz;
    private int tamanho;

    public boolean inserir(int chave) {
        boolean[] inserido = new boolean[1];
        raiz = inserir(raiz, chave, inserido);
        if (inserido[0]) {
            tamanho++;
        }
        return inserido[0];
    }

    public boolean remover(int chave) {
        boolean[] removido = new boolean[1];
        raiz = remover(raiz, chave, removido);
        if (removido[0]) {
            tamanho--;
        }
        return removido[0];
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

    public String emOrdem() {
        StringBuilder sb = new StringBuilder();
        emOrdem(raiz, sb);
        return sb.toString();
    }

    private NoAVL inserir(NoAVL no, int chave, boolean[] inserido) {
        if (no == null) {
            inserido[0] = true;
            return new NoAVL(chave);
        }

        if (chave < no.chave) {
            no.esquerda = inserir(no.esquerda, chave, inserido);
        } else if (chave > no.chave) {
            no.direita = inserir(no.direita, chave, inserido);
        } else {
            return no;
        }

        atualizarNo(no);
        return rebalancear(no);
    }

    private NoAVL remover(NoAVL no, int chave, boolean[] removido) {
        if (no == null) {
            return null;
        }

        if (chave < no.chave) {
            no.esquerda = remover(no.esquerda, chave, removido);
        } else if (chave > no.chave) {
            no.direita = remover(no.direita, chave, removido);
        } else {
            removido[0] = true;

            if (no.esquerda == null) {
                return no.direita;
            }

            if (no.direita == null) {
                return no.esquerda;
            }

            NoAVL sucessor = menorNo(no.direita);
            no.chave = sucessor.chave;
            no.direita = remover(no.direita, sucessor.chave, new boolean[1]);
        }

        atualizarNo(no);
        return rebalancear(no);
    }

    private NoAVL menorNo(NoAVL no) {
        NoAVL atual = no;
        while (atual.esquerda != null) {
            atual = atual.esquerda;
        }
        return atual;
    }

    private NoAVL rebalancear(NoAVL no) {
        if (no.fatorBalanceamento > 1) {
            if (fatorBalanceamento(no.esquerda) < 0) {
                no.esquerda = rotacaoEsquerda(no.esquerda);
            }
            return rotacaoDireita(no);
        }

        if (no.fatorBalanceamento < -1) {
            if (fatorBalanceamento(no.direita) > 0) {
                no.direita = rotacaoDireita(no.direita);
            }
            return rotacaoEsquerda(no);
        }

        return no;
    }

    private NoAVL rotacaoEsquerda(NoAVL b) {
        NoAVL a = b.direita;
        NoAVL subArvoreIntermediaria = a.esquerda;

        a.esquerda = b;
        b.direita = subArvoreIntermediaria;

        // Fórmulas do PDF para atualizar o FB sem recalcular a subárvore inteira.
        int fbOriginalB = b.fatorBalanceamento;
        int fbOriginalA = a.fatorBalanceamento;
        b.fatorBalanceamento = fbOriginalB + 1 - Math.min(fbOriginalA, 0);
        a.fatorBalanceamento = fbOriginalA + 1 + Math.max(b.fatorBalanceamento, 0);

        atualizarAltura(b);
        atualizarAltura(a);
        return a;
    }

    private NoAVL rotacaoDireita(NoAVL b) {
        NoAVL a = b.esquerda;
        NoAVL subArvoreIntermediaria = a.direita;

        a.direita = b;
        b.esquerda = subArvoreIntermediaria;

        // Fórmulas do PDF para atualizar o FB sem recalcular a subárvore inteira.
        int fbOriginalB = b.fatorBalanceamento;
        int fbOriginalA = a.fatorBalanceamento;
        b.fatorBalanceamento = fbOriginalB - 1 - Math.max(fbOriginalA, 0);
        a.fatorBalanceamento = fbOriginalA - 1 + Math.min(b.fatorBalanceamento, 0);

        atualizarAltura(b);
        atualizarAltura(a);
        return a;
    }

    private void atualizarNo(NoAVL no) {
        atualizarAltura(no);
        no.fatorBalanceamento = altura(no.esquerda) - altura(no.direita);
    }

    private void atualizarAltura(NoAVL no) {
        no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita));
    }

    private int altura(NoAVL no) {
        return no == null ? 0 : no.altura;
    }

    private int fatorBalanceamento(NoAVL no) {
        return no == null ? 0 : no.fatorBalanceamento;
    }

    private void emOrdem(NoAVL no, StringBuilder sb) {
        if (no == null) {
            return;
        }

        emOrdem(no.esquerda, sb);
        if (sb.length() > 0) {
            sb.append(' ');
        }
        sb.append(formatarNo(no));
        emOrdem(no.direita, sb);
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
