package br.com.raroacademy.projetofinal.interfaces;

public interface  ICommandHandler<C extends ICommand, R> {
    R handle(C command);
}
