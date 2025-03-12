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
    private Product selectedProduct = new Product("", 0); // NULL OLMASINI ÖNLEMEK İÇİN BOŞ NESNE
    private List<Product> products = new ArrayList<>();

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

    public List<Product> getProducts() { return products; }
    
    public void selectProduct(Product product) {
        if (product != null) {
            this.selectedProduct = product;
            this.productName = product.getName();
            this.quantity = product.getQuantity();
        }
    }
    
    public void addProduct() {
        if (productName != null && !productName.trim().isEmpty() && quantity > 0) {
            products.add(new Product(productName, quantity));
            productName = "";
            quantity = 0;
            selectedProduct = new Product("", 0); // FORMU SIFIRLA
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Ürün eklendi!"));
        }
    }
    
    public void removeProduct(Product product) {
        products.remove(product);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Ürün silindi!"));
    }
    
    public void updateProduct() {
        if (selectedProduct != null && selectedProduct.getName() != null && !selectedProduct.getName().trim().isEmpty() 
                && selectedProduct.getQuantity() > 0) {
            
            boolean updated = false;
            
            for (Product p : products) {
                if (p.getName().equals(selectedProduct.getName())) {
                    p.setName(selectedProduct.getName()); // Doğru şekilde güncelle
                    p.setQuantity(selectedProduct.getQuantity());
                    updated = true;
                    break;
                }
            }

            if (updated) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Başarılı", "Ürün güncellendi!"));
                
                // Formu sıfırla (Ama `selectedProduct` nesnesini kaybetme!)
                selectedProduct = new Product("", 0);  
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Ürün bulunamadı!"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Geçersiz veri!"));
        }
    }


    public static class Product implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private int quantity;
 
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
 
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public Product(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }
    }
}
