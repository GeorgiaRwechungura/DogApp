package georgia.com.dogapp.model;

public class DogBreed {
    public String breeId;
    public  String dogBreed;
    public  String lifeSpan;
    public  String breedGroup;
    public String bredFor;
    public String temperament;
    public  String imageUrl;
    public  int uid;

    public DogBreed(String breeId, String dogBreed, String lifeSpan, String breedGroup, String bredFor, String temperament, String imageUrl) {
        this.breeId = breeId;
        this.dogBreed = dogBreed;
        this.lifeSpan = lifeSpan;
        this.breedGroup = breedGroup;
        this.bredFor = bredFor;
        this.temperament = temperament;
        this.imageUrl = imageUrl;
    }
}
