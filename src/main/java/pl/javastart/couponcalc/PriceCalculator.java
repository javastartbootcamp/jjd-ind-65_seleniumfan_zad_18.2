package pl.javastart.couponcalc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class PriceCalculator {

    public double calculatePrice(List<Product> products, List<Coupon> coupons) {
        if (Objects.isNull(products) && Objects.isNull(coupons)) {
            return 0;
        }

        if (Objects.isNull(coupons)) {
            return products.stream()
                    .mapToDouble(Product::getPrice)
                    .sum();
        }

        Map<Category, Integer> categoryWithDiscountMap = coupons.stream()
                .collect(Collectors.toMap(Coupon::getCategory, Coupon::getDiscountValueInPercents));

        Map<Category, Double> categoryPriceMap = new HashMap<>();
        for (Product product : products) {
            categoryPriceMap.put(product.getCategory(), categoryPriceMap.getOrDefault(product.getCategory(), 0.0) + product.getPrice());
        }

        double maxDiscountAmount = 0;
        double sum = 0;
        for (Map.Entry<Category, Double> entry : categoryPriceMap.entrySet()) {
            sum += entry.getValue();
            if (categoryWithDiscountMap.containsKey(entry.getKey())) {
                double discountAmount = entry.getValue() * (categoryWithDiscountMap.get(entry.getKey()) / 100.);
                if (discountAmount > maxDiscountAmount) {
                    maxDiscountAmount = discountAmount;
                }
            }
        }

        Optional<Coupon> couponWithoutCategory = coupons.stream()
                .filter(coupon -> Objects.isNull(coupon.getCategory()))
                .findFirst();

        double discountAllProducts = 0;
        if (couponWithoutCategory.isPresent()) {
            discountAllProducts = sum * (couponWithoutCategory.get().getDiscountValueInPercents() / 100.);
        }
        return discountAllProducts > maxDiscountAmount ? roundToTwoPlaces(sum - discountAllProducts) : roundToTwoPlaces(sum - maxDiscountAmount);
    }

    double roundToTwoPlaces(double number) {
        return BigDecimal.valueOf(number).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}