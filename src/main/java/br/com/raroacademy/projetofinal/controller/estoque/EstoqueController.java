package br.com.raroacademy.projetofinal.controller.estoque;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.raroacademy.projetofinal.dto.estoque.EstoqueDetalhadoPaginaDTO;
import br.com.raroacademy.projetofinal.dto.estoque.EstoqueGeralRespostaDTO;
import br.com.raroacademy.projetofinal.enums.STATUS_EQUIPAMENTO;
import br.com.raroacademy.projetofinal.service.estoque.EstoqueService;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {
	
	@Autowired
	private EstoqueService estoqueService;
	
	@GetMapping("/geral")
	public ResponseEntity<Page<EstoqueGeralRespostaDTO>> listarEstoqueGeral(
			@RequestParam(required = false) String tipoEquipamento,
			@RequestParam(required = false) List<STATUS_EQUIPAMENTO> status,
			@RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho
			) 
	{
		Pageable paginacao = PageRequest.of(pagina, tamanho);
        Page<EstoqueGeralRespostaDTO> estoque = estoqueService.obterEstoqueGeral(tipoEquipamento, status, paginacao);
        return ResponseEntity.ok(estoque);
    }
	
	@GetMapping("/detalhado")
    public ResponseEntity<EstoqueDetalhadoPaginaDTO> listarEstoqueDetalhado(
            @RequestParam(required = false) String tipoEquipamento,
            @RequestParam(required = false) List<STATUS_EQUIPAMENTO> status,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho
            ) 
	{
        Pageable paginacao = PageRequest.of(pagina, tamanho);
        EstoqueDetalhadoPaginaDTO respostaPagina = estoqueService.obterEstoqueDetalhado(tipoEquipamento, status, paginacao);
        return ResponseEntity.ok(respostaPagina);
    }
}
