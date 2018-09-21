package com.lvmama.bms.pusher.protocol.rpc;

import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 由系统加载器加载tnt-bms-extend中的类
 * 非tnt-bms-extend类从管理后台上传的推送器jar和jar/lib加载
 */
public class RpcClassLoader extends URLClassLoader {

    private URLClassLoader systemClassloader;


    public RpcClassLoader(){
        super(new URL[0], null);
        this.systemClassloader = (URLClassLoader) RpcClassLoader.class.getClassLoader();
    }

    public void build(byte[] msgPusherJar, String classPathRoot) throws IOException {

        //清理历史数据
        FileUtils.deleteQuietly(new File(classPathRoot));

        JarArchiveInputStream jarInput = new JarArchiveInputStream(new ByteArrayInputStream(msgPusherJar));

        Pattern urlPattern = Pattern.compile("^classes/|META-INF/|lib/.+$");

        JarArchiveEntry archiveEntry = null;
        while((archiveEntry = jarInput.getNextJarEntry())!=null) {

            File file = new File(classPathRoot + File.separator + archiveEntry.getName());

            if(archiveEntry.isDirectory()) {
                file.mkdirs();
            } else {
                IOUtils.copy(jarInput, new FileOutputStream(file));
            }

            //添加classpath
            Matcher matcher = urlPattern.matcher(archiveEntry.getName());
            if(matcher.matches()) {
                this.addURL(file.toURI().toURL());
            }

        }

    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if(name.startsWith("com.lvmama.bms.extend.rpc")) {
            return systemClassloader.loadClass(name);
        } else {
            return super.loadClass(name);
        }
    }

}
