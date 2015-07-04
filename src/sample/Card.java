package sample;


import javafx.scene.image.Image;

/**
 * Created by ksofia8 on 6/20/15.
 */
public class Card {
    private Image image;
    private String number;
    private String color;
    private String shape;
    private String fill;

    public Image getImage() {
        return image;
    }

    public String getNumber() {
        return number;
    }

    public String getColor() {
        return color;
    }

    public String getShape() {
        return shape;
    }

    public String getFill() {
        return fill;
    }

    public Card(Image image, String number, String color, String shape, String fill) {
        super();
        this.image = image;
        this.number = number;
        this.color = color;
        this.shape = shape;
        this.fill = fill;
    }

    public boolean compareColorTo(Card card) {
        return this.getColor() == card.getColor();
    }

    public boolean compareNumberTo(Card card) {
        return this.getNumber() == card.getNumber();
    }

    public boolean compareShapeTo(Card card) {
        return this.getShape() == card.getShape();
    }

    public boolean compareFillTo(Card card) {
        return this.getFill() == card.getFill();
    }

}
