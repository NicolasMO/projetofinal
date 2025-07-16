package br.com.raroacademy.projetofinal.dispachers;

import br.com.raroacademy.projetofinal.interfaces.ICommand;
import br.com.raroacademy.projetofinal.interfaces.ICommandHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class CommandBus {
    private final Map<Class<?>, ICommandHandler<?, ?>> handlers = new HashMap<>();

    public <C extends ICommand, R> void registerHandler(Class<C> commandClass, ICommandHandler<C, R> handler) {
        handlers.put(commandClass, handler);
    }

    @SuppressWarnings("unchecked")
    public <R, C extends ICommand> R dispatch(C command) {
        ICommandHandler<C, R> handler = (ICommandHandler<C, R>) handlers.get(command.getClass());
        if (handler == null) {
            throw new RuntimeException("No handler found for command: " + command.getClass().getName());
        }
        return handler.handle(command);
    }
}
