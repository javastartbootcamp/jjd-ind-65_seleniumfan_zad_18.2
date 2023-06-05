package pl.javastart.couponcalc;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.javastart.couponcalc.Category.*;

public class PriceCalculatorTest {
    private final PriceCalculator priceCalculator = new PriceCalculator();

    @Test
    public void shouldReturnZeroForNoProducts() {
        // when
        double result = priceCalculator.calculatePrice(null, null);

        // then
        assertThat(result).isEqualTo(0.);
    }

    @Test
    public void shouldReturnPriceForSingleProductAndNoCoupons() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Masło", 5.99, FOOD));

        // when
        double result = priceCalculator.calculatePrice(products, null);

        // then
        assertThat(result).isEqualTo(5.99);
    }

    @Test
    public void shouldReturnPriceForMultipleProductsAndNoCoupons() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Masło", 5.39, FOOD));
        products.add(new Product("Mleko", 2.39, FOOD));
        products.add(new Product("Chleb", 8, FOOD));
        products.add(new Product("Lamborghini", 14_000_000, CAR));
        products.add(new Product("Play gituar", 40, ENTERTAINMENT));

        // when
        double result = priceCalculator.calculatePrice(products, null);

        // then
        assertThat(result).isEqualTo(14_000_055.78);
    }

    @Test
    public void shouldReturnPriceForMultipleProductsAndCouponsWithoutPassingCategory() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Masło", 5.39, FOOD));
        products.add(new Product("Mleko", 2.39, FOOD));
        products.add(new Product("Chleb", 8, FOOD));

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(ENTERTAINMENT, 20));
        coupons.add(new Coupon(CAR, 20));

        // when
        double result = priceCalculator.calculatePrice(products, coupons);

        // then
        assertThat(result).isEqualTo(15.78);
    }

    @Test
    public void shouldReturnPriceForSingleProductAndOneCoupon() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Masło", 5.99, FOOD));

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(FOOD, 20));

        // when
        double result = priceCalculator.calculatePrice(products, coupons);

        // then
        assertThat(result).isEqualTo(4.79);
    }

    @Test
    public void shouldReturnPriceForMultipleProductsAndOneCoupon() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Masło", 5.99, FOOD));
        products.add(new Product("Ser", 15.99, FOOD));
        products.add(new Product("Chleb", 8, FOOD));
        products.add(new Product("Dżem", 6.22, FOOD));

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(FOOD, 10));

        // when
        double result = priceCalculator.calculatePrice(products, coupons);

        // then
        assertThat(result).isEqualTo(32.58);
    }

    @Test
    public void shouldReturnPriceForSingleProductAndCouponWithoutCategory() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Masło", 5.99, FOOD));

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(null, 10));

        // when
        double result = priceCalculator.calculatePrice(products, coupons);

        // then
        assertThat(result).isEqualTo(5.39);
    }

    @Test
    public void shouldReturnPriceForThreeProductsAndCouponWithoutCategory() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Masło", 6, FOOD));
        products.add(new Product("Cinema ticket", 60, ENTERTAINMENT));
        products.add(new Product("radio", 34, CAR));

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(null, 10));

        // when
        double result = priceCalculator.calculatePrice(products, coupons);

        // then
        assertThat(result).isEqualTo(90);
    }

    @Test
    public void shouldReturnPriceForFourProductsAndCoupons() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Masło", 6, FOOD));
        products.add(new Product("Cinema ticket", 60, ENTERTAINMENT));
        products.add(new Product("radio", 34, CAR));
        products.add(new Product("Ser", 15, FOOD));

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(FOOD, 10));
        coupons.add(new Coupon(ENTERTAINMENT, 20));

        // when
        double result = priceCalculator.calculatePrice(products, coupons);

        // then
        assertThat(result).isEqualTo(103);
    }

    @Test
    public void shouldReturnPriceWithOverallDiscount() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Masło", 6, FOOD));
        products.add(new Product("Cinema ticket", 60, ENTERTAINMENT));
        products.add(new Product("radio", 34, CAR));
        products.add(new Product("Ser", 15, FOOD));

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(FOOD, 10));
        coupons.add(new Coupon(ENTERTAINMENT, 20));
        coupons.add(new Coupon(null, 50));

        // when
        double result = priceCalculator.calculatePrice(products, coupons);

        // then
        assertThat(result).isEqualTo(57.5);
    }

    @Test
    public void shouldReturnPriceWithCategoryDiscountInsteadOverallDiscount() {
        // given
        List<Product> products = new ArrayList<>();
        products.add(new Product("Masło", 6, FOOD));
        products.add(new Product("Cinema ticket x 4", 200, ENTERTAINMENT));
        products.add(new Product("radio", 34, CAR));
        products.add(new Product("Ser", 15, FOOD));

        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon(FOOD, 10));
        coupons.add(new Coupon(ENTERTAINMENT, 50));
        coupons.add(new Coupon(null, 10));

        // when
        double result = priceCalculator.calculatePrice(products, coupons);

        // then
        assertThat(result).isEqualTo(155);
    }
}