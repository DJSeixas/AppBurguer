package backend.padua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String address;

    @Column(nullable = false, length = 160)
    private int number;

    @Column(length = 160)
    private String complement;

    @Column(nullable = false, length = 160)
    private String neighborhood;

    @Column(nullable = false, length = 160)
    private String cep;

    @Column(nullable = false, length = 160)
    private String city;

    @Column(nullable = false, length = 160)
    private String state;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
