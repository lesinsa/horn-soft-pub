package ru.prbb.commons.image;

import org.junit.Test;
import ru.prbb.common.image.MyImage;
import ru.prbb.common.image.ScaleType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * @author MatkhanovAA
 */
public class MyImageTest {

    @Test
    public void test1() throws Exception {
        // тест "для посмотреть на результат"
        MyImage sut = createMyImage("/sample.jpg");
        sut.rescale(128, 128);
        sut.writeTo(new FileOutputStream("target/smaple-128x128.jpg"));
    }

    @Test
    public void testSizes() throws Exception {
        MyImage sut = createMyImage("/sample.jpg");
        assertSizes(sut, 640, 480);
    }

    @Test
    public void testRescaleWidth() throws Exception {
        MyImage sut = createMyImage("/sample.jpg");
        sut.rescale(320, 320);
        assertSizes(sut, 320, 240);
    }

    @Test
    public void testRescaleHeight() throws Exception {
        MyImage sut = createMyImage("/sample.jpg");
        sut.rescale(640, 240);
        assertSizes(sut, 320, 240);
    }

    @Test
    public void testRescaleWidthByMin() throws Exception {
        MyImage sut = createMyImage("/sample.jpg");
        sut.rescale(10, 240, ScaleType.FIT_BY_MIN);
        assertSizes(sut, 320, 240);
    }

    @Test
    public void testRescaleHeightByMin() throws Exception {
        MyImage sut = createMyImage("/sample.jpg");
        sut.rescale(320, 10, ScaleType.FIT_BY_MIN);
        assertSizes(sut, 320, 240);
    }

    @Test
    public void testRescaleEnlarge() throws Exception {
        MyImage sut = createMyImage("/sample.jpg");
        sut.rescale(10, 720, ScaleType.FIT_BY_MIN);
        assertSizes(sut, 960, 720);
    }

    @Test
    public void testTypeJPG() throws Exception {
        MyImage sut = createMyImage("/sample.jpg");
        assertEquals(MyImage.ImageType.JPEG, sut.getType());
        assertEquals("image/jpeg", sut.getContentType());
    }

    @Test
    public void testTypeGIF() throws Exception {
        MyImage sut = createMyImage("/sample.gif");
        assertEquals(MyImage.ImageType.GIF, sut.getType());
        assertEquals("image/gif", sut.getContentType());
    }

    @Test
    public void testTypePNG() throws Exception {
        MyImage sut = createMyImage("/sample.png");
        assertEquals(MyImage.ImageType.PNG, sut.getType());
        assertEquals("image/png", sut.getContentType());
    }

    @Test
    public void testTypeBMP() throws Exception {
        MyImage sut = createMyImage("/sample.bmp");
        assertEquals(MyImage.ImageType.BMP, sut.getType());
        assertEquals("image/bmp", sut.getContentType());
    }

    private MyImage createMyImage(String resourceName) throws IOException {
        InputStream inputStream = MyImageTest.class.getResourceAsStream(resourceName);
        try {
            return MyImage.create(inputStream);
        } finally {
            inputStream.close();
        }
    }

    private void assertSizes(MyImage sut, int expectedWidth, int expectedHeight) {
        assertEquals(expectedWidth, sut.getWidth());
        assertEquals(expectedHeight, sut.getHeight());
    }
}
