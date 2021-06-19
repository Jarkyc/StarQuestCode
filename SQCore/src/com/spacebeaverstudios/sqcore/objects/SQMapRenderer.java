package com.spacebeaverstudios.sqcore.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public abstract class SQMapRenderer extends MapRenderer {
    public final boolean canItemFrame;
    private int lastTickRendered = -10;

    public SQMapRenderer(boolean canItemFrame) {
        super(true);
        this.canItemFrame = canItemFrame;
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if (Bukkit.getCurrentTick() - 10 >= lastTickRendered) {
            lastTickRendered = Bukkit.getCurrentTick();
            customRender(mapView, mapCanvas, player);
        }
    }

    public abstract void customRender(MapView mapView, MapCanvas mapCanvas, Player player);

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
