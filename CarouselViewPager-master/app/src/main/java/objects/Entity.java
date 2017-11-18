package objects;

public class Entity {
    public int imageRes;
    public String titleRes;
    public String description;

    public Entity (int imageRes, String titleRes, String description){
        this.imageRes = imageRes;
        this.titleRes = titleRes;
        this.description = description;
    }

    public int getImage() {
        return this.imageRes;
    }

    public void setImage(int image) {
        this.imageRes = image;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}