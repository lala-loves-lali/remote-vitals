package com.remote_vitals.backend.reportGenerator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

/**
 * Creates clean graph visualizations for a single vital type.
 * Focuses only on the graph itself without additional text or labels.
 */
public class SingleVitalGraphExporter {

    private int[] values;
    private String[] dateLabels;
    private String vitalType;
    private Color lineColor = Color.BLUE;
    private Color pointColor = new Color(0, 102, 204);

    /**
     * Constructor for SingleVitalGraphExporter
     * 
     * @param values Array of vital sign values
     * @param dateLabels Array of date labels corresponding to values
     * @param vitalType Type of vital sign
     */
    public SingleVitalGraphExporter(int[] values, String[] dateLabels, String vitalType) {
        this.values = values;
        this.dateLabels = dateLabels;
        this.vitalType = vitalType;
        
        // Set color based on vital type
        if (vitalType.contains("Blood Pressure")) {
            this.lineColor = Color.RED;
        } else if (vitalType.contains("Heart Rate")) {
            this.lineColor = new Color(255, 51, 51); // Bright red
        } else if (vitalType.contains("Temperature")) {
            this.lineColor = new Color(255, 153, 51); // Orange
        } else if (vitalType.contains("Respiratory")) {
            this.lineColor = new Color(51, 153, 255); // Light blue
        }
    }

    /**
     * Creates a clean graph visualization.
     * 
     * @return BufferedImage of the graph
     */
    public BufferedImage createPicture() {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try {
            // Calculate graph dimensions
            int xSpacing = 80;
            int yScale = 2;
            int yOffset = 300;
            int graphWidth = (values.length * xSpacing) + 120;
            int graphHeight = yOffset + 100;
            
            // Find min and max values to scale properly
            int maxValue = Integer.MIN_VALUE;
            int minValue = Integer.MAX_VALUE;
            for (int value : values) {
                maxValue = Math.max(maxValue, value);
                minValue = Math.min(minValue, value);
            }
            
            // Add some padding to the range
            int range = Math.max(maxValue - minValue, 10);
            yScale = (int) Math.max(1, (yOffset - 100) / range);
            
            // Create points for the graph
            Object[] points = new Object[values.length];
            
            // Add graph points
            for (int i = 0; i < values.length; i++) {
                int x = i * xSpacing + 60;
                int y = yOffset - ((values[i] - minValue) * yScale) - 50;
                
                // Create a visible point marker
                points[i] = graph.insertVertex(parent, null, "",
                        x, y, 6, 6,
                        "shape=ellipse;fillColor=" + getHexColor(pointColor) + ";strokeColor=none");
            }
            
            // Connect points with lines
            for (int i = 0; i < points.length - 1; i++) {
                graph.insertEdge(parent, null, "",
                        points[i], points[i + 1],
                        "strokeColor=" + getHexColor(lineColor) + ";strokeWidth=2;endArrow=none");
            }
            
            // Add date labels (using fewer labels if there are many points)
            int labelInterval = Math.max(1, values.length / 5);
            for (int i = 0; i < dateLabels.length; i += labelInterval) {
                int x = i * xSpacing + 60;
                int y = yOffset + 20;
                graph.insertVertex(parent, null, dateLabels[i],
                        x - 15, y, 30, 20,
                        "strokeColor=none;fillColor=none;fontColor=black;fontSize=10");
            }
            
            // Add value labels on the y-axis
            int valueInterval = Math.max(1, range / 5);
            for (int i = 0; i <= 5; i++) {
                int value = minValue + (i * valueInterval);
                int y = yOffset - ((value - minValue) * yScale) - 50;
                graph.insertVertex(parent, null, String.valueOf(value),
                        20, y - 10, 30, 20,
                        "strokeColor=none;fillColor=none;fontColor=black;fontSize=9");
            }

        } finally {
            graph.getModel().endUpdate();
        }

        try {
            // Render graph to image with white background
            float scale = 3.0f; // Higher scale = higher resolution
            BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, scale, Color.WHITE, true, null);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper to convert Color to hex for mxGraph styles
     */
    private String getHexColor(Color color) {
        return String.format("#%02x%02x%02x",
                color.getRed(), color.getGreen(), color.getBlue());
    }
} 