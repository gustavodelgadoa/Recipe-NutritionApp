package cs1302.api;

import java.util.List;

/**
 * Represents a recipe with a label, image, and list of ingredients.
 *
 */
public class Recipe {
    private String label;
    private String image;
    private List<String> ingredientLines;

    /**
     * Returns the label of the recipe.
     *
     * @return label.
     */

    public String getLabel() {
        return label;
    } // getLabel

    /**
     * Returns the URL of the recipe's image.
     *
     * @return image url.
     */
    public String getImage() {
        return image;
    } // getImage

    /**
     * Returns the list of ingredient descriptions.
     *
     * @return list of ingredients.
     */
    public List<String> getIngredientLines() {
        return ingredientLines;
    } // getIngredientLines
} // Recipe
