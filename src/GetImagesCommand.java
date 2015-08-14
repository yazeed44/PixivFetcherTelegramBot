
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.List;

import com.scienjus.bean.Illust;
import com.scienjus.bean.IllustImage;
import com.scienjus.bean.IllustListItem;
import com.scienjus.callback.DownloadCallback;
import com.scienjus.client.PixivParserClient;
import com.scienjus.filter.IllustFilter;
import com.scienjus.thread.IllustImageDownloadTask;

import io.github.nixtabyte.telegram.jtelebot.client.RequestHandler;
import io.github.nixtabyte.telegram.jtelebot.client.impl.DefaultRequestHandler;
import io.github.nixtabyte.telegram.jtelebot.exception.JsonParsingException;
import io.github.nixtabyte.telegram.jtelebot.exception.TelegramServerException;
import io.github.nixtabyte.telegram.jtelebot.request.TelegramRequest;
import io.github.nixtabyte.telegram.jtelebot.request.factory.TelegramRequestFactory;
import io.github.nixtabyte.telegram.jtelebot.response.json.Message;
import io.github.nixtabyte.telegram.jtelebot.response.json.TelegramResponse;
import io.github.nixtabyte.telegram.jtelebot.server.impl.AbstractCommand;
public class GetImagesCommand extends AbstractCommand {

	public static final String DOWNLOAD_DIR = "images/";
	private final PixivParserClient mClient;
	private int mSendedImageCount = 1;
	

	public GetImagesCommand(Message message, RequestHandler requestHandler) {
		super(message, requestHandler);
		mClient = PixivBotMain.getPixivInstance();
		
	}

	@Override
	public void execute() {
		final GetImagesRequest request = new GetImagesRequest(message.getText());

			
		System.out.println(request);
		
		new Thread(new Runnable(){

			@Override
			public void run() {
			
				
				if (mClient.login()){
					
					final List<IllustListItem> matchedIllust = mClient.search(request.getKeywords(),new IllustFilter(){

						@Override
						public boolean doFilter(IllustListItem item) {
							
							return item.getFavoriteCount() >= 100;
						}},request.getLimit());
					System.out.println("The count of illust  "+matchedIllust.size());
					
					for(final IllustListItem item : matchedIllust){
						
						final Illust illust = mClient.getIllust(item.getId());
						
						
						
						for(final IllustImage image : illust.getImages()){
							new Thread(createImageDownloadTask(image)).start();
						}
					}
				}
				
				
				
				
				mClient.close();
				
				
			}}){
			
		}.start();
		
	}
	
	private IllustImageDownloadTask createImageDownloadTask(final IllustImage image){
		
		return new IllustImageDownloadTask(image,new DownloadCallback(){

			@Override
			public void onFinished(IllustImage illust, byte[] file) {
				try {
					final String dir = DOWNLOAD_DIR + "/" + illust.getIllustId() + "." + illust.getExtension();
					writeBytesToFile(file,dir);
				
					
					
					
					final TelegramRequest sendImageRequest = TelegramRequestFactory.createSendPhotoRequest(message.getChat().getId(), new File(dir),null, null, null);
					final TelegramResponse sendResponse = new DefaultRequestHandler(PrivateInfo.BOT_TOKEN).sendRequest(sendImageRequest);
				
					System.out.println("Is response successfull ?  " + sendResponse.isSuccessful());
					
					System.out.println("Sended images count  " + mSendedImageCount);
					mSendedImageCount++;
					
				} 
				
				catch (JsonParsingException e) {
					
					System.out.println("Failed to send the image  " + e.getMessage());
					
				} catch (TelegramServerException e) {
					
					System.out.println("Failed to send the image  " + e.getMessage());
				}
				
			}});
	}
	
	private void writeBytesToFile(final byte[] bytes,final String dir){
		
		
		try(final FileOutputStream fos = new FileOutputStream(dir)) {
			new File(dir).createNewFile();
			fos.write(bytes);
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
	}

}
