package org.monk.commerce.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.monk.commerce.entity.Cart;
import org.monk.commerce.entity.CartItem;
import org.monk.commerce.entity.Coupon;
import org.monk.commerce.entity.DiscountDetails;
import org.monk.commerce.exception.CouponException;
import org.monk.commerce.repository.CouponRepository;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    private Coupon validCoupon;
    private Cart validCart;

    @BeforeEach
    void setUp() {
        // Sample coupon setup
        validCoupon = new Coupon();
        validCoupon.setId(1L);
        validCoupon.setType("cart-wise");
        validCoupon.setExpirationDate(LocalDateTime.now().plusDays(5));
        DiscountDetails details = new DiscountDetails();
        details.setDiscount(10.0);
        details.setThreshold(100.0);
        validCoupon.setDetails(details);

        // Sample cart setup
        validCart = new Cart();
        CartItem item = new CartItem();
        item.setProductId(101L);
        item.setPrice(50.0);
        item.setQuantity(3);
        validCart.setItems(List.of(item));
    }

    @Test
    void testCreateCoupon_Success() {
        when(couponRepository.save(validCoupon)).thenReturn(validCoupon);

        Coupon createdCoupon = couponService.createCoupon(validCoupon);

        assertNotNull(createdCoupon);
        assertEquals(validCoupon.getId(), createdCoupon.getId());
        verify(couponRepository, times(1)).save(validCoupon);
    }

    @Test
    void testGetCouponById_Success() {
        when(couponRepository.findById(1L)).thenReturn(Optional.of(validCoupon));

        Coupon coupon = couponService.getCouponById(1L);

        assertNotNull(coupon);
        assertEquals(1L, coupon.getId());
        verify(couponRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCouponById_NotFound() {
        when(couponRepository.findById(1L)).thenReturn(Optional.empty());

        CouponException exception = assertThrows(CouponException.class, () -> {
            couponService.getCouponById(1L);
        });

        assertEquals("Coupon with ID 1 not found", exception.getMessage());
        verify(couponRepository, times(1)).findById(1L);
    }

    @Test
    void testApplyCoupon_Success() {
        when(couponRepository.findById(1L)).thenReturn(Optional.of(validCoupon));

        Cart updatedCart = couponService.applyCoupon(1L, validCart);

        assertNotNull(updatedCart);
        assertEquals(150.0, updatedCart.getTotalPrice());
        assertEquals(15.0, updatedCart.getTotalDiscount());
        assertEquals(135.0, updatedCart.getFinalPrice());
        verify(couponRepository, times(1)).findById(1L);
    }

    @Test
    void testApplyCoupon_Expired() {
        validCoupon.setExpirationDate(LocalDateTime.now().minusDays(1));
        when(couponRepository.findById(1L)).thenReturn(Optional.of(validCoupon));

        CouponException exception = assertThrows(CouponException.class, () -> {
            couponService.applyCoupon(1L, validCart);
        });

        assertEquals("Coupon with ID 1 has expired", exception.getMessage());
        verify(couponRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteCoupon_Success() {
        when(couponRepository.existsById(1L)).thenReturn(true);
        doNothing().when(couponRepository).deleteById(1L);

        couponService.deleteCoupon(1L);

        verify(couponRepository, times(1)).existsById(1L);
        verify(couponRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCoupon_NotFound() {
        when(couponRepository.existsById(1L)).thenReturn(false);

        CouponException exception = assertThrows(CouponException.class, () -> {
            couponService.deleteCoupon(1L);
        });

        assertEquals("Coupon with ID 1 not found", exception.getMessage());
        verify(couponRepository, times(1)).existsById(1L);
    }
}
