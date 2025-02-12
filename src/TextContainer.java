import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.*;

public class TextContainer {

    public static void main (String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TextSaver test = new TextSaver();
        String saverMethod = test.getSaverMethod();
        if (saverMethod.isEmpty()) {
            throw new NoSuchMethodException("Please specify class annotation");
        }

        if (TextSaver.class.isAnnotationPresent(SaverFinder.class)) {
            SaverFinder annoSF = TextSaver.class.getAnnotation(SaverFinder.class);
            String saverName = annoSF.saverName();

            Method saveMethod = TextSaver.class.getDeclaredMethod(saverName, String.class);
            if (saveMethod.isAnnotationPresent(Saver.class)) {
                Saver annoSaver = saveMethod.getAnnotation(Saver.class);
                saveMethod.invoke(new TextSaver(), annoSaver.targetPath());
            }
        }
    }

}

@SaverFinder(saverName = "save")
class TextSaver {
    String text = "Any text for saving\nInto .txt file";

    public String getSaverMethod() {
        if (TextSaver.class.isAnnotationPresent(SaverFinder.class)) {
            return TextSaver.class.getAnnotation(SaverFinder.class).saverName();
        }
        return "";
    }

    @Saver(targetPath = "/tmp/saver_test.txt")
    public void save (String targetPath) throws FileNotFoundException {
        System.out.println(targetPath);
        try (PrintWriter out = new PrintWriter(targetPath)) {
            out.println(text);
        } catch (FileNotFoundException $ex) {
            System.out.println("Can't save file " + targetPath);
        }
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Saver {
    String targetPath();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface SaverFinder {
    String saverName();
}