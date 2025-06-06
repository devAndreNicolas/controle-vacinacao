package Controller;
import Model.Pessoa;
import Model.Repository.VacinacaoRepository;

import java.time.LocalDate;
import java.util.List;

public class PessoaController {

    private final VacinacaoRepository repository;

    public PessoaController() {
        this.repository = VacinacaoRepository.getInstance();
    }

    public void salvarPessoa(Long id, String nome, String cpf, LocalDate dataNascimento) {
        Pessoa pessoa = new Pessoa(nome, cpf, dataNascimento);
        if (id != null) {
            pessoa.setId(id); // Garante que estamos atualizando a pessoa correta
        }
        repository.savePessoa(pessoa);
    }

    public void excluirPessoa(Long id) {
        // Regra de negócio: não permitir excluir pessoa se ela tiver vacinas registradas
        boolean temVacinacao = repository.getAllVacinacoes().stream()
                .anyMatch(v -> v.getPessoa().getId().equals(id));

        if (temVacinacao) {
            throw new IllegalStateException("Não é possível excluir uma pessoa que possui registros de vacinação.");
        }

        repository.deletePessoa(id);
    }

    public List<Pessoa> listarPessoas() {
        return repository.getAllPessoas();
    }
}