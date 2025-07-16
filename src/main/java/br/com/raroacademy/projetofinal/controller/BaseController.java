package br.com.raroacademy.projetofinal.controller;

import br.com.raroacademy.projetofinal.dispachers.CommandBus;
import br.com.raroacademy.projetofinal.interfaces.ICommand;
import br.com.raroacademy.projetofinal.model.ResponseModel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public class BaseController {

    private final HttpServletResponse httpServletResponse;
    private final CommandBus commandBus;

    public BaseController(CommandBus commandBus, HttpServletResponse httpServletResponse) {
        this.commandBus = commandBus;
        this.httpServletResponse = httpServletResponse;
    }

    protected <R, C extends ICommand<R>> ResponseModel ExecuteController(C object, int StatusSuccess, int BadStatusRequest) {
        try {
            Object response = commandBus.dispatch(object);
            httpServletResponse.setStatus(StatusSuccess);
            return new ResponseModel(response);
        } catch (Exception e) {
            httpServletResponse.setStatus(BadStatusRequest);
            return new ResponseModel(e.getMessage());
        }
    }
}
