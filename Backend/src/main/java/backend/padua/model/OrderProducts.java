package backend.padua.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "order_products")
public class OrderProducts implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @EmbeddedId
    private OrderProductsPK id;

    @Column
    private Double price;

    @Column
    private Integer quantity;

    @Column
    private Double discount;

    public OrderProducts(Order orders,Product product , Double price, Integer
            quantity, Double discount) {
        super();
        id.setOrder(orders);
        id.setProduct(product);
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
    }

    public double getSubTotal(){
        return (price - discount) * quantity;
    }

    public Order getOrder() {
        return id.getOrder();
    }

    public void setOrder(Order orders) {
        id.setOrder(orders);
    }

    public Product getProduct() {
        return id.getProduct();
    }

    public void setProduct(Product product) {
        id.setProduct(product);
    }

}
