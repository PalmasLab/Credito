package palmaslab.mapas.repository;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import palmaslab.mapas.repository.Postosaude;





public class LocalFileManager {

	/**
	 * This static factory method creates and returns a 
	 * VideoFileManager object to the caller. Feel free to customize
	 * this method to take parameters, etc. if you want.
	 * 
	 * @return
	 * @throws IOException
	 */
	
	private static final int IMG_WIDTH = 100;
	private static final int IMG_HEIGHT = 100;
	
	
	public static LocalFileManager get() throws IOException {

		if(System.getProperty("os.name").startsWith("Windows"))
		{
			targetDir_ = Paths.get("C:/PalmasLab/Credito/");
		}
		else
		{
			targetDir_ = Paths.get("/srv/PalmasServer/locals/");
		}
		return new LocalFileManager();
	}
	public String getTargetDir()
	{
		
		return targetDir_.toString();
	}
	//private Path targetDir_ = Paths.get(System.getProperty("user.dir")+"/images");
	//"/srv/myapp/locals"
	private static Path targetDir_ = Paths.get(System.getProperty("user.dir")+"/images");
	
	// The VideoFileManager.get() method should be used
	// to obtain an instance
	private LocalFileManager() throws IOException{
		if(!Files.exists(targetDir_)){
			Files.createDirectories(targetDir_);
		}
	}
	
	// Private helper method for resolving video file paths
	private Path getLocalPath(Postosaude g, int PictureNumberByLocal){
		assert(g != null);
		//System.out.println("Working Path="+targetDir_);
		return targetDir_.resolve("picture"+"-"+g.getId()+"-"+PictureNumberByLocal+".jpg");
	}
	//Private helper method for resolving EXCEL FILES paths
	private Path getLocalPathExcel(){
		return targetDir_.resolve("mapa_credito_excel.xls");
	}
	private Path getLocalPathSmaller(Postosaude g, int PictureNumberByLocal){
		assert(g != null);
		//System.out.println("Working Path="+targetDir_);
		return targetDir_.resolve("pictureSmall"+"-"+g.getId()+"-"+PictureNumberByLocal+".jpg");
	}
	private Path getLocalPathBackground(String imageName){
	
		return targetDir_.resolve(imageName);
	}
	/**
	 * This method returns true if the specified Video has binary
	 * data stored on the file system.
	 * 
	 * @param v
	 * @return
	 */
	public boolean hasLocalData(Postosaude g,int i){
		Path source = getLocalPath(g, i);
		return Files.exists(source);
	}
	
	/**
	 * This method copies the binary data for the given video to
	 * the provided output stream. The caller is responsible for
	 * ensuring that the specified Video has binary data associated
	 * with it. If not, this method will throw a FileNotFoundException.
	 * 
	 * @param v 
	 * @param out
	 * @throws IOException 
	 */
	public void copyGiftData(Postosaude g,int i, OutputStream out) throws IOException {
		Path source = getLocalPath(g,i);
		if(!Files.exists(source)){
			throw new FileNotFoundException("Unable to find the referenced gift file for giftId:"+g.getId());
		}
		Files.copy(source, out);
	}
	public void copySmallGiftData(Postosaude g,int i, OutputStream out) throws IOException {
		Path source = getLocalPathSmaller(g,i);
		if(!Files.exists(source)){
			throw new FileNotFoundException("Unable to find the referenced gift file for giftId:"+g.getId());
		}
		Files.copy(source, out);
	}
	public void copyImageBackgroundData(String imageData, OutputStream out) throws IOException {
		Path source = getLocalPathBackground(imageData);
		if(!Files.exists(source)){
			throw new FileNotFoundException("Unable to find the referenced gift file ");
		}
		Files.copy(source, out);
	}
	/**
	 * This method reads all of the data in the provided InputStream and stores
	 * it on the file system. The data is associated with the Video object that
	 * is provided by the caller.
	 * 
	 * @param v
	 * @param videoData
	 * @throws IOException
	 */
	
	public void saveLocalData(Postosaude g,int i, InputStream localData) throws IOException{
		assert(localData != null);
		
		Path target = getLocalPath(g,i);
		Path targetSmaller = getLocalPathSmaller(g,i);
		Files.copy(localData, target, StandardCopyOption.REPLACE_EXISTING);
		resizeImage(target,targetSmaller);
		
	}
	public InputStream resizeImage(Path target,Path targetSmaller)
	{
		BufferedImage resizedImage = null;
		BufferedImage originalImage =null;
	
		try {
			
			//BUFFERING ORGINALIMAGE
		
			File file = new File(target.toString()); // I have bear.jpg in my working directory  
		    FileInputStream fis = new FileInputStream(file);  
			originalImage = ImageIO.read(fis);
			
			
			//GETTING THE IMAGE_TYPE
			int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			
			//CREATING THE RESIZED_IMAGE
			resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
			g.dispose();
			
			File outputfile = new File(targetSmaller.toString());
			ImageIO.write(resizedImage, "jpg", outputfile);



		
		return (InputStream) ImageIO.createImageInputStream(resizedImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	public void deleteImageFromLocal(Postosaude g, int i) throws IOException
	{
		Path target = getLocalPath(g,i);
		Files.delete(target);
	}
	public List<String> getImagesFromLocal(Postosaude p)
	{
		List<String> images = new ArrayList<String>();
		
		for( int a = 0 ;a < 20 ; a++)
		{
			if(hasLocalData(p,a))
			{
				images.add(p.getId()+"-"+a);
				
			}
		}
		
		return images;
	}
	
	
	//APACHE POI!!
	//SAVE NEW EXCEL FILE!!
	
	public void saveLocalData( InputStream localData) throws IOException{
		assert(localData != null);
		
		Path target = getLocalPathExcel();
		Files.copy(localData, target, StandardCopyOption.REPLACE_EXISTING);
		
	}
	
	
	

}


