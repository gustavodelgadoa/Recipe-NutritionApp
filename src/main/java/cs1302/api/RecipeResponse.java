package cs1302.api;

import java.util.List;

/**
 * Represents a response from the Recipe Search API.
 */
public class RecipeResponse {
    private List<Hit> hits;

    /**
     * Returns the number of hits.
     *
     * @return hits.
     */
    public List<Hit> getHits() {
        return hits;
    }
} // RecipeResponse
