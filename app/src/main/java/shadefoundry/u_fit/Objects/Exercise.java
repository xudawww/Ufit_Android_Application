package shadefoundry.u_fit.Objects;

import java.io.Serializable;

public class Exercise implements Serializable {
    private Long reps;
    private Long lastPerformDate;
    private Long image;
    private Long sets;

    private Long nextPerformedDate;



    private String exerciseID,title,description,imageUrl,highScore,videoUrl,category;


    public Exercise(){}
    public Exercise(String exerciseID, String title, String description, String imageUrl, Long image, Long reps, String highscore, String videoUrl, Long lastPerformDate, String category,Long sets) {
        this.exerciseID = exerciseID;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.image = image;
        this.reps = reps;
        this.highScore = highscore;
        this.videoUrl = videoUrl;
        this.lastPerformDate = lastPerformDate;
        this.category = category;
        this.sets = sets;
    }

    //getters and setters




    public String getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(String exerciseID) {
        this.exerciseID = exerciseID;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public Long getSets() {
        return sets;
    }

    public void setSets(Long sets) {
        this.sets = sets;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getImage() {
        return image;
    }

    public void setImage(Long image) {
        this.image = image;
    }

    public Long getReps() {
        return reps;
    }

    public void setReps(Long reps) {
        this.reps = reps;
    }

    public Long getLastPerformDate() {
        return lastPerformDate;
    }

    public void setLastPerformDate(Long lastPerformDate) {
        this.lastPerformDate = lastPerformDate;
    }

    public String getHighscore() {
        return highScore;
    }

    public void setHighscore(String highscore) {
        this.highScore = highscore;
    }

    public String getEquipment() {
        return videoUrl;
    }

    public void setEquipment(String equipment) {
        this.videoUrl = equipment;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public Long getNextPerformedDate() { return nextPerformedDate; }

    public void setNextPerformedDate(Long nextPerformedDate) { this.nextPerformedDate = nextPerformedDate;
    }
}
