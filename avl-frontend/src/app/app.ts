import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

type TreeAction = 'add' | 'remove';

interface AvlNode {
  value: number;
  height: number;
  left: AvlNode | null;
  right: AvlNode | null;
}

@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly treeTypes = ['AVL'];
  protected selectedTreeType = 'AVL';
  protected selectedAction: TreeAction = 'add';
  protected valueInput = '';
  protected numbers = [30, 20, 40, 10, 25, 35, 50];
  protected tree: AvlNode | null = null;
  protected feedback =
    'Escolha AVL, informe um numero e use Adicionar ou Remover para atualizar o grafico.';

  public constructor() {
    this.tree = this.buildTree(this.numbers);
  }

  protected setAction(action: TreeAction): void {
    this.selectedAction = action;
  }

  protected submitValue(): void {
    const trimmedValue = this.valueInput.trim();
    const parsedValue = Number(trimmedValue);

    if (trimmedValue === '' || !Number.isInteger(parsedValue)) {
      this.feedback = 'Informe um numero inteiro valido para atualizar a arvore.';
      return;
    }

    if (this.selectedAction === 'add') {
      this.addValue(parsedValue);
    } else {
      this.removeValue(parsedValue);
    }

    this.valueInput = '';
  }

  private addValue(value: number): void {
    if (this.numbers.includes(value)) {
      this.feedback = `O numero ${value} ja esta na arvore AVL.`;
      return;
    }

    this.tree = this.insertNode(this.tree, value);
    this.numbers = [...this.numbers, value];
    this.feedback = `Numero ${value} adicionado com sucesso na arvore AVL.`;
  }

  private removeValue(value: number): void {
    if (!this.numbers.includes(value)) {
      this.feedback = `Nao foi possivel remover ${value} porque ele nao esta na arvore.`;
      return;
    }

    this.tree = this.removeNode(this.tree, value);
    this.numbers = this.numbers.filter((currentValue) => currentValue !== value);
    this.feedback = `Numero ${value} removido com sucesso da arvore AVL.`;
  }

  private buildTree(values: number[]): AvlNode | null {
    let root: AvlNode | null = null;

    for (const value of values) {
      root = this.insertNode(root, value);
    }

    return root;
  }

  private insertNode(node: AvlNode | null, value: number): AvlNode {
    if (!node) {
      return this.createNode(value);
    }

    if (value < node.value) {
      node.left = this.insertNode(node.left, value);
    } else if (value > node.value) {
      node.right = this.insertNode(node.right, value);
    } else {
      return node;
    }

    return this.rebalance(node);
  }

  private removeNode(node: AvlNode | null, value: number): AvlNode | null {
    if (!node) {
      return null;
    }

    if (value < node.value) {
      node.left = this.removeNode(node.left, value);
    } else if (value > node.value) {
      node.right = this.removeNode(node.right, value);
    } else {
      if (!node.left && !node.right) {
        return null;
      }

      if (!node.left) {
        return node.right;
      }

      if (!node.right) {
        return node.left;
      }

      const nextNode = this.findSmallest(node.right);
      node.value = nextNode.value;
      node.right = this.removeNode(node.right, nextNode.value);
    }

    return this.rebalance(node);
  }

  private rebalance(node: AvlNode): AvlNode {
    this.updateHeight(node);

    const balance = this.getBalance(node);

    if (balance > 1) {
      if (this.getBalance(node.left) < 0 && node.left) {
        node.left = this.rotateLeft(node.left);
      }

      return this.rotateRight(node);
    }

    if (balance < -1) {
      if (this.getBalance(node.right) > 0 && node.right) {
        node.right = this.rotateRight(node.right);
      }

      return this.rotateLeft(node);
    }

    return node;
  }

  private rotateLeft(node: AvlNode): AvlNode {
    const newRoot = node.right;

    if (!newRoot) {
      return node;
    }

    node.right = newRoot.left;
    newRoot.left = node;

    this.updateHeight(node);
    this.updateHeight(newRoot);

    return newRoot;
  }

  private rotateRight(node: AvlNode): AvlNode {
    const newRoot = node.left;

    if (!newRoot) {
      return node;
    }

    node.left = newRoot.right;
    newRoot.right = node;

    this.updateHeight(node);
    this.updateHeight(newRoot);

    return newRoot;
  }

  private findSmallest(node: AvlNode): AvlNode {
    let current = node;

    while (current.left) {
      current = current.left;
    }

    return current;
  }

  private updateHeight(node: AvlNode): void {
    node.height = Math.max(this.getHeight(node.left), this.getHeight(node.right)) + 1;
  }

  private getHeight(node: AvlNode | null): number {
    return node?.height ?? 0;
  }

  private getBalance(node: AvlNode | null): number {
    if (!node) {
      return 0;
    }

    return this.getHeight(node.left) - this.getHeight(node.right);
  }

  private createNode(value: number): AvlNode {
    return {
      value,
      height: 1,
      left: null,
      right: null
    };
  }
}
