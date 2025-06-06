package Model.Repository;

import Model.Pessoa;
import Model.Vacina;
import Model.Vacinacao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class VacinacaoRepository {

    private static VacinacaoRepository instance;
    private final List<Pessoa> pessoas = new ArrayList<>();
    private final List<Vacina> vacinas = new ArrayList<>();
    private final List<Vacinacao> vacinacoes = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    private VacinacaoRepository() {
        // A lista de pessoas agora começa vazia. O usuário irá cadastrá-las.
        // Mantemos as vacinas predefinidas para simplificar o escopo.
        addVacina(new Vacina("Coronavac", "Sinovac"));
        addVacina(new Vacina("Pfizer-BioNTech", "Pfizer"));
        addVacina(new Vacina("AstraZeneca", "Oxford"));
        addVacina(new Vacina("Janssen", "Johnson & Johnson"));
    }

    public static synchronized VacinacaoRepository getInstance() {
        if (instance == null) {
            instance = new VacinacaoRepository();
        }
        return instance;
    }

    // --- Métodos CRUD para Pessoas ---
    public void savePessoa(Pessoa pessoa) {
        if (pessoa.getId() == null) {
            // Criar nova pessoa
            pessoa.setId(idCounter.incrementAndGet());
            pessoas.add(pessoa);
        } else {
            // Atualizar pessoa existente
            Optional<Pessoa> existente = findPessoaById(pessoa.getId());
            existente.ifPresent(p -> {
                p.setNome(pessoa.getNome());
                p.setCpf(pessoa.getCpf());
                p.setDataNascimento(pessoa.getDataNascimento());
            });
        }
    }

    public void deletePessoa(Long id) {
        pessoas.removeIf(p -> p.getId().equals(id));
    }

    public Optional<Pessoa> findPessoaById(Long id) {
        return pessoas.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public List<Pessoa> getAllPessoas() {
        return new ArrayList<>(pessoas);
    }

    // --- Métodos para Vacinas (sem alterações) ---
    public void addVacina(Vacina v) {
        v.setId(idCounter.incrementAndGet());
        vacinas.add(v);
    }

    public List<Vacina> getAllVacinas() {
        return new ArrayList<>(vacinas);
    }

    // --- Métodos CRUD para Vacinação ---
    public void addVacinacao(Vacinacao vc) {
        vc.setId(idCounter.incrementAndGet());
        vacinacoes.add(vc);
    }

    public void deleteVacinacao(Long id) {
        vacinacoes.removeIf(v -> v.getId().equals(id));
    }

    public List<Vacinacao> getAllVacinacoes() {
        return new ArrayList<>(vacinacoes);
    }
}