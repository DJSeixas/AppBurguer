package backend.padua.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewClientDTO extends RepresentationModel<NewClientDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;

    @NotEmpty(message = "Preenchimento obrigatório")
    @Size(min = 5, max=120, message = "O tamanho deve ser entre 5 e 120 caracteres")
    private String name;

    @NotEmpty(message = "Preenchimento obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotEmpty(message = "Preenchimento obrigatório")
    @CPF(message = "CPF inválido")
    private String cpf;

    @NotEmpty(message = "Preenchimento obrigatório")
    private String address;

    @NotNull(message = "Preenchimento obrigatório")
    private int number;

    private String complement;

    @NotEmpty(message = "Preenchimento obrigatório")
    private String neighborhood;

    @NotEmpty(message = "Preenchimento obrigatório")
    private String city;

    @NotEmpty(message = "Preenchimento obrigatório")
    private String state;

    @NotEmpty(message = "Preenchimento obrigatório")
    private String cep;

    @NotEmpty(message = "Preenchimento obrigatório")
    private String telephone;

}
