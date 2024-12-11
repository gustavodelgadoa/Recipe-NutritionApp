package cs1302.api;

/**
 * Represents a search result containing a single recipe.
 */
public class Hit {
    private Recipe recipe;

    /**
     * Returns the recipe associated with this search result.
     *
     * @return associated recipe object.
     */
    public Recipe getRecipe() {
        return recipe;
    }
} // Hit
