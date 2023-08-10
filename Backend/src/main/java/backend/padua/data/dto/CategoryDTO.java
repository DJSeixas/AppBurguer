package backend.padua.data.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    private Long id;

    @NotEmpty(message = "Preenchimento obrigatório")
    @Length(min = 5, max=80, message = "O tamanho deve ser entre 5 e 80 caracteres")
    private String name;
}
