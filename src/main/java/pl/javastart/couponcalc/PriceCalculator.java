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

        double totalSum = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
        if (Objects.isNull(coupons)) {
            return totalSum;
        }
        Map<Category, Double> categoryPriceMap = sumOfPricesByCategory(products);
        double maxDiscountAmount = findMaxDiscountByCategory(coupons, categoryPriceMap);
        maxDiscountAmount = calculateMaxTotalDiscount(coupons, totalSum, maxDiscountAmount);
        return roundToTwoPlaces(totalSum - maxDiscountAmount);
    }

    private static double calculateMaxTotalDiscount(List<Coupon> coupons, double totalSum, double maxDiscountAmount) {
        Optional<Coupon> couponWithoutCategory = coupons.stream()
                .filter(coupon -> Objects.isNull(coupon.getCategory()))
                .findFirst();
        if (couponWithoutCategory.isPresent()) {
            double discountAllProducts = totalSum * (couponWithoutCategory.get().getDiscountValueInPercents() / 100.);
            maxDiscountAmount = Math.max(maxDiscountAmount, discountAllProducts);
        }
        return maxDiscountAmount;
    }

    private static double findMaxDiscountByCategory(List<Coupon> coupons, Map<Category, Double> categoryPriceMap) {
        Map<Category, Integer> categoryWithDiscountMap = coupons.stream()
                .collect(Collectors.toMap(Coupon::getCategory, Coupon::getDiscountValueInPercents));
        double maxDiscountAmount = 0;
        for (Map.Entry<Category, Double> entry : categoryPriceMap.entrySet()) {
            if (categoryWithDiscountMap.containsKey(entry.getKey())) {
                double discountAmount = entry.getValue() * (categoryWithDiscountMap.get(entry.getKey()) / 100.);
                if (discountAmount > maxDiscountAmount) {
                    maxDiscountAmount = discountAmount;
                }
            }
        }
        return maxDiscountAmount;
    }

    private static Map<Category, Double> sumOfPricesByCategory(List<Product> products) {
        Map<Category, Double> categoryPriceMap = new HashMap<>();
        for (Product product : products) {
            categoryPriceMap.merge(product.getCategory(), product.getPrice(), Double::sum);
        }
        return categoryPriceMap;
    }

    double roundToTwoPlaces(double number) {
        return BigDecimal.valueOf(number).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}