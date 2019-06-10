package com.jqh.agent;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyClassLoader extends ClassLoader {

    @Override
    public Class<?> findClass(String name) {
        String classPath = getClassPath();
//        String myPath = "file:///E:/项目/studyagent/target/classes" + name.replace(".","/") + ".class";
        String myPath = "file://" + classPath + name.replace(".", "/") + ".class";
        byte[] cLassBytes = null;
        Path path = null;
        try {
            path = Paths.get(new URI(myPath));
            cLassBytes = Files.readAllBytes(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Class clazz = defineClass(name, cLassBytes, 0, cLassBytes.length);
        return clazz;
    }

    public String getClassPath() {
        String path = this.getClass().getResource("/").getPath();
        return path;
    }
}
