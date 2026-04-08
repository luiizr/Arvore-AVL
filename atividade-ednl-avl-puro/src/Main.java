
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Scanner para leitura da entrada 
        Scanner scanner = new Scanner(System.in);
        // Importando a classe ArvoreAVL, para poder criar a árvore junto das coisas da main
        ArvoreAVL arvore = new ArvoreAVL();
        // Variável para controlar se o usuário ainda está utilizando o sistema de menu ou não
        boolean executando = true;

        System.out.println("Atividade EDNL - Árvore AVL - Coleguinha");
        while (executando) {
            imprimirMenu();
            int opcao = lerInteiro(scanner, "Escolha uma opcao: ");

            switch (opcao) {
                case 1:
                    // Vai abrir a função e pedir o número para inserir
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
        System.out.println("--- Menu ---");
        System.out.println("1 - Adicionar nó");
        System.out.println("2 - Remover nó");
        System.out.println("3 - Buscar nó");
        System.out.println("4 - Mostrar arvore");
        System.out.println("0 - Sair");
    }

    private static void inserirNo(Scanner scanner, ArvoreAVL arvore) {
        // Número que ele quer inserir
        int chave = lerInteiro(scanner, "Digite a chave para inserir: ");
        // if porque na função a gente verifica se a chave já existe ou não, para não permitir chaves duplicadas
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

    // Função para garantir que, enquanto o usuário não digitar um número inteiro válido, ele continue pedindo para o usuário digitar um número inteiro válido
    private static int lerInteiro(Scanner scanner, String mensagem) {
        while (true) {
            System.out.println(mensagem);
            String entrada = scanner.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro valido.");
            }
        }
    }
}
