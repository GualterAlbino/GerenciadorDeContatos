

import Controller.CTRLContato;
import Visao.TelaPrincipal;

public class GerenciadorDeContatos {

    public static void main(String[] args) {
        //Instancia a tela
        var Tela = new TelaPrincipal();

        //Define ela ao centro
        Tela.setLocationRelativeTo(null);

        //Define o titulo da janela
        Tela.setTitle("Agenda de Contatos");

        // Impedir o redimensionamento do JFrame
        Tela.setResizable(false);

        Tela.setVisible(true);
        
        
        String[] dados = new String[]{
            "1c1649a4-dfe9-435a-a375-bd4606b73068",
            "Saulo",
            "Marcos",
            "Joao",
            "Saulo",
            "Saulo",
            "Saulo",
            "Saulo",
            "Saulo",
            "Saulo"
        };
        
        CTRLContato.salvar(dados);

    }

}
