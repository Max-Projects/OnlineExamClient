/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teacher;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author Scott
 */
public class ImageUtils {
    private static final Logger LOGGER;
    private static final Base64.Decoder DECODER;
    
    static {
        LOGGER = Logger.getLogger(ImageUtils.class.getSimpleName());
        DECODER = Base64.getDecoder();
    }
    
    public static Image decode(String base64Str, double width, double height) {
        LOGGER.info("length of base64 string=" + base64Str.length());
        byte[] bytes = DECODER.decode(base64Str);

        return new Image(new ByteArrayInputStream(bytes), width, height, true, true);
    }
}
