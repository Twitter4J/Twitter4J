package twitter4j;

import java.io.*;

/**
 * A {@link java.io.FileInputStream} that limits input to a given number of bytes
 *
 * Created by jrbuckeridge on 7/5/16.
 */
public class LimitedFileInputStream extends FileInputStream {

    /**
     * The initial skip to read from file
     */
    protected final long skip;

    /**
     * Currently skipped bytes
     */
    private long currentSkipped = 0;

    /**
     * Maximum number of bytes to read from file
     */
    protected final long max;

    /**
     * Current count of bytes read
     */
    protected int count = 0;

    public LimitedFileInputStream(String name, long skip, long max) throws FileNotFoundException {
        super(name);
        this.skip = skip;
        this.max = max;
    }

    public LimitedFileInputStream(File file, long skip, long max) throws FileNotFoundException {
        super(file);
        this.skip = skip;
        this.max = max;
    }

    public LimitedFileInputStream(FileDescriptor fdObj, long skip, long max) {
        super(fdObj);
        this.skip = skip;
        this.max = max;
    }

    public long getSkip() {
        return skip;
    }

    public long getMax() {
        return max;
    }

    /**
     * Skips the initial bytes if necessary
     */
    private void doInitialSkip() throws IOException {
        while (currentSkipped < skip) {
            currentSkipped += skip(skip - currentSkipped);
        }
    }

    @Override
    public int read() throws IOException {
        doInitialSkip();
        if (count >= max) {
            return -1;
        }

        int read = super.read();
        count++;
        return read;
    }

    @Override
    public int read(byte[] b) throws IOException {
        doInitialSkip();
        if (count >= max) {
            return -1;
        }

        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        doInitialSkip();
        if (count >= max) {
            return -1;
        }

        long pending = max - count;
        int read = super.read(b, off, b.length < pending ? b.length : (int) pending);
        count += read;
        return read;
    }

    @Override
    public int available() throws IOException {
        int available = super.available();
        return available > max - count ? (int) (max - count) : available;
    }
}
