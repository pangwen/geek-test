import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Description: 自定义 classloader, 加载编码后的 xlass 文件
 * <p>
 * Created on 2021/6/28.
 *
 * @author <a href="maillto:smile.pangwen@gmail.com">pangwen</a>
 * @version 0.1
 */
public class ClassloaderTest {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        MyClassloader myClassloader = new MyClassloader();
        Path path = Paths.get("Hello.xlass");

        Class<?> cls = loadClass(myClassloader, "Hello", path);
        Method method = cls.getMethod("hello");
        Object o = cls.newInstance();
        method.invoke(o);
    }

    private static Class<?> loadClass(MyClassloader myClassloader, String name, Path path) {

        return myClassloader.loadClassFromFile(name, path.toFile());
    }

    static class MyClassloader extends ClassLoader {

        public Class<?> loadClassFromFile(String name, File file) {
            byte[] bytes = loadClassData(file);
            return defineClass(name, bytes, 0, bytes.length);
        }

        private byte[] loadClassData(File file) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (InputStream in = new FileInputStream(file)) {
                byte[] b = new byte[1024];
                int len;
                while ((len = in.read(b, 0, b.length)) != -1) {
                    out.write(b, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] bytes = out.toByteArray();
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (255 - bytes[i]);
            }
            return bytes;
        }
    }
}
