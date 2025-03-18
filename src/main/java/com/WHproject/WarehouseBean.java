package com.WHproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named("warehouseBean")
@SessionScoped
public class WarehouseBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String productName;
    private int quantity;
    private Product selectedProduct = new Product(); // // Parametresiz constructor kullanılıyor
    private List<Product> products = new ArrayList<>();
    private ProductDAO productDAO = new ProductDAO();

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public Product getSelectedProduct() { return selectedProduct; }
    public void setSelectedProduct(Product selectedProduct) { 
        if (selectedProduct != null) {
            this.selectedProduct = selectedProduct;
        }
    }

    public List<Product> getProducts() {  
    	products = productDAO.getAllProducts();  // Veritabanından ürünleri alıyoruz
    	return products; 
    }
    
    public void selectProduct(Product product) {
        if (product != null) {
            this.selectedProduct = product;
            this.productName = product.getName();
            this.quantity = product.getQuantity();
        }
    }
    
    public void addProduct() {
        if (productName != null && !productName.trim().isEmpty() && quantity > 0) {
            Product newProduct = new Product(0,productName, quantity);
            productDAO.addProduct(newProduct);  // Veritabanına ekliyoruz
            products.add(newProduct);  // Listeye ekliyoruz (sayfa güncellenir)
            productName = "";
            quantity = 0;
            selectedProduct = new Product();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Ürün eklendi!"));
        }
    }
    
    public void removeProduct(Product product) {
        productDAO.removeProduct(product);  // Veritabanından siliyoruz
        products.remove(product);  // Listeyi güncelliyoruz
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Ürün silindi!"));
    }
    
    public void updateProduct() {
        if (selectedProduct != null && selectedProduct.getName() != null && !selectedProduct.getName().trim().isEmpty() 
                && selectedProduct.getQuantity() > 0) {
            productDAO.updateProduct(selectedProduct);  // Veritabanında güncelliyoruz
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Ürün güncellendi!"));
            selectedProduct = new Product();  
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Geçersiz veri!"));
        }
    }



    public static class Product implements Serializable {
        private static final long serialVersionUID = 1L;
        private int id;
        private String name;
        private int quantity;
        
        public Product() {
            this.id = 0; // Boş nesne için id = 0
            this.name = "";
            this.quantity = 0;
        }
         public Product(int id, String name, int quantity) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
        }
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
 
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

       
    }
}
