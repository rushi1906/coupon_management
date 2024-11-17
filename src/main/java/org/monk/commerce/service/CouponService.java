package org.monk.commerce.service;
import java.util.List;
import java.util.Optional;

import org.monk.commerce.entity.ApplicableCoupons;
import org.monk.commerce.entity.Cart;
import org.monk.commerce.entity.CartItem;
import org.monk.commerce.entity.Coupon;
import org.monk.commerce.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface CouponService {
    public Coupon createCoupon(Coupon coupon) ;
    public List<Coupon> getAllCoupons();
    public Coupon getCouponById(Long id);
    public Coupon updateCoupon(Long id, Coupon coupon);
    public void deleteCoupon(Long id);
    public List<ApplicableCoupons> getApplicableCoupons(Cart cart);
    public Cart applyCoupon(Long couponId, Cart cart);
    public boolean isCouponApplicable(Coupon coupon, Cart cart);
    public Double calculateDiscount(Coupon coupon, Cart cart);
    public Double getItemDiscount(Coupon coupon, CartItem item);
}
