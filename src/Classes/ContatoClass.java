
package Classes;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.awt.List;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class ContatoClass {
    //Usar classe Uuid para gerar o ID
    
    private String codigo;
    private String nome;
    private String tipoContato;
    private Boolean isFavorito;
     
    private String telefone;
    private String celular;
    private String fax;
    
    private String observacao;
    
    private String nomeEmpresa;
    private String cargoEmpresa;
    
    
    public ContatoClass(){
        UUID uuid = UUID.randomUUID();
        codigo = uuid.toString();
        //System.out.println("Novo objeto código: " + codigo);
    }

    

    
    
    
    public String getCodigo() {
        return codigo;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoContato() {
        return tipoContato;
    }

    public void setTipoContato(String tipoContato) {
        this.tipoContato = tipoContato;
    }

    public Boolean getIsFavorito() {
        return isFavorito;
    }

    public void setIsFavorito(Boolean isFavorito) {
        this.isFavorito = isFavorito;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getCargoEmpresa() {
        return cargoEmpresa;
    }

    public void setCargoEmpresa(String cargoEmpresa) {
        this.cargoEmpresa = cargoEmpresa;
    }
    
    
    
    
   
    
}
