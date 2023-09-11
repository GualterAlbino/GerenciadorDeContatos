package gerenciadordecontatos;

import Classes.ContatoClass;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gualt
 */
public class TelaPrincipal extends javax.swing.JFrame {

    private String path = "D:\\GualterAlbino\\Documents\\Faculdade\\Faculdade 6-Semestre\\Laboratorio de Progrmação - Saulo\\Estudo Dirigido 2\\GerenciadorDeContatos\\xml\\arquivo.xml";

    private boolean ContatoEditado;

    private ArrayList<ContatoClass> listaContatos;

    public TelaPrincipal() {
        initComponents();

        //==>Text Area
        //Ativa a quebra de linha
        ObservacaoTextArea.setLineWrap(true);

        //Ativa a quebra de linha EM PALAVRAS COMPLETAS
        ObservacaoTextArea.setWrapStyleWord(true);

        //Instancia o Array de Contatos
        listaContatos = new ArrayList<>();

        //Desabilita o botao de gravar no inicio
        GravarButton.setEnabled(false); // Inicializa o botao desativado

        //Desabilita todos os campos no incio
        camposEditaveis(false);

        //Ao iniciar, oculta o painel de empresa
        EmpresaPanel.setVisible(false);

        //Semaforo para indicar se ao clicar em slavar deve ser editado um contato existente ou adicionado um novo
        ContatoEditado = false;
        
        //Define a tabela como não editavel
        //Tabela.setEnabled(false);

    }

    //GravarXML
    private void GravarArquivoXML() {
        try {
            XStream st = new XStream(new DomDriver());
            st.alias("Contatos", List.class);
            st.alias("Contato", ContatoClass.class);

            File Arquivo = new File(path);

            FileOutputStream ArqXML = new FileOutputStream(Arquivo);
            ArqXML.write(st.toXML(listaContatos).getBytes());

            ArqXML.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao tentar gravar \n" + e);
        }
    }

    //Carregar XML
    private void CarregarDadosArquivoXML() throws FileNotFoundException {

        try {
            XStream st = new XStream(new DomDriver());

            //Substituir "ContatoClass" pela usa classe contato
            st.allowTypes(new Class[]{ContatoClass.class, List.class, ArrayList.class});

            st.alias("Contatos", ArrayList.class);
            st.alias("Contato", ContatoClass.class);

            BufferedReader ArqXML = new BufferedReader(new FileReader(path));

            listaContatos = (ArrayList<ContatoClass>) (st.fromXML(ArqXML));

            DefaultTableModel table = (DefaultTableModel) Tabela.getModel();

            for (var i = 0; i < listaContatos.size(); i++) {

                table.addRow(new Object[]{
                    listaContatos.get(i).getNome(),
                    listaContatos.get(i).getTipoContato(),
                    listaContatos.get(i).getIsFavorito(),
                    listaContatos.get(i).getCodigo()});
            }

            ArqXML.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao ler \n" + e);
            System.out.println(e);
        }

    }

    /*
    *Se verdadeiro, CAMPOS EDITAVEIS
    *Se falso, CAMPOS NÃO EDITAVEIS
     */
    private void camposEditaveis(boolean valor) {
        NomeInput.setEnabled(valor);
        TipoSelect.setEnabled(valor);
        FavoritosChekBox.setEnabled(valor);
        TelefoneInput.setEnabled(valor);
        CelularInput.setEnabled(valor);
        FaxInput.setEnabled(valor);
        ObservacaoTextArea.setEnabled(valor);
        NomeEmpresaInput.setEnabled(valor);
        CargoEmpresaInput.setEnabled(valor);
    }


    /*
    *Se verdadeiro, MODO EDIÇÃO
    *Se falso, MODO LEITURA
     */
    private void modo(boolean valor) {

        camposEditaveis(valor);
        //Desabilitar/Habilitar Botoes
        InserirButton.setEnabled(!valor);
        EditarButton.setEnabled(!valor);
        ExcluirButton.setEnabled(!valor);

        //Define esses botoes com valores contrarios aos demaias
        GravarButton.setEnabled(valor);
        CancelarButton.setEnabled(valor);
        
        Tabela.setEnabled(valor);

    }

    //==>Limpa os campos
    private void limparCampos() {
        NomeInput.setText("");
        TelefoneInput.setText("");
        CelularInput.setText("");
        FaxInput.setText("");
        ObservacaoTextArea.setText("");
        NomeEmpresaInput.setText("");
        CargoEmpresaInput.setText("");
    }

    //Grava um contato novo (ID diferente)
    private void GravarNovoContato() {
        var novoContato = new ContatoClass();
        novoContato.setNome(NomeInput.getText());
        novoContato.setTipoContato((String) TipoSelect.getSelectedItem());
        novoContato.setIsFavorito(FavoritosChekBox.isSelected());
        novoContato.setTelefone(TelefoneInput.getText());
        novoContato.setCelular(CelularInput.getText());
        novoContato.setFax(FaxInput.getText());
        novoContato.setObservacao(ObservacaoTextArea.getText());
        novoContato.setNomeEmpresa(NomeEmpresaInput.getText());
        novoContato.setCargoEmpresa(CargoEmpresaInput.getText());

        listaContatos.add(novoContato);

        DefaultTableModel table = (DefaultTableModel) Tabela.getModel();
        table.addRow(new Object[]{novoContato.getNome(), novoContato.getTipoContato(), novoContato.getIsFavorito(), novoContato.getCodigo()});

        //Volta para o modo leitura
        modo(false);

        //Desabilita os campos novamente
        camposEditaveis(false);
    }

    //Atualiza um contato existente
    private void GravarContatoEditado(int linha) {

        var novoContato = new ContatoClass();
        novoContato.setNome(NomeInput.getText());
        novoContato.setTipoContato((String) TipoSelect.getSelectedItem());
        novoContato.setIsFavorito(FavoritosChekBox.isSelected());
        novoContato.setTelefone(TelefoneInput.getText());
        novoContato.setCelular(CelularInput.getText());
        novoContato.setFax(FaxInput.getText());
        novoContato.setObservacao(ObservacaoTextArea.getText());
        novoContato.setNomeEmpresa(NomeEmpresaInput.getText());
        novoContato.setCargoEmpresa(CargoEmpresaInput.getText());

        // Substitui o contato existente na posição 'linha' pela nova versão
        listaContatos.set(linha, novoContato);

        // Atualize a linha correspondente na tabela
        DefaultTableModel tabela = (DefaultTableModel) Tabela.getModel();
        tabela.setValueAt(novoContato.getNome(), linha, 0);
        tabela.setValueAt(novoContato.getTipoContato(), linha, 1);
        tabela.setValueAt(novoContato.getIsFavorito(), linha, 2);
        tabela.setValueAt(novoContato.getCodigo(), linha, 3);

        //Volta para o modo leitura
        modo(false);

        //Desabilita os campos novamente
        camposEditaveis(false);

        //Seta o semaforo como falso
        ContatoEditado = false;
    }

    //Apaga um contato
    private void ApagarContato() {
        DefaultTableModel tabela = (DefaultTableModel) Tabela.getModel();

        var linha = Tabela.getSelectedRow();

        //Valida se há um elemento selecionado
        if (Tabela.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(rootPane, "Não há linhas selecionadas!");
            return;
        }

        //Remove o elemeto
        listaContatos.remove(linha);
        tabela.removeRow(linha);
    }

    //Obtem a linha atualmente selecionada
    private ContatoClass ObterContatoSelecionado() {
        DefaultTableModel tabela = (DefaultTableModel) Tabela.getModel();
        String linha = tabela.getValueAt(Tabela.getSelectedRow(), 3).toString();

        for (ContatoClass a : listaContatos) {
            if (a.getCodigo().compareTo((linha)) == 0) {
                return a;
            }
        }
        return null;
    }

    //Seta o contato Obtido
    private void SetarContatoSelecionado(ContatoClass contato) {

        NomeInput.setText(contato.getNome());
        TipoSelect.setSelectedItem(contato.getTipoContato());
        FavoritosChekBox.setSelected(contato.getIsFavorito());
        TelefoneInput.setText(contato.getTelefone());
        FaxInput.setText(contato.getFax());
        ObservacaoTextArea.setText(contato.getObservacao());
        NomeEmpresaInput.setText(contato.getNomeEmpresa());
        CargoEmpresaInput.setText(contato.getCargoEmpresa());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ContatosPanel = new javax.swing.JPanel();
        NomeLabel = new javax.swing.JLabel();
        NomeInput = new javax.swing.JTextField();
        TipoSelect = new javax.swing.JComboBox<>();
        TipoLabel = new javax.swing.JLabel();
        FavoritosChekBox = new javax.swing.JCheckBox();
        TelefonesPanel = new javax.swing.JPanel();
        TelefoneLabel = new javax.swing.JLabel();
        CelularLabel = new javax.swing.JLabel();
        FaxLabel = new javax.swing.JLabel();
        FaxInput = new javax.swing.JTextField();
        TelefoneInput = new javax.swing.JFormattedTextField();
        CelularInput = new javax.swing.JFormattedTextField();
        ObservacaoPanel = new javax.swing.JPanel();
        ObservacaoScroll = new javax.swing.JScrollPane();
        ObservacaoTextArea = new javax.swing.JTextArea();
        EmpresaPanel = new javax.swing.JPanel();
        NomeEmpresaLabel = new javax.swing.JLabel();
        NomeEmpresaInput = new javax.swing.JTextField();
        CargoEmpresaLabel = new javax.swing.JLabel();
        CargoEmpresaInput = new javax.swing.JTextField();
        RegistrosPanel = new javax.swing.JPanel();
        TabelaScroll = new javax.swing.JScrollPane();
        Tabela = new javax.swing.JTable();
        GravarButton = new javax.swing.JButton();
        InserirButton = new javax.swing.JButton();
        EditarButton = new javax.swing.JButton();
        ExcluirButton = new javax.swing.JButton();
        CancelarButton = new javax.swing.JButton();
        CabecalhoPanel = new javax.swing.JPanel();
        TituloLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        ContatosPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Contatos"));

        NomeLabel.setText("Nome:");

        NomeInput.setEnabled(false);
        NomeInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NomeInputActionPerformed(evt);
            }
        });

        TipoSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pessoal", "Profissional", "Outro" }));
        TipoSelect.setEnabled(false);
        TipoSelect.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TipoSelectItemStateChanged(evt);
            }
        });

        TipoLabel.setText("Tipo de Contato:");

        FavoritosChekBox.setText("Favoritos");
        FavoritosChekBox.setEnabled(false);
        FavoritosChekBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        FavoritosChekBox.setName(""); // NOI18N

        TelefonesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Telefones"));

        TelefoneLabel.setText("Telefone:");

        CelularLabel.setText("Celular:");

        FaxLabel.setText("Fax:");

        FaxInput.setEnabled(false);

        try {
            TelefoneInput.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) #### - ####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            CelularInput.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) ##### - ####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout TelefonesPanelLayout = new javax.swing.GroupLayout(TelefonesPanel);
        TelefonesPanel.setLayout(TelefonesPanelLayout);
        TelefonesPanelLayout.setHorizontalGroup(
            TelefonesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(FaxInput)
            .addComponent(TelefoneInput, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(TelefonesPanelLayout.createSequentialGroup()
                .addGroup(TelefonesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FaxLabel)
                    .addComponent(CelularLabel)
                    .addComponent(TelefoneLabel))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(CelularInput)
        );
        TelefonesPanelLayout.setVerticalGroup(
            TelefonesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TelefonesPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(TelefoneLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TelefoneInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CelularLabel)
                .addGap(4, 4, 4)
                .addComponent(CelularInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(FaxLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FaxInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        ObservacaoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Observação"));

        ObservacaoTextArea.setColumns(20);
        ObservacaoTextArea.setRows(5);
        ObservacaoTextArea.setEnabled(false);
        ObservacaoScroll.setViewportView(ObservacaoTextArea);

        javax.swing.GroupLayout ObservacaoPanelLayout = new javax.swing.GroupLayout(ObservacaoPanel);
        ObservacaoPanel.setLayout(ObservacaoPanelLayout);
        ObservacaoPanelLayout.setHorizontalGroup(
            ObservacaoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ObservacaoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ObservacaoScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addContainerGap())
        );
        ObservacaoPanelLayout.setVerticalGroup(
            ObservacaoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ObservacaoPanelLayout.createSequentialGroup()
                .addComponent(ObservacaoScroll)
                .addContainerGap())
        );

        EmpresaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Empresa"));

        NomeEmpresaLabel.setText("Nome:");

        NomeEmpresaInput.setEnabled(false);

        CargoEmpresaLabel.setText("Cargo:");

        CargoEmpresaInput.setEnabled(false);
        CargoEmpresaInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CargoEmpresaInputActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout EmpresaPanelLayout = new javax.swing.GroupLayout(EmpresaPanel);
        EmpresaPanel.setLayout(EmpresaPanelLayout);
        EmpresaPanelLayout.setHorizontalGroup(
            EmpresaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmpresaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(EmpresaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NomeEmpresaLabel)
                    .addComponent(NomeEmpresaInput, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CargoEmpresaLabel)
                    .addComponent(CargoEmpresaInput, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        EmpresaPanelLayout.setVerticalGroup(
            EmpresaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmpresaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NomeEmpresaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NomeEmpresaInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(CargoEmpresaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CargoEmpresaInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        RegistrosPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Registros"));

        Tabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Contato", "Favorito", "Codigo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        Tabela.setFocusable(false);
        Tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelaMouseClicked(evt);
            }
        });
        TabelaScroll.setViewportView(Tabela);

        javax.swing.GroupLayout RegistrosPanelLayout = new javax.swing.GroupLayout(RegistrosPanel);
        RegistrosPanel.setLayout(RegistrosPanelLayout);
        RegistrosPanelLayout.setHorizontalGroup(
            RegistrosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RegistrosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabelaScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        RegistrosPanelLayout.setVerticalGroup(
            RegistrosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RegistrosPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(TabelaScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout ContatosPanelLayout = new javax.swing.GroupLayout(ContatosPanel);
        ContatosPanel.setLayout(ContatosPanelLayout);
        ContatosPanelLayout.setHorizontalGroup(
            ContatosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContatosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ContatosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ContatosPanelLayout.createSequentialGroup()
                        .addComponent(RegistrosPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(66, 66, 66))
                    .addGroup(ContatosPanelLayout.createSequentialGroup()
                        .addGroup(ContatosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ContatosPanelLayout.createSequentialGroup()
                                .addComponent(TelefonesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ObservacaoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(NomeLabel)
                            .addComponent(NomeInput, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(ContatosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ContatosPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(TipoLabel)
                                .addGap(183, 183, 183))
                            .addGroup(ContatosPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ContatosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(ContatosPanelLayout.createSequentialGroup()
                                        .addComponent(TipoSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(27, 27, 27)
                                        .addComponent(FavoritosChekBox))
                                    .addComponent(EmpresaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );
        ContatosPanelLayout.setVerticalGroup(
            ContatosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContatosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ContatosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NomeLabel)
                    .addComponent(TipoLabel))
                .addGap(4, 4, 4)
                .addGroup(ContatosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NomeInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TipoSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FavoritosChekBox))
                .addGap(18, 18, 18)
                .addGroup(ContatosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(EmpresaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TelefonesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ObservacaoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(RegistrosPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        GravarButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        GravarButton.setText("Gravar");
        GravarButton.setEnabled(false);
        GravarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GravarButtonActionPerformed(evt);
            }
        });

        InserirButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        InserirButton.setText("Inserir");
        InserirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InserirButtonActionPerformed(evt);
            }
        });

        EditarButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        EditarButton.setText("Editar");
        EditarButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EditarButtonMouseClicked(evt);
            }
        });
        EditarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditarButtonActionPerformed(evt);
            }
        });

        ExcluirButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ExcluirButton.setText("Excluir");
        ExcluirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExcluirButtonActionPerformed(evt);
            }
        });

        CancelarButton.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        CancelarButton.setText("Cancelar");
        CancelarButton.setEnabled(false);
        CancelarButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CancelarButtonMouseClicked(evt);
            }
        });
        CancelarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelarButtonActionPerformed(evt);
            }
        });

        CabecalhoPanel.setBackground(new java.awt.Color(0, 51, 51));

        TituloLabel.setBackground(new java.awt.Color(0, 102, 102));
        TituloLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TituloLabel.setForeground(new java.awt.Color(255, 255, 255));
        TituloLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TituloLabel.setText("Agenda de Contatos");
        TituloLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout CabecalhoPanelLayout = new javax.swing.GroupLayout(CabecalhoPanel);
        CabecalhoPanel.setLayout(CabecalhoPanelLayout);
        CabecalhoPanelLayout.setHorizontalGroup(
            CabecalhoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CabecalhoPanelLayout.createSequentialGroup()
                .addGap(233, 233, 233)
                .addComponent(TituloLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CabecalhoPanelLayout.setVerticalGroup(
            CabecalhoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CabecalhoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TituloLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        TituloLabel.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(InserirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(GravarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CancelarButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ExcluirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EditarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 120, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CabecalhoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ContatosPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 749, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CabecalhoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ContatosPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(EditarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(GravarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(InserirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(ExcluirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(CancelarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CargoEmpresaInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CargoEmpresaInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CargoEmpresaInputActionPerformed

    //
    //==>Grava o novo registro
    //
    private void GravarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GravarButtonActionPerformed
        if (ContatoEditado == true) {
            var linha = Tabela.getSelectedRow();
            GravarContatoEditado(linha);
        } else {
            GravarNovoContato();
        }
    }//GEN-LAST:event_GravarButtonActionPerformed

    //
    //==>Ao clicar em incluir, ele habilita todos os campos para serem preenchidos
    //
    private void InserirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InserirButtonActionPerformed
        limparCampos();
        modo(true);
        GravarButton.setEnabled(true);


    }//GEN-LAST:event_InserirButtonActionPerformed

    private void EditarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditarButtonActionPerformed
        
    }//GEN-LAST:event_EditarButtonActionPerformed

    //
    //==>Remover elemento da tela
    //
    private void ExcluirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExcluirButtonActionPerformed
        ApagarContato();
    }//GEN-LAST:event_ExcluirButtonActionPerformed

    private void CancelarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelarButtonActionPerformed

    }//GEN-LAST:event_CancelarButtonActionPerformed

    private void NomeInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NomeInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NomeInputActionPerformed

    //
    //==>Ao fechar a janela, grava o XML
    //
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        GravarArquivoXML();
    }//GEN-LAST:event_formWindowClosing

    //
    //==>Ao abrir, carrega o XML
    //
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            CarregarDadosArquivoXML();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowOpened

    private void TipoSelectItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TipoSelectItemStateChanged
        if (TipoSelect.getSelectedItem() == "Profissional") {
            EmpresaPanel.setVisible(true);
        } else {
            EmpresaPanel.setVisible(false);
        }
    }//GEN-LAST:event_TipoSelectItemStateChanged

    private void CancelarButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarButtonMouseClicked
        modo(false);
        limparCampos();
    }//GEN-LAST:event_CancelarButtonMouseClicked

    private void TabelaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelaMouseClicked

        //Se não houver nada selecionado , RETORNE
        if (Tabela.getSelectedRow() == -1 /*|| !Tabela.isEnabled()*/) {
            return;
        }

        ContatoClass contato = ObterContatoSelecionado();

        if (contato != null) {
            //Setta a linha selecionada
            SetarContatoSelecionado(contato);
        }
    }//GEN-LAST:event_TabelaMouseClicked

    private void EditarButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EditarButtonMouseClicked
        ContatoEditado = true;

        //Se não houver nada selecionado , RETORNE;
        if (Tabela.getSelectedRow() == -1 || !Tabela.isEnabled()) {
            JOptionPane.showMessageDialog(rootPane, "Selecione um contato a ser editado.");
            return;
        } else {
            camposEditaveis(true);
            modo(true);
        }

    }//GEN-LAST:event_EditarButtonMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CabecalhoPanel;
    private javax.swing.JButton CancelarButton;
    private javax.swing.JTextField CargoEmpresaInput;
    private javax.swing.JLabel CargoEmpresaLabel;
    private javax.swing.JFormattedTextField CelularInput;
    private javax.swing.JLabel CelularLabel;
    private javax.swing.JPanel ContatosPanel;
    private javax.swing.JButton EditarButton;
    private javax.swing.JPanel EmpresaPanel;
    private javax.swing.JButton ExcluirButton;
    private javax.swing.JCheckBox FavoritosChekBox;
    private javax.swing.JTextField FaxInput;
    private javax.swing.JLabel FaxLabel;
    private javax.swing.JButton GravarButton;
    private javax.swing.JButton InserirButton;
    private javax.swing.JTextField NomeEmpresaInput;
    private javax.swing.JLabel NomeEmpresaLabel;
    private javax.swing.JTextField NomeInput;
    private javax.swing.JLabel NomeLabel;
    private javax.swing.JPanel ObservacaoPanel;
    private javax.swing.JScrollPane ObservacaoScroll;
    private javax.swing.JTextArea ObservacaoTextArea;
    private javax.swing.JPanel RegistrosPanel;
    private javax.swing.JTable Tabela;
    private javax.swing.JScrollPane TabelaScroll;
    private javax.swing.JFormattedTextField TelefoneInput;
    private javax.swing.JLabel TelefoneLabel;
    private javax.swing.JPanel TelefonesPanel;
    private javax.swing.JLabel TipoLabel;
    private javax.swing.JComboBox<String> TipoSelect;
    private javax.swing.JLabel TituloLabel;
    // End of variables declaration//GEN-END:variables
}
