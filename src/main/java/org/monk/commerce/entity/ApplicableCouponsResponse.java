package org.monk.commerce.entity;

import java.util.List;

public class ApplicableCouponsResponse {
	private List<ApplicableCoupons> applicableCoupons;

	public ApplicableCouponsResponse(List<ApplicableCoupons> applicableCoupons) {
		this.applicableCoupons = applicableCoupons;
	}

	public List<ApplicableCoupons> getApplicableCoupons() {
		return applicableCoupons;
	}

	public void setApplicableCoupons(List<ApplicableCoupons> applicableCoupons) {
		this.applicableCoupons = applicableCoupons;
	}
}
