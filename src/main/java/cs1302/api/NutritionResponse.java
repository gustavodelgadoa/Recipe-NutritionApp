package cs1302.api;

import java.util.List;
import java.util.Map;

/**
 *
 *
 */
public class NutritionResponse {
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

    // Getter methods
    public String getUri() {
        return uri;
    }

    public double getCalories() {
        return calories;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public List<String> getDietLabels() {
        return dietLabels;
    }

    public List<String> getHealthLabels() {
        return healthLabels;
    }

    public List<String> getCautions() {
        return cautions;
    }

    public double getTotalCO2Emissions() {
        return totalCO2Emissions;
    }

    public String getCo2EmissionsClass() {
        return co2EmissionsClass;
    }

    public Map<String, Nutrient> getTotalNutrients() {
        return totalNutrients;
    }

    public Map<String, Nutrient> getTotalDaily() {
        return totalDaily;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public Map<String, Nutrient> getTotalNutrientsKCal() {
        return totalNutrientsKCal;
    }

    // Nutrient Class
    public static class Nutrient {
        private String label;
        private double quantity;
        private String unit;

        public String getLabel() {
            return label;
        }

        public double getQuantity() {
            return quantity;
        }

        public String getUnit() {
            return unit;
        }
    } // Nutrient

    // Ingredient Class
    public static class Ingredient {
        private String text;
        private List<ParsedIngredient> parsed;

        public String getText() {
            return text;
        }

        public List<ParsedIngredient> getParsed() {
            return parsed;
        }

        // ParsedIngredient Class
        public static class ParsedIngredient {
            private double quantity;
            private String measure;
            private String food;
            private String foodMatch;
            private double weight;
            private Map<String, Nutrient> nutrients;

            public double getQuantity() {
                return quantity;
            }

            public String getMeasure() {
                return measure;
            }

            public String getFood() {
                return food;
            }

            public String getFoodMatch() {
                return foodMatch;
            }

            public double getWeight() {
                return weight;
            }

            public Map<String, Nutrient> getNutrients() {
                return nutrients;
            }
        } // ParsedIngredient
    } // Ingredient
} // NutritionResponse
