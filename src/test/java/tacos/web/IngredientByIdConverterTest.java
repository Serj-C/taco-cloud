package tacos.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.data.IngredientRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IngredientByIdConverterTest {
    private IngredientByIdConverter converter;

    @BeforeEach
    void setUp() {
        IngredientRepository ingredientRepo = mock(IngredientRepository.class);
        when(ingredientRepo.findById("AAAA"))
                .thenReturn(Optional.of(new Ingredient("AAAA", "TEST INGREDIENT", Type.CHEESE)));
        when(ingredientRepo.findById("ZZZZ"))
                .thenReturn(Optional.empty());

        this.converter = new IngredientByIdConverter(ingredientRepo);
    }

    @Test
    void testShouldReturnValueWhenPresent() {
        assertThat(converter.convert("AAAA"))
                .isEqualTo(new Ingredient("AAAA", "TEST INGREDIENT", Type.CHEESE));
    }

    @Test
    void testShouldReturnNullWhenMissing() {
        assertThat(converter.convert("ZZZZ")).isNull();
    }
}
