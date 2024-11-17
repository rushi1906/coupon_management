# coupon_management

# Features
1. Covered the almost every case mentioned .
2. Added expiration dates for coupons.
3. Added the unit test cases for bussiness logic.
4. Error handling: Includes validation for coupon applicability, invalid input, and edge cases.
5. Dynamic Coupon Validation: Dynamically fetch and calculate applicable coupons based on cart details.
6. Easily add new coupon types and business logic in the future.
7. Added crud functionality to coupons easy to maintain and add new coupons.
8. Implemented dynamic applicable coupons based on cart items.
9. Implemented coupon apply on given cart, calulating discont based on coupon type and cart items and returning updated_cart onject.
10. Implemented loose coupling by using interfaces.

# Assumptions

1. Cart Details:
    All items in the cart are valid and mapped to existing products.
2. Coupon Expiration:
    Coupons are assumed to be valid unless explicitly marked as expired (future improvement).
3. Price Validity:
    Product prices are fixed during coupon application (no dynamic price adjustments).
4. No Overlapping Discounts:
    Only one coupon can be applied at a time to the cart or a product.

# Limitations
1. performance: 
    Used synchronous (single-threading ) approch in when number of request per unit time will increase can cause delay in response.

2. Error Handling:
    Basic error handling is implemented, but detailed error codes for specific scenarios (e.g., repetition limit exceeded) are not covered.
3. Dynamic Coupon Types:
    Adding new coupon types requires modifying service logic.
4. NULL check:
    Assumed data is present and mapped correctely. need to check null cases more specifically. 

# Suggested Improvements
1. Advanced Coupon Stacking: 
    Enable application of multiple coupons with precedence rules.
2. Expiration and Scheduling:
    Add expiry dates and activation windows for coupons.
3. Performance Optimization: 
    Use a caching layer (eg. Redis) for faster coupon retrieval and messasing broker for communication (eg. kafka).
4. Detailed Validation: 
    Enhance error handling with descriptive messages for all failure cases.
5. Async API's:
   use Multi-Threaded environment(producer-consumer) More efficient and faster in case of high volume applications.
6. Distributed Architecture:
    Use distributed database to avoid sigle point failure.

