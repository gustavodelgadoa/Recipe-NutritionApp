package cs1302.api;

import java.util.List;
import java.util.Map;

/**
 * Represents the nutritional response obtained from the Nutrition Analysis API.
 * Class holds the detailed nutritional data for a recipe based on its ingredients.
 */
public class NutritionResponse {

    /**
     * Instance variables declared within class.
     */

    private String uri;
    private double calories;
    private double totalWeight;

    private List<String> dietLabels;
    private List<String> healthLabels;
    private List<String> cautions;

    private double totalCO2Emissions;
    private String co2EmissionsClass;

    private Map<String, Nutrient> totalNutrients;
    private Map<String, Nutrient> totalDaily;

    private List<Ingredient> ingredients;
    private Map<String, Nutrient> totalNutrientsKCal;

    /** Getter methods. */

    /**
     * Returns the URI.
     *
     * @return uri.
     */
    public String getUri() {
        return uri;
    }

    /**
     * Returns the calories.
     *
     * @return calories.
     */
    public double getCalories() {
        return calories;
    }

    /**
     * Returns totalWeight.
     *
     * @return totalWweight.
     */
    public double getTotalWeight() {
        return totalWeight;
    }

    /**
     * Returns diet labels.
     *
     * @return dietLabels.
     */
    public List<String> getDietLabels() {
        return dietLabels;
    }

    /**
     * Returns health labels.
     *
     * @return healthLabels.
     */
    public List<String> getHealthLabels() {
        return healthLabels;
    }

    /**
     * Returns cautions.
     *
     * @return cautions.
     */
    public List<String> getCautions() {
        return cautions;
    }

    /**
     * Returns total CO2 emissions.
     *
     * @return totalCO2Emissions.
     */
    public double getTotalCO2Emissions() {
        return totalCO2Emissions;
    }

    /**
     * Returns co2 emission class.
     *
     * @return co2EmissionClass.
     */
    public String getCo2EmissionsClass() {
        return co2EmissionsClass;
    }

    /**
     * Returns total nutrients.
     *
     * @return totalNutrients.
     */
    public Map<String, Nutrient> getTotalNutrients() {
        return totalNutrients;
    }

    /**
     * Returns total daily.
     *
     * @return totalDaily.
     */
    public Map<String, Nutrient> getTotalDaily() {
        return totalDaily;
    }

    /**
     * Returns ingredients.
     *
     * @return ingredients.
     */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Returns total nutrients k cal.
     *
     * @return totalNutrientsKCal.
     */
    public Map<String, Nutrient> getTotalNutrientsKCal() {
        return totalNutrientsKCal;
    }

    /**
     * Represents a nutrient with its label, quantity, and unit.
     *
     */
    public static class Nutrient {
        private String label;
        private double quantity;
        private String unit;

        /**
         * Returns the label of the nutrient.
         *
         * @return label.
         */
        public String getLabel() {
            return label;
        }

        /**
         * Returns the quantity of the nutrient.
         *
         * @return quantity.
         */
        public double getQuantity() {
            return quantity;
        }

        /**
         * Returns the unit of measurement for the nutrient.
         *
         * @return unit.
         */
        public String getUnit() {
            return unit;
        }
    } // Nutrient

    /**
     * Represents an ingredient and its parsed details.
     *
     */
    public static class Ingredient {
        private String text;
        private List<ParsedIngredient> parsed;

        /**
         * Returns the raw text of the ingredient.
         *
         * @return text.
         */
        public String getText() {
            return text;
        }

        /**
         * Retuns a list of parsed details for the ingredients.
         *
         * @return parsed.
         */
        public List<ParsedIngredient> getParsed() {
            return parsed;
        }

        /**
         * Represents a parsed version of an ingredient with details.
         *
         */
        public static class ParsedIngredient {
            private double quantity;
            private String measure;
            private String food;
            private String foodMatch;
            private double weight;
            private Map<String, Nutrient> nutrients;

            /**
             * Returns the quantity of the parsed ingredient.
             *
             * @return quantity.
             */
            public double getQuantity() {
                return quantity;
            }

            /**
             * Returns the measure of the parsed ingredient.
             *
             * @return measure.
             */
            public String getMeasure() {
                return measure;
            }

            /**
             * Returns the name of the food item.
             *
             * @return food.
             */
            public String getFood() {
                return food;
            }

            /**
             * Returns the matched food item.
             *
             * @return foodMatch.
             */
            public String getFoodMatch() {
                return foodMatch;
            }

            /**
             * Returns the weight.
             *
             * @return weight.
             */
            public double getWeight() {
                return weight;
            }

            /**
             * Returns the nutrients.
             *
             * @return nutrients.
             */
            public Map<String, Nutrient> getNutrients() {
                return nutrients;
            }
        } // ParsedIngredient
    } // Ingredient
} // NutritionResponse
