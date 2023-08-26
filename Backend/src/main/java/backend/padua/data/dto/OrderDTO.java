package backend.padua.data.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO extends RepresentationModel<OrderDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotEmpty(message = "Preenchimento obrigatório")
    private String date;

    @NotEmpty(message = "Preenchimento obrigatório")
    private String clientName;

    @NotEmpty(message = "Preenchimento obrigatório")
    private String deliveryAddress;

    @NotEmpty(message = "Preenchimento obrigatório")
    private List<String> itens;
}
