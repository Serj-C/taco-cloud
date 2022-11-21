package tacos.data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import tacos.Ingredient;
import tacos.Ingredient.Type;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class IngredientRepositoryTests {
    @Autowired
    IngredientRepository ingredientRepo;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void testFindById() {
        Optional<Ingredient> flto = ingredientRepo.findById("FLTO");
        assertThat(flto)
                .isPresent()
                .contains(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));

        Optional<Ingredient> xxxx = ingredientRepo.findById("XXXX");
        assertThat(xxxx).isEmpty();
    }
}
