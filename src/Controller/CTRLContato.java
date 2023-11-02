package Controller;

import DAO.DAOContato;
import Modelo.ContatoClass;
import java.util.ArrayList;
import java.util.UUID;

public class CTRLContato {

    public static void salvar(String[] dados) {
        String codigo = dados[3];

        ContatoClass contato = new ContatoClass();
        contato.arrayTo(dados);

        DAOContato dao = new DAOContato();

        if (codigo.equals("")) {
            contato.setCodigo(UUID.randomUUID().toString());
            dao.inserir(contato);
        } else {
            dao.editar(contato);
        }
    }

    public static void deletar(String codigo) {
        DAOContato dao = new DAOContato();

        if (codigo.equals("")) {
            System.out.println("Codigo para exclusão não informado!");
        } else {
            dao.excluir(codigo);
        }

    }

    public static String[] recuperar(String codigo) {

        DAOContato dao = new DAOContato();
        ContatoClass objContato = dao.recuperar(codigo);
        return objContato.toArray();

    }

    public static String[][] recuperarTodos() {
        DAOContato dao = new DAOContato();
        ArrayList<ContatoClass> listaContatos = dao.recuperarTodos();

        String[][] data = new String[listaContatos.size()][];

        for (int i = 0; i < listaContatos.size(); i++) {
            ContatoClass contato = listaContatos.get(i);
            data[i] = new String[]{
                contato.getNome(),
                contato.getTipoContato(),
                String.valueOf(contato.getIsFavorito()), // Converte o valor booleano para string
                contato.getCodigo()
            };
        }

        return data;
    }

}
