package poly.edu.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "Orderdetails")
public class OrderDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Double price;
    Integer quantity;

    @ManyToOne
    @JoinColumn(name = "Productid")
    Product product;

    @ManyToOne
    @JoinColumn(name = "Orderid")
    Order order;

    // --- CONSTRUCTOR ---
    public OrderDetail() {
    }

    // --- GETTERS & SETTERS (Copy đoạn này để hết lỗi detail.set...) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
}