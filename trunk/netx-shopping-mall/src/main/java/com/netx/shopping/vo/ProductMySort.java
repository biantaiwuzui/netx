package com.netx.shopping.vo;

import com.netx.shopping.model.product.Product;

public class ProductMySort implements Comparable<ProductMySort> {
    private Product product;
    private Integer order;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public int compareTo(ProductMySort o) {
        return order.compareTo(o.getOrder());
    }
}
