package Controller;
import Model.Pessoa;
import Model.Repository.VacinacaoRepository;
import Model.Vacina;
import Model.Vacinacao;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller responsável pela lógica de negócio relacionada à vacinação.
 * Faz a ponte entre a View (telas) e o Model (repositório de dados).
 */
public class VacinacaoController {

    private final VacinacaoRepository repository;

    public VacinacaoController() {
        this.repository = VacinacaoRepository.getInstance();
    }

    public void registrarVacinacao(Pessoa pessoa, Vacina vacina, LocalDate data, int dose) {
        if (pessoa == null || vacina == null) {
            System.err.println("Controller: Pessoa ou Vacina não pode ser nula.");
            return;
        }
        Vacinacao novaVacinacao = new Vacinacao(pessoa, vacina, data, dose);
        repository.addVacinacao(novaVacinacao);
    }

    public void excluirVacinacao(Long id) {
        repository.deleteVacinacao(id);
    }

    public List<Vacinacao> listarVacinacoes() {
        return repository.getAllVacinacoes();
    }

    public List<Pessoa> listarPessoas() {
        return repository.getAllPessoas();
    }

    public List<Vacina> listarVacinas() {
        return repository.getAllVacinas();
    }
}