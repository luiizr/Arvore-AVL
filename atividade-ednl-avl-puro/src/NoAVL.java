
final class NoAVL {
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
