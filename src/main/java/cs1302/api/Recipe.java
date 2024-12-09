package cs1302.api;

import java.util.List;

/**
 *
 *
 */
public class Recipe {
    private String label;
    private String image;
    private List<String> ingredientLines;

    public String getLabel() {
        return label;
    } // getLabel

    public String getImage() {
        return image;
    } // getImage

    public List<String> getIngredientLines() {
        return ingredientLines;
    } // getIngredientLines
} // Recipe
