package com.spacebeaverstudios.sqcore.objects;

import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;

public abstract class SQMapRenderer extends MapRenderer {
    public final boolean canItemFrame;

    public SQMapRenderer(boolean canItemFrame) {
        super(true);
        this.canItemFrame = canItemFrame;
    }

    @SuppressWarnings("unused")
    protected void fillCanvas(MapCanvas mapCanvas, byte color) {
        rectangle(mapCanvas, 0, 0, 128, 128, color);
    }

    @SuppressWarnings("unused")
    protected void rectangle(MapCanvas mapCanvas, int x, int y, int width, int height, byte color) {
        for (int xCounter = x; xCounter < x + width; xCounter++) {
            for (int yCounter = y; yCounter < y + height; yCounter++) {
                mapCanvas.setPixel(xCounter, yCounter, color);
            }
        }
    }
}
