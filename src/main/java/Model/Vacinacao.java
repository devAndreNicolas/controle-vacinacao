package Model;
import java.time.LocalDate;

public class Vacinacao extends BaseEntity {
    private Pessoa pessoa;
    private Vacina vacina;
    private LocalDate dataAplicacao;
    private int dose;

    // Construtor
    public Vacinacao(Pessoa pessoa, Vacina vacina, LocalDate dataAplicacao, int dose) {
        this.pessoa = pessoa;
        this.vacina = vacina;
        this.dataAplicacao = dataAplicacao;
        this.dose = dose;
    }

    // Getters e Setters
    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Vacina getVacina() {
        return vacina;
    }

    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }
}