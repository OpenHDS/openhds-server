package org.openhds.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;





public class ZipMaker {

    private File zipFile;
    private List<File> filesToZip;

    public ZipMaker(String zipFile){
        this(new File(zipFile));
    }

    public ZipMaker(File zipFile){
        this.zipFile = zipFile;
        this.filesToZip = new ArrayList<File>();
    }

    public ZipMaker(File zipFile, File fileToZip){
        this(zipFile);
        addFile(fileToZip);
    }

    public ZipMaker(String zipFile, String fileToZip){
        this(zipFile);
        addFile(fileToZip);
    }

    public ZipMaker(File zipFile, Collection<? extends File> filesToZip){
        this(zipFile);
        addFiles(filesToZip);
    }

    public ZipMaker(String zipFile, List<String> filesToZip){
        this(zipFile);
        addFiles(filesToZip);
    }

    public void addFile(File file){
        this.filesToZip.add(file);
    }

    public void addFile(String file){
        this.filesToZip.add(new File(file));
    }

    public void addFiles(Collection<? extends File> files){
        this.filesToZip.addAll(files);
    }

    public void addFiles(List<String> files){
        for (String file : files){
            addFile(file);
        }
    }

    public static String changeExtToZip(String filename){
        int point = filename.lastIndexOf(".");

        if (point == -1){
            return filename+".zip";
        }

        if (filename.toLowerCase().endsWith(".zip")){
            return filename;
        }

        String nf = filename.substring(0, point)+".zip";

        if (nf.equalsIgnoreCase(filename)){
            return filename + ".zip";
        }

        return nf;
    }

    public static File changeExtToZip(File file){
        String filename = file.toString();
        String newFilename = changeExtToZip(filename);
        return new File(newFilename);
    }

    public boolean makeZip(){
        return xmakeZip(this.zipFile, this.filesToZip);
    }

    private boolean xmakeZip(File zipFile, List<File> sourceFiles) {

        Map<String, String> files = GetAllFiles(sourceFiles);

        try {

            //create byte buffer
            byte[] buffer = new byte[200 * 1024];

            FileOutputStream fout = new FileOutputStream(zipFile);
            ZipOutputStream zout = new ZipOutputStream(fout);


            for (String sourceFile : files.keySet()) {

                String zipEntryFile = files.get(sourceFile);

                FileInputStream fin = new FileInputStream(sourceFile);
                zout.putNextEntry(new ZipEntry(zipEntryFile));

                int length;

                while ((length = fin.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }

                fin.close();
            }

            zout.closeEntry();
            zout.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

    private Map<String, String> GetAllFiles(List<File> sourceFiles) {
        Map<String, String> map = new HashMap<String, String>();

        for (File file : sourceFiles) {
            if (file.isFile()){
                map.put(file.toString(), file.getName());
                continue;
            }

            if (file.isDirectory()){
                String path = file.getParent().toString();
                Map<String, String> map2 = getFiles(path, file.listFiles());

                if (map2.size()>0){
                    map.putAll(map2);
                }

            }

        }

        return map;
    }

    private Map<String, String> getFiles(String path, File[] listFiles) {
        Map<String, String> map = new HashMap<String, String>();

        for (File file : listFiles) {
            if (file.isFile()){
                map.put(file.toString(), file.toString().replaceFirst(path+File.pathSeparator, ""));
                continue;
            }

            if (file.isDirectory()){
                Map<String, String> map2 = getFiles(path, file.listFiles());

                if (map2.size()>0){
                    map.putAll(map2);
                }
            }

        }

        return map;
    }

}