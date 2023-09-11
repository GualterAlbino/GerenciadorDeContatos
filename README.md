# GerenciadorDeContatos
Projeto simples utilizando a biblioteca Java Swing e o recurso de prevalência de dados armazendo as modificações em memoria principal, e trasnferindo posteriomente para um arquivo XML sendo esse utilizado como "base de dados" para que a aplicação consuma ao inicializar.


# Modo edição e leitura
Modo de edição: Neste modo o sistema deve:        

Desabilitar os botões: Inserir, Editar e Excluir;
Habilitar os botões: Gravar e Cancelar;
Habilitar todos os campos de entrada de dados: Nome, Tipo de contato, Favorito, Telefone, Celular, E-mail, Observação, Nome da empresa e Cargo 
Modo de Leitura: Neste modo o sistema deve:        

Habilitar os botões: Inserir, Editar e Excluir;
Desabilitar os botões: Gravar e Cancelar;
Desabilitar todos os campos de entrada de dados: Nome, Tipo de contato, Favorito, Telefone, Celular, E-mail, Observação, Nome da empresa e Cargo 


# Prevalência de dados.

Ao clicar no botão gravar, o sistema deve inserir a linha correspondente ao contato na tabela.
Ao clicar em uma linha da tabela, o sistema deve carregar os dados do contato para o formulário da tela.
Ao clicar no botão excluir, o sistema deve excluir o contato e remover a linha selecionada da tabela.
Ao clicar no botão cancelar, o sistema deve desfazer as alterações realizadas no contato e não gravadas.
Ao clicar no botão inserir, o sistema deve limpar o formulário e posicionar o curso no primeiro campo para que o usuário possa inserir os dados.
