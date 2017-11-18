package objects;

public class MediaEntity {
    String description;
    String image;
    String name;
    String video;

    public MediaEntity(String name, String image, String video, String description) {
        this.name = name;
        this.image = image;
        this.video = video;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return this.video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
