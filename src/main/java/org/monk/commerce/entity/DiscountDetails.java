package org.monk.commerce.entity;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

@Embeddable
public class DiscountDetails {

    private Double threshold;
    private Double discount;
    @JsonProperty("product_id")
    private Long productId;

    @ElementCollection
    @JsonProperty("buy_products")
    private List<BuyGetProduct> buyProducts;

    @ElementCollection
    @JsonProperty("get_products")
    private List<BuyGetProduct> getProducts;

    @JsonProperty("repition_limit")
    private Integer repetitionLimit;

	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public List<BuyGetProduct> getBuyProducts() {
		return buyProducts;
	}

	public void setBuyProducts(List<BuyGetProduct> buyProducts) {
		this.buyProducts = buyProducts;
	}

	public List<BuyGetProduct> getGetProducts() {
		return getProducts;
	}

	public void setGetProducts(List<BuyGetProduct> getProducts) {
		this.getProducts = getProducts;
	}

	public Integer getRepetitionLimit() {
		return repetitionLimit;
	}

	public void setRepetitionLimit(Integer repetitionLimit) {
		this.repetitionLimit = repetitionLimit;
	}
}
