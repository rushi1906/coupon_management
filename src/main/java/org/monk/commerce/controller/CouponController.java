package org.monk.commerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.monk.commerce.entity.ApplicableCoupons;
import org.monk.commerce.entity.ApplicableCouponsResponse;
import org.monk.commerce.entity.Cart;
import org.monk.commerce.entity.CartRequest;
import org.monk.commerce.entity.Coupon;
import org.monk.commerce.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupons")
public class CouponController {

	private final CouponService couponService;

	@Autowired
	public CouponController(CouponService couponService) {
		this.couponService = couponService;
	}

	@PostMapping
	public Coupon createCoupon(@RequestBody Coupon coupon) {
		return couponService.createCoupon(coupon);
	}

	@GetMapping
	public List<Coupon> getAllCoupons() {
		return couponService.getAllCoupons();
	}

	@GetMapping("/{id}")
	public Coupon getCouponById(@PathVariable Long id) {
		return couponService.getCouponById(id);
	}

	@PutMapping("/{id}")
	public Coupon updateCoupon(@PathVariable Long id, @RequestBody Coupon coupon) {
		return couponService.updateCoupon(id, coupon);
	}

	@DeleteMapping("/{id}")
	public void deleteCoupon(@PathVariable Long id) {
		couponService.deleteCoupon(id);
	}

	@PostMapping("/applicable-coupons")
	public Map<String, List<ApplicableCoupons>> getApplicableCoupons(@RequestBody CartRequest cartRequest) {
		List<ApplicableCoupons> applicableCoupons = couponService.getApplicableCoupons(cartRequest.getCart());
		Map<String, List<ApplicableCoupons>> response = new HashMap<>();
		response.put("applicable_coupons", applicableCoupons);
		return response;
	}

	@PostMapping("/apply/{id}")
	public Map<String, Cart> applyCoupon(@PathVariable Long id, @RequestBody CartRequest cartRequest) {
		Cart updatedCart = couponService.applyCoupon(id, cartRequest.getCart());
		Map<String, Cart> response = new HashMap<>();
		response.put("updated_cart", updatedCart);
		return response;
	}
}
