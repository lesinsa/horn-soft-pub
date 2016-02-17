import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

/**
 * @author by lesinsa on 17.10.2015.
 */
public class Main {
    public static void main(String[] args) throws MimeTypeParseException {
        MimeType mimeType = new MimeType("application/json; charset=utf-8");
        System.out.println(mimeType);
    }
}
