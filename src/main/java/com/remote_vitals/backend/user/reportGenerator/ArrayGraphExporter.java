package com.remote_vitals.backend.user.reportGenerator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

public class ArrayGraphExporter {

    private int[][] dataSeries;
    private String[] seriesNames;
    private String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private Color[] lineColors = {Color.GREEN, Color.ORANGE, Color.BLUE, Color.RED};

    public ArrayGraphExporter(int[][] dataSeries, String[] seriesNames) {
        this.dataSeries = dataSeries;
        this.seriesNames = seriesNames;
    }

    public BufferedImage createPicture() {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try {
            int xSpacing = 80;
            int yScale = 2;
            int yOffset = 300;

            // Draw multiple data series
            for (int s = 0; s < dataSeries.length; s++) {
                Object[] points = new Object[dataSeries[s].length];

                // Add invisible points (vertices) for graph lines
                for (int i = 0; i < dataSeries[s].length; i++) {
                    int x = i * xSpacing + 60;
                    int y = yOffset - (dataSeries[s][i] * yScale);
                    points[i] = graph.insertVertex(parent, null, "",
                            x, y, 1, 1,
                            "strokeColor=none;fillColor=none");
                }

                // Connect points with colored lines
                for (int i = 0; i < points.length - 1; i++) {
                    graph.insertEdge(parent, null, "",
                            points[i], points[i + 1],
                            "strokeColor=" + getHexColor(lineColors[s % lineColors.length]) + ";strokeWidth=2;endArrow=none");
                }
            }

            // Add month labels under the graph
            for (int i = 0; i < months.length; i++) {
                int x = i * xSpacing + 60;
                int y = yOffset + 20;
                graph.insertVertex(parent, null, months[i],
                        x, y, 30, 20,
                        "strokeColor=none;fillColor=none;fontColor=black");
            }

            // Add color-coded legend at bottom
            for (int s = 0; s < seriesNames.length; s++) {
                graph.insertVertex(parent, null, seriesNames[s],
                        100 + s * 150, yOffset + 60, 100, 20,
                        "strokeColor=none;fillColor=" + getHexColor(lineColors[s % lineColors.length]) + ";fontColor=black");
            }

        } finally {
            graph.getModel().endUpdate();
        }

        try {
            // Render graph to image
            float scale = 4.0f; // Higher scale = higher resolution
            BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, scale, Color.WHITE, true, null);

            // OPTIONAL: Save image to file for testing/debugging
             File imgFile = new File("line_graph_only_edges.png");
             ImageIO.write(image, "PNG", imgFile);
             System.out.println("Graph exported to: " + imgFile.getAbsolutePath());

            return image;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper to convert Color to hex for mxGraph styles
    private String getHexColor(Color color) {
        return String.format("#%02x%02x%02x",
                color.getRed(), color.getGreen(), color.getBlue());
    }
}
