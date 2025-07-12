package br.com.raroacademy.projetofinal.model.usuario;

import br.com.raroacademy.projetofinal.enums.TipoToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
public class TokenUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime dataExpiracao;

    @Column(nullable = false)
    private boolean utilizado = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoToken tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public TokenUsuario(String token, LocalDateTime dataExpiracao, TipoToken tipo, Usuario usuario) {
        this.token = token;
        this.dataExpiracao = dataExpiracao;
        this.tipo = tipo;
        this.usuario = usuario;
        this.utilizado = false;
    }
}
