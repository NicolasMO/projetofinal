package br.com.raroacademy.projetofinal.dto.response;

import java.util.List;

public class ApiErroResposta {

    private int status;
    private String erro;
    private List<String> detalhes;

    public ApiErroResposta(int status, String erro, String mensagem, List<String> detalhes) {
        this.status = status;
        this.erro = erro;
        this.detalhes = detalhes;
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getErro() { return erro; }
    public void setErro(String erro) { this.erro = erro; }

    public List<String> getDetalhes() { return detalhes; }
    public void setDetalhes(List<String> detalhes) { this.detalhes = detalhes; }
}
