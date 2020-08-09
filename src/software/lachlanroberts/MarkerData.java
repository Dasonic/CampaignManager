package software.lachlanroberts;

import java.io.Serializable;

public class MarkerData implements Serializable {
    // Marker contents variables
    public String title;
    public String shortDescription;
    public String fullDescription;

    // Positioning variables
    public double pointX = 0;
    public double pointY = 0;
    public double imgOffsetX;
    public double imgOffsetY;
    public double formVboxXOffset = 0;
    public double formVboxYOffset = 0;

    public MarkerData() {
    }
}
