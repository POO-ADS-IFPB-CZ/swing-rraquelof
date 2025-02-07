package src.view;

import src.dao.ProdutoDAO;
import src.model.Produto;
import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;

public class Main {
    private final ProdutoDAO produtoDAO;

    public Main() {
        this.produtoDAO = new ProdutoDAO();
    }

    public Main(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public void menu() {
        while (true) {
            String opcao = JOptionPane.showInputDialog(null, "Escolha uma opção:\n1 - Cadastrar Produto\n2 - Listar Produtos\n3 - Editar Produto\n4 - Deletar Produto\n5 - Sair", "Menu", JOptionPane.QUESTION_MESSAGE);
            if (opcao == null || opcao.equals("5")) {
                break;
            }
            switch (opcao) {
                case "1":
                    cadastrarProduto();
                    break;
                case "2":
                    listarProdutos();
                    break;
                case "3":
                    editarProduto();
                    break;
                case "4":
                    deletarProduto();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cadastrarProduto() {
        JTextField idField = new JTextField();
        JTextField descricaoField = new JTextField();
        JTextField precoField = new JTextField();
        JTextField validadeField = new JTextField();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("ID do Produto:"));
        panel.add(idField);
        panel.add(new JLabel("Descrição do Produto:"));
        panel.add(descricaoField);
        panel.add(new JLabel("Preço do Produto:"));
        panel.add(precoField);
        panel.add(new JLabel("Data de Validade (AAAA-MM-DD):"));
        panel.add(validadeField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Cadastrar Produto", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText());
                String descricao = descricaoField.getText();
                float preco = Float.parseFloat(precoField.getText());
                LocalDate validade = LocalDate.parse(validadeField.getText());

                Produto produto = new Produto(id, descricao, preco, validade);
                boolean sucesso = produtoDAO.adicionarProduto(produto);
                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Produto já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Erro no formato dos dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Erro ao salvar o produto!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listarProdutos() {
        try {
            Set<Produto> produtos = produtoDAO.getProdutos();
            if (produtos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum produto cadastrado.");
            } else {
                StringBuilder lista = new StringBuilder("Lista de Produtos:\n");
                for (Produto p : produtos) {
                    lista.append(p).append("\n");
                }
                JOptionPane.showMessageDialog(null, lista.toString());
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar produtos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarProduto() {
        try {
            Set<Produto> produtos = produtoDAO.getProdutos();
            if (produtos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum produto cadastrado.");
                return;
            }
            int id = Integer.parseInt(JOptionPane.showInputDialog("Informe o ID do produto a ser editado:"));
            for (Produto p : produtos) {
                if (p.getId() == id) {
                    String novaDescricao = JOptionPane.showInputDialog("Nova descrição:", p.getDescricao());
                    float novoPreco = Float.parseFloat(JOptionPane.showInputDialog("Novo preço:", p.getPreco()));
                    LocalDate novaValidade = LocalDate.parse(JOptionPane.showInputDialog("Nova validade (AAAA-MM-DD):", p.getValidade().toString()));

                    Produto produtoAtualizado = new Produto(id, novaDescricao, novoPreco, novaValidade);

                    if (produtoDAO.atualizarProduto(produtoAtualizado)) {
                        JOptionPane.showMessageDialog(null, "Produto atualizado com sucesso!");
                    }
                    return;
                }else {
                    JOptionPane.showMessageDialog(null, "Produto não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao editar o produto!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarProduto() {
        try {
            Set<Produto> produtos = produtoDAO.getProdutos();
            if (produtos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum produto cadastrado.");
                return;
            }
            int id = Integer.parseInt(JOptionPane.showInputDialog("Informe o ID do produto a ser removido:"));

            if (produtoDAO.removerProduto(id)) {
                JOptionPane.showMessageDialog(null, "Produto removido com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Produto não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erro ao remover o produto!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new Main().menu();
    }
}