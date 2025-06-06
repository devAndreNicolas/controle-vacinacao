package Model;
import java.util.Objects;

public class Vacina extends BaseEntity {
    private String nome;
    private String fabricante;

    // Construtor
    public Vacina(String nome, String fabricante) {
        this.nome = nome;
        this.fabricante = fabricante;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    // Sobrescrita do toString para exibição em JComboBox
    @Override
    public String toString() {
        return getNome();
    }

    // Sobrescrita de equals e hashCode para comparação correta em coleções
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacina vacina = (Vacina) o;
        return Objects.equals(getId(), vacina.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}