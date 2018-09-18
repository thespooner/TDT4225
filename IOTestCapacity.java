package Distsystemer2;
import java.io.FileOutputStream;
import java.util.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.*;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class IOTestCapacity {

    // 1 GB = 1024*1024*1024 Bytes
    private static final int BLOCKSIZE = 8192; //8KB
    private static final long NBLOCKS = 131072; // #Blocks per 1GB
    private Scanner sc;
    private ByteBuffer buffer = ByteBuffer.allocate(BLOCKSIZE);
    private String filename = "C:/Users/CarlOtto/IdeaProjects/project/src/DistSystemer/output";

    public static void main(String[] args) throws FileNotFoundException, IOException{
        IOTestCapacity IOT = new IOTestCapacity();
        IOT.getMoneyForNothingToBuffer();
        System.out.println(IOT.writeBufferToFile(1));
        System.out.println(IOT.readBufferFromFile(1));
    }
    public void getMoneyForNothingToBuffer() throws FileNotFoundException{
        sc = new Scanner(new File("C:/Users/CarlOtto/IdeaProjects/project/src/DistSystemer/MoneyForNothing.txt"));
        while (sc.hasNext() && buffer.hasRemaining()){
            String inputString = sc.next();
            for (byte b :inputString.getBytes()
                 ) {
                try {
                    buffer.put(b);
                } catch (Exception e){
                    break; // Buffer is full
                }
            }
        }
        sc.close();

    }

    public boolean bufferFull(){
        return !buffer.hasRemaining();
    }

    public void writeBufferToFile() throws IOException{ // a beta function for testing
        FileOutputStream fos = new FileOutputStream(filename);
        buffer.rewind();
        FileChannel fileChannel = fos.getChannel();
        fileChannel.write(buffer);
        fileChannel.close();
        fos.close();
    }

    public String writeBufferToFile(int GB) throws IOException{
        //FileOutputStream fos = new FileOutputStream(filename);
        //FileChannel fileChannel = fos.getChannel();
        Path path = Paths.get(filename);
        SeekableByteChannel channel = Files.newByteChannel(path,EnumSet.of(CREATE, APPEND));
        long start_time = System.nanoTime();
        for(int i = 0 ; i < GB*NBLOCKS ; i++ ){
            buffer.rewind();
            channel.write(buffer);
        }
        long end_time = System.nanoTime();
        channel.close();
        double difference = (end_time - start_time) / 1e6;
        return "Time elapsed "+Double.toString(difference);
    }
    public String readBufferFromFile(int GB) throws IOException{
        Path path = Paths.get(filename);
        FileChannel fileChannel = FileChannel.open(path);
        long start_time = System.nanoTime();
        for(int i = 0 ; i < GB*NBLOCKS ; i++ ){
            buffer.clear();
            fileChannel.read(buffer);
        }
        long end_time = System.nanoTime();
        fileChannel.close();
        double difference = (end_time - start_time) / 1e6;
        return "Time elapsed "+Double.toString(difference);
    }


}
