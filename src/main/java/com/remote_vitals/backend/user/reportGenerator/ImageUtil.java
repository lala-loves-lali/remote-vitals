package com.remote_vitals.backend.user.reportGenerator;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtil {

    public static BufferedImage createGraphImage(int[] medicalData) {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try {
            Object[] vertices = new Object[medicalData.length];

            int xSpacing = 60;
            int yScale = 2;
            int yOffset = 300;

            for (int i = 0; i < medicalData.length; i++) {
                int x = i * xSpacing;
                int y = yOffset - (medicalData[i] * yScale);
                vertices[i] = graph.insertVertex(parent, null, "", x, y, 1, 1); // tiny invisible vertex
            }

            for (int i = 0; i < medicalData.length - 1; i++) {
                graph.insertEdge(parent, null, "", vertices[i], vertices[i + 1]);
            }

        } finally {
            graph.getModel().endUpdate();
        }

        return mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, true, null);
    }

    public static Image bufferedImageToPdfImage(BufferedImage bImage) throws IOException, BadElementException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", baos);
        baos.flush();
        Image image = Image.getInstance(baos.toByteArray());
        baos.close();
        return image;
    }
}
