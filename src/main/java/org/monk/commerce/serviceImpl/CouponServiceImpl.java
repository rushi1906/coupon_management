package org.monk.commerce.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.monk.commerce.entity.ApplicableCoupons;
import org.monk.commerce.entity.BuyGetProduct;
import org.monk.commerce.entity.Cart;
import org.monk.commerce.entity.CartItem;
import org.monk.commerce.entity.Coupon;
import org.monk.commerce.exception.CouponException;
import org.monk.commerce.repository.CouponRepository;
import org.monk.commerce.service.CouponService;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CouponServiceImpl implements CouponService {

	private final CouponRepository couponRepository;

	public CouponServiceImpl(CouponRepository couponRepository) {
		this.couponRepository = couponRepository;
	}

	public Coupon createCoupon(Coupon coupon) {
		validateCoupon(coupon);
		return couponRepository.save(coupon);
	}

	public List<Coupon> getAllCoupons() {
		return couponRepository.findAll();
	}

	public Coupon getCouponById(Long id) {
		
		Coupon coupon = couponRepository.findById(id)
				.orElseThrow(() -> new CouponException("Coupon with ID " + id + " not found"));
		validateCoupon(coupon);
		return coupon;
	}

	public Coupon updateCoupon(Long id, Coupon coupon) {
		if (!couponRepository.existsById(id)) {
			throw new CouponException("Coupon with ID " + id + " not found");
		}
		validateCoupon(coupon);
		coupon.setId(id);
		return couponRepository.save(coupon);
	}

	public void deleteCoupon(Long id) {
		if (!couponRepository.existsById(id)) {
			throw new CouponException("Coupon with ID " + id + " not found");
		}
		couponRepository.deleteById(id);
	}

	public List<ApplicableCoupons> getApplicableCoupons(Cart cart) {
		if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
			throw new CouponException("Cart is empty or invalid");
		}
		return couponRepository.findAll().stream().filter(coupon -> isCouponApplicable(coupon, cart))
				.map(coupon -> new ApplicableCoupons(coupon.getId(), coupon.getType(), calculateDiscount(coupon, cart)))
				.toList();
	}

	public Cart applyCoupon(Long couponId, Cart cart) {
		if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
			throw new CouponException("Cart is empty or invalid");
		}

		Coupon coupon = couponRepository.findById(couponId)
				.orElseThrow(() -> new CouponException("Coupon with ID " + couponId + " not found"));

		validateCouponExpiry(coupon);

		Double totalDiscount = calculateDiscount(coupon, cart);

		cart.getItems().forEach(item -> {
			Double itemDiscount = getItemDiscount(coupon, item);
			item.setTotalDiscount(itemDiscount);
		});
		cart.setTotalPrice(calculateTotalPrice(cart));
		cart.setTotalDiscount(totalDiscount);
		cart.setFinalPrice(cart.getTotalPrice() - cart.getTotalDiscount());
		return cart;
	}

	private void validateCoupon(Coupon coupon) {
		if (coupon.getType() == null || coupon.getType().isBlank()) {
			throw new CouponException("Coupon type must not be null or empty");
		}
		if (!coupon.getType().equalsIgnoreCase("bxgy")
				&& (coupon.getDetails().getDiscount() == null || coupon.getDetails().getDiscount() <= 0)) {
			throw new CouponException("Discount must be greater than zero");
		}
		if ("cart-wise".equals(coupon.getType())
				&& (coupon.getDetails().getThreshold() == null || coupon.getDetails().getThreshold() <= 0)) {
			throw new CouponException("Cart-wise coupons must have a valid threshold");
		}
	}

	public boolean isCouponApplicable(Coupon coupon, Cart cart) {
		if (coupon != null && coupon.getExpirationDate() != null
				&& coupon.getExpirationDate().isBefore(LocalDateTime.now())) {
			return false;
		}
		if ("cart-wise".equals(coupon.getType())) {
			return calculateTotalPrice(cart) >= coupon.getDetails().getThreshold();
		} else if ("product-wise".equals(coupon.getType())) {
			Long targetProductId = coupon.getDetails().getProductId();
			return cart.getItems().stream().anyMatch(item -> item.getProductId().equals(targetProductId));
		} else if ("bxgy".equals(coupon.getType())) {

			List<BuyGetProduct> buyProducts = coupon.getDetails().getBuyProducts();
			List<BuyGetProduct> getProducts = coupon.getDetails().getGetProducts();
			int repetitionLimit = coupon.getDetails().getRepetitionLimit();

			if (buyProducts == null || getProducts == null || repetitionLimit <= 0) {
				throw new CouponException("Invalid BxGy coupon details");
			}

			int maxRepetitions = Integer.MAX_VALUE;

			for (BuyGetProduct buyProduct : buyProducts) {
				Long productId = buyProduct.getProductId();
				int requiredQuantity = buyProduct.getQuantity();

				int availableQuantity = 0;
				for (CartItem cartItem : cart.getItems()) {
					if (cartItem.getProductId().equals(productId)) {
						availableQuantity = cartItem.getQuantity();
						break;
					}
				}

				if (availableQuantity < requiredQuantity) {
					return false;
				}
				int repetitionsForProduct = availableQuantity / requiredQuantity;

				maxRepetitions = Math.min(maxRepetitions, repetitionsForProduct);
			}

			// Ensure the repetitions do not exceed the repetition limit
			return maxRepetitions > 0 && maxRepetitions <= repetitionLimit;
		}
		return true;
	}

	private Double calculateTotalPrice(Cart cart) {

		return cart.getItems().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
	}

	public Double calculateDiscount(Coupon coupon, Cart cart) {
		if ("cart-wise".equals(coupon.getType())) {
			return calculateTotalPrice(cart) * coupon.getDetails().getDiscount() / 100;
		} else if ("product-wise".equals(coupon.getType())) {
			Long targetProductId = coupon.getDetails().getProductId();
			double discountPercentage = coupon.getDetails().getDiscount();
			double totalDiscount = 0.0;
			for (CartItem item : cart.getItems()) {
				if (item.getProductId().equals(targetProductId)) {
					totalDiscount = (item.getPrice() * item.getQuantity()) * (discountPercentage / 100);
					break;
				}
			}
			return totalDiscount;
		} else if ("bxgy".equals(coupon.getType())) {
			List<BuyGetProduct> buyProducts = coupon.getDetails().getBuyProducts();
			List<BuyGetProduct> getProducts = coupon.getDetails().getGetProducts();
			int repetitionLimit = coupon.getDetails().getRepetitionLimit();

			if (buyProducts == null || getProducts == null || repetitionLimit <= 0) {
				throw new IllegalArgumentException("Invalid BxGy coupon details");
			}

			int maxRepetitions = Integer.MAX_VALUE;

			for (BuyGetProduct buyProduct : buyProducts) {
				Long productId = buyProduct.getProductId();
				int requiredQuantity = buyProduct.getQuantity();

				int availableQuantity = 0;
				for (CartItem cartItem : cart.getItems()) {
					if (cartItem.getProductId().equals(productId)) {
						availableQuantity = cartItem.getQuantity();
						break;
					}
				}

				if (availableQuantity < requiredQuantity) {
					return 0.0;
				}

				int repetitionsForProduct = availableQuantity / requiredQuantity;
				maxRepetitions = Math.min(maxRepetitions, repetitionsForProduct);
			}

			int actualRepetitions = Math.min(maxRepetitions, repetitionLimit);

			double totalDiscount = 0.0;
			for (BuyGetProduct getProduct : getProducts) {
				Long productId = getProduct.getProductId();
				int freeQuantity = getProduct.getQuantity() * actualRepetitions;

				for (CartItem cartItem : cart.getItems()) {
					if (cartItem.getProductId().equals(productId)) {
						totalDiscount += cartItem.getPrice() * freeQuantity;
						break;
					}
				}
			}

			return totalDiscount;
		}
		return 0.0;
	}

	public Double getItemDiscount(Coupon coupon, CartItem item) {
        if ("product-wise".equals(coupon.getType()) && coupon.getDetails().getProductId()==item.getProductId() ) {
            return (item.getPrice() * coupon.getDetails().getDiscount() / 100) * item.getQuantity() ;
        }
		return 0.0;
	}
	public void validateCouponExpiry(Coupon coupon) {
		if (coupon != null && coupon.getExpirationDate() != null
				&& coupon.getExpirationDate().isBefore(LocalDateTime.now())) {
			throw new CouponException("Coupon with ID " + coupon.getId() + " has expired");
		}
	}
}
