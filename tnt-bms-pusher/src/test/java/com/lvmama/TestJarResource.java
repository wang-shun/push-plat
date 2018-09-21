package com.lvmama;

import com.lvmama.bms.pusher.support.MsgCacheManager;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TestJarResource {

    @Test
    public void testLoad() {

        Integer t = 10;
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{new URL("file:C:\\Users\\Administrator\\Desktop\\推送\\RpcPusher\\RpcPusher.jar")});

            classLoader.loadClass(Integer.class.getName());

            Enumeration<URL> urls = classLoader.getResources("lib");

            while (urls.hasMoreElements()) {
                File file = new File(urls.nextElement().toURI());
                for (File f : file.listFiles()) {
                    System.out.println(f.getPath());
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testLoadFolder() {
        try {

            URLClassLoader classLoader = new URLClassLoader(new URL[] {new URL("file:C:\\Users\\Administrator\\Desktop\\推送\\RpcPusher\\RpcPusher.jar")}, null);

            List<URL> libUrls = new ArrayList<>();
            JarFile localJarFile = new JarFile(new File("C:\\Users\\Administrator\\Desktop\\推送\\RpcPusher\\RpcPusher.jar"));
            Enumeration<JarEntry> entries = localJarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if(jarEntry.getName().startsWith("lib/")) {
                    libUrls.add(classLoader.getResource(jarEntry.getName()));
                    System.out.println(jarEntry.getName());
                }
            }

            libUrls.add(new URL("file:C:\\Users\\Administrator\\Desktop\\推送\\RpcPusher\\RpcPusher.jar"));

            classLoader = new URLClassLoader(libUrls.toArray(new URL[0]), null);
            Class targetClass = classLoader.loadClass("org.apache.commons.codec.digest.DigestUtils");
            System.out.println(targetClass);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
