package ba.fpmoz.yumai.model;

import java.util.Dictionary;
import java.util.Map;

public class Recipe {
    private String title;
    private String search;
    private String category;
    private String complexity;
    private String cooking_time;
    private String servings;
    private String description;

    private Map<String, Object> ingredients;
    private String image;
    private boolean AI;
    private String author_id;
    private String timestamp;

    public Recipe() {
        // Empty constructor
    }
    public Recipe (String title, String search, String category, String complexity, String cooking_time, String servings, String description, Map<String, Object> ingredients, String recipeImage, boolean AI, String author_id, String timestamp) {
        this.title = title;
        this.search =search;
        this.category = category;
        this.complexity = complexity;
        this.cooking_time = cooking_time;
        this.servings = servings;
        this.description = description;
        this.ingredients = ingredients;
        this.image = recipeImage;
        this.AI = AI;
        this.author_id = author_id;
        this.timestamp = timestamp;

    }




    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSearch() {
        return search;
    }


    public void setSearch(String search) {
        this.search = search;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) { this.complexity = complexity; }

    public String getCooking_time() {
        return cooking_time;
    }

    public void setCooking_time(String cooking_time) {
        this.cooking_time = cooking_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription (String description) { this.description = description; }

    public Map<String, Object> getIngredients() { return ingredients;}

    public void setIngredients (Map<String, Object> ingredients) {this.ingredients = ingredients;}

    public String getAuthor_id() { return author_id;}

    public void setAuthor_id(String author_id) { this.author_id = author_id;}

    public String getTimestamp() {return timestamp;}

    public void setTimestamp(String timestamp) {this.timestamp = timestamp;}

    public boolean getAI() {return AI;}

    public void setAI(boolean AI) {this.AI = AI;}


    public String getServings() {return servings;}

    public void setServings(String servings) {this.servings = servings;}
}
