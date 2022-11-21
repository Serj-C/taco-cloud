package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tacos.Ingredient.Type;
import tacos.data.IngredientRepository;

@SpringBootApplication
public class TacoCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(TacoCloudApplication.class, args);
	}

	// List of ingredients to be persisted when spring.jpa.hibernate.ddl-auto=create-drop
	// Below bean has to be removed/commented if spring.jpa.hibernate.ddl-auto=none (data from data.sql is inserted
	// instead).
	@Bean
	public CommandLineRunner dataLoader(IngredientRepository ingredientRepo) {
		return args -> {
			ingredientRepo.save(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));
			ingredientRepo.save(new Ingredient("COTO", "Corn Tortilla", Type.WRAP));
			ingredientRepo.save(new Ingredient("GRBF", "Ground Beef", Type.PROTEIN));
			ingredientRepo.save(new Ingredient("CARN", "Carnitas", Type.PROTEIN));
			ingredientRepo.save(new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
			ingredientRepo.save(new Ingredient("LETC", "Lettuce", Type.VEGGIES));
			ingredientRepo.save(new Ingredient("CHED", "Cheddar", Type.CHEESE));
			ingredientRepo.save(new Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
			ingredientRepo.save(new Ingredient("SLSA", "Salsa", Type.SAUCE));
			ingredientRepo.save(new Ingredient("SRCR", "Sour Cream", Type.SAUCE));
		};
	}
}
