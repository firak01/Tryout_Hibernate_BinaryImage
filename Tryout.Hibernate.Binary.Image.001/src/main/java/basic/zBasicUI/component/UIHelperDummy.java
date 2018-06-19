package basic.zBasicUI.component;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileCacheImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

public class UIHelperDummy {

	//Diese Methoden in den Kernel übernehmen. Sie dienen hier nur zur Verdeutlichung wie mit ImagInputStream und den BLOBs gearbeitet werden kann
	public File addImageExtension(File incoming) throws IOException {
		
		String format = null;
		ImageInputStream iis = ImageIO.createImageInputStream(incoming);
		Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
		while (imageReaders.hasNext()) {
		    ImageReader reader = (ImageReader) imageReaders.next();
		    format = reader.getFormatName().toLowerCase();
		   // log.debug("filetype is: " + format);

		    File newfile = new File("imageimage." + format);
			if (newfile.exists()) {
				newfile.delete();    	
			}
			
		    Files.copy(incoming.toPath(), newfile.toPath());
		    incoming.delete();

		    return newfile;
		}
		return null;
		
	}
	
	/**
	 * Extracts the picture size of a given Image.
	 *
	 * @param path Path to the image
	 * @return Image-size in pixel
	 * @throws IOException Throws exception when file-access fails
	 */
	public static Dimension getPictureSize(final String path) throws IOException {
	    try (ImageInputStream in = ImageIO.createImageInputStream(path)) {
	        final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
	        if (readers.hasNext()) {
	            ImageReader reader = readers.next();
	            try {
	                reader.setInput(in);
	                return new Dimension(reader.getWidth(0), reader.getHeight(0));
	            } finally {
	                reader.dispose();
	            }
	        }
	        return null;
	    }
	}
	
	
	public ImageInputStream createInputStreamInstance(Object input, boolean useCache, File cacheDir) throws IOException {
		if (input instanceof InputStream) {
			InputStream is = (InputStream)input;

			if (useCache) {
				return new FileCacheImageInputStream(is, cacheDir);
			} else {
				return new MemoryCacheImageInputStream(is);
			}
		} else {
			throw new IllegalArgumentException();
		}
	}
	
}
