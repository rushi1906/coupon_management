package org.monk.commerce.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApplicableCoupons {
	@JsonProperty("coupon_id")
    private Long couponId;      
    private String type;
    private Double discount;    
	public ApplicableCoupons(Long couponId, String type, Double discount) {
		super();
		this.couponId = couponId;
		this.type = type;
		this.discount = discount;
	}
	public Long getCouponId() {
		return couponId;
	}
	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}

   
}
