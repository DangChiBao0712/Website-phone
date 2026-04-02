package poly.edu.model;

public class CartItem {
    Integer id;
    String name;
    String image;
    Double price;
    Integer qty = 1;

    // Constructor không tham số
    public CartItem() {
    }

    // Constructor đầy đủ tham số
    public CartItem(Integer id, String name, String image, Double price, Integer qty) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.qty = qty;
    }

    // --- GETTERS VÀ SETTERS THỦ CÔNG ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}