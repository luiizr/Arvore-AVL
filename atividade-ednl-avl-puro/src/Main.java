
import java.util.Scanner;

import avl.ArvoreAVL;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArvoreAVL arvore = new ArvoreAVL();
        boolean executando = true;

        while (executando) {
            imprimirMenu();
            int opcao = lerInteiro(scanner, "Escolha uma opcao: ");

            switch (opcao) {
                case 1:
                    inserirNo(scanner, arvore);
                    break;
                case 2:
                    removerNo(scanner, arvore);
                    break;
                case 3:
                    buscarNo(scanner, arvore);
                    break;
                case 4:
                    mostrarArvore(arvore);
                    break;
                case 5:
                    mostrarEmOrdem(arvore);
                    break;
                case 0:
                    executando = false;
                    System.out.println("Encerrando o programa.");
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        }

        scanner.close();
    }

    private static void imprimirMenu() {
        System.out.println();
        System.out.println("========== ARVORE AVL ==========");
        System.out.println("1 - Incluir no");
        System.out.println("2 - Remover no");
        System.out.println("3 - Buscar no");
        System.out.println("4 - Mostrar arvore");
        System.out.println("5 - Mostrar em ordem");
        System.out.println("0 - Sair");
    }

    private static void inserirNo(Scanner scanner, ArvoreAVL arvore) {
        int chave = lerInteiro(scanner, "Digite a chave para inserir: ");
        if (arvore.inserir(chave)) {
            System.out.println("Chave " + chave + " inserida com sucesso.");
        } else {
            System.out.println("A chave " + chave + " ja existe na arvore.");
        }
    }

    private static void removerNo(Scanner scanner, ArvoreAVL arvore) {
        int chave = lerInteiro(scanner, "Digite a chave para remover: ");
        if (arvore.remover(chave)) {
            System.out.println("Chave " + chave + " removida com sucesso.");
        } else {
            System.out.println("A chave " + chave + " nao foi encontrada.");
        }
    }

    private static void buscarNo(Scanner scanner, ArvoreAVL arvore) {
        int chave = lerInteiro(scanner, "Digite a chave para buscar: ");
        if (arvore.contem(chave)) {
            System.out.println("A chave " + chave + " esta presente na arvore.");
        } else {
            System.out.println("A chave " + chave + " nao esta na arvore.");
        }
    }

    private static void mostrarArvore(ArvoreAVL arvore) {
        System.out.println();
        System.out.println("Arvore atual:");
        arvore.mostrar();
        System.out.println("Total de nos: " + arvore.tamanho());
        System.out.println("Altura da arvore: " + arvore.altura());
    }

    private static void mostrarEmOrdem(ArvoreAVL arvore) {
        if (arvore.estaVazia()) {
            System.out.println("Arvore vazia.");
            return;
        }

        System.out.println("Em ordem: " + arvore.emOrdem());
    }

    private static int lerInteiro(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro valido.");
            }
        }
    }
}
