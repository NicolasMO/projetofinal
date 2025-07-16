package br.com.raroacademy.projetofinal.config.handlers;

import br.com.raroacademy.projetofinal.dispachers.CommandBus;
import br.com.raroacademy.projetofinal.service.usuario.CreateUser.CreateUserCommand;
import br.com.raroacademy.projetofinal.service.usuario.CreateUser.CreateUserCommandHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandConfig {
    private final CommandBus commandBus;
    private final CreateUserCommandHandler createUserCommandHandler;

    public CommandConfig(CommandBus commandBus, CreateUserCommandHandler createUserCommandHandler) {
        this.commandBus = commandBus;
        this.createUserCommandHandler = createUserCommandHandler;
    }

    @PostConstruct
    public void registerHandlers() {
        commandBus.registerHandler(CreateUserCommand.class, createUserCommandHandler);
    }
}
