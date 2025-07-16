package br.com.raroacademy.projetofinal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseModel {
    public ResponseModel(Object content) {
        Content = content;
    }

    @JsonProperty("content")
    public Object Content;
}
