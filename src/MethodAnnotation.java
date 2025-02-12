import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.*;

public class MethodAnnotation {
    public static void main (String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method testMethod = MathTest.class.getDeclaredMethod("test", int.class, int.class);
        if (testMethod.isAnnotationPresent(Test.class)) {
            Test annoTest = testMethod.getAnnotation(Test.class);

            testMethod.invoke(new MathTest(), annoTest.a(), annoTest.b());
        }
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface Test {
    int a();
    int b();
}

class MathTest {
    @Test(a = 2, b = 5)
    public void test(int a, int b) {
        double result = Math.pow(b, a);
        System.out.println(result);
    }
}