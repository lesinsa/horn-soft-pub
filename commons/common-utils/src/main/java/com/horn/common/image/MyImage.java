package com.horn.common.image;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * @author MatkhanovAA
 */
public final class MyImage {
    public static final int READ_BUFFER_SIZE = 4096;

    private byte[] buffer;
    private BufferedImage image;
    private ImageType imageType;
    private int width;
    private int height;

    private MyImage(byte[] buffer) {
        this.buffer = buffer;
        innerInit(false);
    }

    public static MyImage create(InputStream inputStream) throws IOException {
        // буфферизируем входной поток данных
        byte[] buffer = readAllBytes(inputStream);
        return new MyImage(buffer);
    }

    public static MyImage create(byte[] data) {
        try {
            return create(new ByteArrayInputStream(data));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(inputStream.available());
        byte[] buffer = new byte[READ_BUFFER_SIZE];
        int r = inputStream.read(buffer);
        while (r != -1) {
            baos.write(buffer, 0, r);
            r = inputStream.read(buffer);
        }
        buffer = baos.toByteArray();
        return buffer;
    }

    public int getWidth() {
        return image != null ? image.getWidth() : width;
    }

    public int getHeight() {
        return image != null ? image.getHeight() : height;
    }

    public ImageType getType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public String getContentType() {
        return imageType.getMimeType();
    }

    public void rescale(int width, int height) {
        rescale(width, height, ScaleType.FIT_BOTH);
    }

    public void rescale(int width, int height, ScaleType scaleType) {
        innerInit(true);

        int w0 = getWidth(), h0 = getHeight(), w, h;
        // Масштабируем изображение
        if (scalePrecondition(width, height, w0, h0, scaleType)) {
            // масштабируем по ширине
            w = width;
            h = width * h0 / w0;
        } else {
            // масштабируем по высоте
            w = height * w0 / h0;
            h = height;
        }

        int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
        BufferedImage thumbnailImage = new BufferedImage(w, h, type);
        Graphics2D graphics = thumbnailImage.createGraphics();

        graphics.setComposite(AlphaComposite.Src);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.drawImage(image, 0, 0, w, h, null);
        graphics.dispose();

        image = thumbnailImage;
    }

    private boolean scalePrecondition(int width, int height, int w0, int h0, ScaleType scaleType) {
        if (scaleType == ScaleType.FIT_BOTH) {
            return width * h0 < height * w0;
        } else if (scaleType == ScaleType.FIT_BY_MIN) {
            return width * h0 > height * w0;
        } else {
            throw new IllegalStateException("Unknown scale type");
        }
    }

    public void writeTo(OutputStream output) throws IOException {
        writeTo(imageType, output);
    }

    public void writeTo(ImageType type, OutputStream output) throws IOException {
        if (image != null) {
            ImageIO.write(image, type.formatName, output);
        } else {
            output.write(buffer);
        }
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeTo(out);
        return out.toByteArray();
    }

    public byte[] toByteArray(ImageType type) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeTo(type, out);
        return out.toByteArray();
    }

    private void innerInit(boolean isFullInit) {
        if (image != null) {
            return;
        }
        try {
            try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(new ByteArrayInputStream(buffer))) {
                Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);
                if (!imageReaders.hasNext()) {
                    throw new IllegalArgumentException("Graphical type is not supported");
                }
                ImageReader reader = imageReaders.next();
                try {
                    reader.setInput(imageInputStream);
                    this.width = reader.getWidth(0);
                    this.height = reader.getHeight(0);
                    this.imageType = ImageType.findByFormatName(reader.getFormatName());
                    if (isFullInit) {
                        image = reader.read(0);
                        buffer = null;
                    }
                } finally {
                    reader.dispose();
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public enum ImageType {
        JPEG("jpeg", "image/jpeg"), PNG("png", "image/png"), GIF("gif", "image/gif"), BMP("bmp", "image/bmp");

        private final String formatName;
        private final String mimeType;

        ImageType(String formatName, String mimeType) {
            this.formatName = formatName;
            this.mimeType = mimeType;
        }

        public static ImageType findByFormatName(String name) {
            for (ImageType type : ImageType.values()) {
                if (type.formatName.equalsIgnoreCase(name)) {
                    return type;
                }
            }
            return null;
        }

        public static ImageType findByMimeType(String name) {
            for (ImageType type : ImageType.values()) {
                if (type.mimeType.equalsIgnoreCase(name)) {
                    return type;
                }
            }
            return null;
        }

        public String getMimeType() {
            return mimeType;
        }
    }
}
