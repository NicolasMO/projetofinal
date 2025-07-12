package br.com.raroacademy.projetofinal.config.desserializador;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class DesserializadorInteiro extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
    	
        if (parser.isExpectedNumberIntToken()) {
            return parser.getIntValue();
        }

        throw MismatchedInputException.from(parser, Long.class, "Esperado valor inteiro para campo Integer");
    }
}