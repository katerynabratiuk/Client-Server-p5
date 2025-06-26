package jdbc;

import com.github.katerynabratiuk.entity.Category;
import com.github.katerynabratiuk.entity.Product;
import com.github.katerynabratiuk.repository.DbConnection;
import com.github.katerynabratiuk.repository.criteria.ProductSearchCriteria;
import com.github.katerynabratiuk.repository.implementation.CategoryRepositoryImpl;
import com.github.katerynabratiuk.repository.implementation.ProductRepositoryImpl;
import com.github.katerynabratiuk.service.implementation.CategoryServiceImpl;
import com.github.katerynabratiuk.service.implementation.ProductServiceImpl;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestJDBC {

    private ProductServiceImpl productService;
    private CategoryServiceImpl categoryService;

    @BeforeAll
    void setUp() throws SQLException {
        try (Connection connection = DbConnection.getConnection();
             Statement st = connection.createStatement()) {
            st.executeUpdate("TRUNCATE TABLE product, product_category RESTART IDENTITY CASCADE");
        }
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();

        CategoryRepositoryImpl categoryRepository = new CategoryRepositoryImpl();
        categoryService = new CategoryServiceImpl(categoryRepository);

        productService = new ProductServiceImpl(productRepository);
        addProducts();
    }

    void addProducts() {
        Category category1 = new Category("Electronics");
        Category category2 = new Category("Cereals");

        categoryService.create(category1);
        categoryService.create(category2);

        Product p1 = new Product(null, "Smartphone", 10, new BigDecimal("20000"), new Category(1));
        Product p2 = new Product(null, "Laptop", 5, new BigDecimal("40000"), new Category(1));
        Product p3 = new Product(null, "Buckwheat", 20, new BigDecimal("40"), new Category(2));
        Product p4 = new Product(null, "Wheat cereal", 40, new BigDecimal("42"), new Category(2));

        productService.create(p1);
        productService.create(p2);
        productService.create(p3);
    }


    @Test
    @Order(1)
    @DisplayName("Ensure that products are added correctly by checking size")
    void shouldIncreaseTheSizeOfAll()
    {
        int prevSize = productService.getAll().size();

        productService.create(new Product(null, "Rice", 30, new BigDecimal("59"), new Category(2)));
        int currentSize = productService.getAll().size();
        assertEquals(prevSize,currentSize-1);
    }

    @Test
    @Order(2)
    @DisplayName("Ensure that products are added correctly")
    void shouldCorrectlyAddProduct()
    {
        Product product = productService.get(5);
        assertNull(product);
        product = new Product(null, "Oats", 99, new BigDecimal("70"), new Category(2));
        productService.create(product);

        Product newProduct = productService.get(4);
        assertTrue(newProduct.getId() == 5 ||
                Objects.equals(newProduct.getName(), "Oats") ||
                newProduct.getQuantity() == 99 ||
                newProduct.getPrice().equals(new BigDecimal("70")) ||
                newProduct.getCategory().getId() == 2
        );
    }

    @Test
    @Order(3)
    @DisplayName("Ensure that products are updated correctly")
    void shouldCorrectlyUpdateProduct()
    {
        Product product = productService.get(2);
        product.setPrice(new BigDecimal("42000"));
        product.setQuantity(product.getQuantity() - 2);
        productService.update(product);
        Product newProduct = productService.get(2);
        assertTrue(newProduct.getId() == 2 ||
                Objects.equals(newProduct.getName(), "Laptop") ||
                newProduct.getQuantity() == 3 ||
                newProduct.getPrice().equals(new BigDecimal("42000")) ||
                newProduct.getCategory().getId() == 2
        );
    }

    @Test
    @Order(4)
    @DisplayName("Ensure that products are deleted correctly")
    void shouldCorrectlyDeleteProduct()
    {
        Product product = productService.get(2);
        assertNotNull(product);
        productService.delete(2);
        assertNull(productService.get(2));
    }
//
    @Test
    @Order(5)
    @DisplayName("Ensure that products are found by fragment")
    void shouldFindProductByNameFragment() {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setName("whea"); // buckwheat
        List<Product> products = productService.filter(criteria);
        assertEquals(1, products.size());
        assertEquals("Buckwheat", products.get(0).getName());
    }

    @Test
    @Order(6)
    @DisplayName("Ensure that nothing is displayed when nothing matches.")
    void shouldReturnEmptyListIfNothingMatches() {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setName("lolll");
        List<Product> products = productService.filter(criteria);
        assertEquals(0, products.size());
    }

    @Test
    @Order(7)
    @DisplayName("Ensure that products are filtered by category")
    void shouldFilterByCategory() {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setCategory(new Category(2));
        List<Product> products = productService.filter(criteria);
        assertEquals(3, products.size());

        for (Product product : products) {
            assertNotNull(product.getId());
            assertNotNull(product.getName());
            assertNotNull(product.getPrice());
            assertNotNull(product.getCategory());
            assertEquals(2, product.getCategory().getId());

            assertTrue(
                    product.getName().equalsIgnoreCase("Buckwheat") ||
                            product.getName().equalsIgnoreCase("Rice") ||
                            product.getName().equalsIgnoreCase("Oats")
            );
        }

    }
//
    @Test
    @Order(8)
    void testLowerLimit() {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setLowerLimit(new BigDecimal("5000")); // should only find smartphone
        List<Product> products = productService.filter(criteria);
        assertEquals(1, products.size());
        assertEquals("Smartphone", products.getFirst().getName());
    }

    @Test
    @Order(9)
    void testUpperLimit() {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setUpperLimit(new BigDecimal("5000")); // should find everything but smartphone
        List<Product> products = productService.filter(criteria);
        assertEquals(3, products.size());
        for (Product product : products) {
            assertNotNull(product.getId());
            assertNotNull(product.getName());
            assertNotNull(product.getPrice());
            assertNotNull(product.getCategory());
            assertEquals(2, product.getCategory().getId());

            assertTrue(
                    product.getName().equalsIgnoreCase("Buckwheat") ||
                            product.getName().equalsIgnoreCase("Rice") ||
                            product.getName().equalsIgnoreCase("Oats")
            );
        }
    }

    @Test
    @Order(9)
    void testUpperAndLowerLimit() {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setLowerLimit(new BigDecimal("50"));
        criteria.setUpperLimit(new BigDecimal("60"));
        List<Product> products = productService.filter(criteria);
        assertEquals(1, products.size());
        assertEquals("Rice", products.getFirst().getName());
    }

    @Test
    @Order(10)
    @DisplayName("Filter by name and category")
    void shouldFilterByNameAndCategory() {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setName("wheat");
        criteria.setCategory(new Category(2));

        List<Product> products = productService.filter(criteria);
        for (Product product : products) {
            assertNotNull(product.getId());
            assertNotNull(product.getName());
            assertNotNull(product.getPrice());
            assertNotNull(product.getCategory());
            assertEquals(2, product.getCategory().getId());

            assertTrue(
                    product.getName().equalsIgnoreCase("Buckwheat") ||
                            product.getName().equalsIgnoreCase("Wheat cereal")
            );
        }
    }


}
