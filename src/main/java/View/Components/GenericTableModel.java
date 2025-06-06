package View.Components;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.function.Function;

/**
 * Um TableModel genérico para evitar repetição de código.
 * Consegue exibir qualquer tipo de lista de objetos em uma JTable.
 * @param <T> O tipo do objeto a ser exibido na tabela.
 */
public class GenericTableModel<T> extends AbstractTableModel {

    private final List<String> columnNames;
    private final Function<T, Object[]> rowMapper;
    private List<T> items;

    public GenericTableModel(List<String> columnNames, Function<T, Object[]> rowMapper) {
        this.columnNames = columnNames;
        this.rowMapper = rowMapper;
        this.items = List.of(); // Inicia com lista vazia
    }

    public void setItems(List<T> items) {
        this.items = items;
        fireTableDataChanged(); // Notifica a JTable que os dados mudaram
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        T item = items.get(rowIndex);
        return rowMapper.apply(item)[columnIndex];
    }
}