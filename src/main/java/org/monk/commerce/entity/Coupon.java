package org.monk.commerce.entity;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Coupon {
    @Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    @JsonProperty("expiration_date")
    private LocalDateTime expirationDate;
    @Embedded
    private DiscountDetails details;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DiscountDetails getDetails() {
		return details;
	}

	public void setDetails(DiscountDetails details) {
		this.details = details;
	}

	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}
	
}
