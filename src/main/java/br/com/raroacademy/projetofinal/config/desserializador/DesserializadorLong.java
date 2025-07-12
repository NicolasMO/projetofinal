package br.com.raroacademy.projetofinal.config.desserializador;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class DesserializadorLong extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    	
        if (parser.isExpectedNumberIntToken()) {
            return parser.getLongValue();
        }

        throw MismatchedInputException.from(parser, Long.class, "Esperado valor inteiro para campo Long");
    }
}