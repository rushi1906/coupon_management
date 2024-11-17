package org.monk.commerce.utils;
public enum DiscountType {
    CART_WISE("cart-wise"),
    BXGY("bxgy"),
    PRODUCT_WISE("product-wise");

    private final String value;

    DiscountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DiscountType fromValue(String value) {
        for (DiscountType type : DiscountType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
