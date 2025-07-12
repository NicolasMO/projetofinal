package br.com.raroacademy.projetofinal.config.desserializador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class DesserializadorListaLong extends JsonDeserializer<List<Long>> {

    @Override
    public List<Long> deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        List<Long> result = new ArrayList<>();

        if (parser.currentToken() != JsonToken.START_ARRAY) {
            throw MismatchedInputException.from(parser, List.class, "Esperado um array de inteiros");
        }

        while (parser.nextToken() != JsonToken.END_ARRAY) {
            if (parser.currentToken().isNumeric()) {
                if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                    result.add(parser.getLongValue());
                } else {
                    throw MismatchedInputException.from(parser, Long.class, "Valor decimal não permitido em especificacoesIds");
                }
            } else {
                throw MismatchedInputException.from(parser, Long.class, "Esperado valor numérico na lista especificacoesIds");
            }
        }

        return result;
    }	
}