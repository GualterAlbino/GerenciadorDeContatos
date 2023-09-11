package gerenciadordecontatos;

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

    }

}
