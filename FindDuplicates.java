/*
 javac FindDuplicates.java
 java FindDuplicates C:\Users\Sharath\Downloads\test

*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FindDuplicates {
    private static MessageDigest mDigest;
    static {
        try {
            mDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("cannot initialize SHA-512 hash function", e);
        }
    }

    public static void find(Map<String, List<String>> dupFilesMap, File directory) throws Exception  {
        String hash;
        for (File subDir : directory.listFiles()) {
            if (subDir.isDirectory()) {
                find(dupFilesMap, subDir);
            } else {
                try {
                    hash = makeHash(subDir);
                    List<String> list = dupFilesMap.get(hash);
                    if (list == null) {
                        list = new LinkedList<String>();
                        dupFilesMap.put(hash, list);
                    }
                    list.add(subDir.getAbsolutePath());
                } catch (IOException e) {
                    throw new RuntimeException("cannot read file " + subDir.getAbsolutePath(), e);
                }
            }
        }
    }

    /*
     * return hash of the file
     */
    public static String makeHash(File infile) throws Exception {
        
        int bufferSize = 8192;
		FileInputStream fStream = new FileInputStream( infile );
		FileChannel fChannel = fStream.getChannel( );
		MappedByteBuffer memBuffer = fChannel.map( FileChannel.MapMode.READ_ONLY, 0L, fChannel.size( ) );
		byte[] barray = new byte[bufferSize];
		int numBytes;
		while( memBuffer.hasRemaining( ) ) {
			numBytes = Math.min( memBuffer.remaining( ), bufferSize );
			memBuffer.get( barray, 0, numBytes );
			mDigest.update(barray, 0, numBytes);
		}
		fChannel.close();
		String hash = new BigInteger(1, mDigest.digest()).toString(16);
        return hash;
    }


    public static void main(String[] args) {
		//long startTime = System.currentTimeMillis();
        if (args.length < 1) {
            System.out.println("Please provide directory to look for duplicate files.");
            return;
        }
        File dir = new File(args[0]);
        if (!dir.isDirectory()) {
            System.out.println("Directory missing.");
            return;
        }
        Map<String, List<String>> dupFilesMap = new HashMap<String, List<String>>();
        try {
            FindDuplicates.find(dupFilesMap, dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (List<String> list : dupFilesMap.values()) {
            if (list.size() > 1) {
                System.out.println("--");
                for (String file : list) {
                    System.out.println(file);
                }
            }
        }
		
        //System.out.println("--");
		//long endTime   = System.currentTimeMillis();
		//long totalTime = endTime - startTime;
		//System.out.println(totalTime);
    }
}