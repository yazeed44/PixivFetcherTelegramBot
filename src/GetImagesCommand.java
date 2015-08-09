import java.io.File;

import client.GetImagesRequest;
import client.PixivClient;
import io.github.nixtabyte.telegram.jtelebot.client.RequestHandler;
import io.github.nixtabyte.telegram.jtelebot.exception.JsonParsingException;
import io.github.nixtabyte.telegram.jtelebot.exception.TelegramServerException;
import io.github.nixtabyte.telegram.jtelebot.request.TelegramRequest;
import io.github.nixtabyte.telegram.jtelebot.request.factory.TelegramRequestFactory;
import io.github.nixtabyte.telegram.jtelebot.response.json.Message;
import io.github.nixtabyte.telegram.jtelebot.response.json.TelegramResponse;
import io.github.nixtabyte.telegram.jtelebot.server.impl.AbstractCommand;

public class GetImagesCommand extends AbstractCommand {

	public GetImagesCommand(Message message, RequestHandler requestHandler) {
		super(message, requestHandler);
		
	}

	@Override
	public void execute() {
		final GetImagesRequest request = new GetImagesRequest(message.getText()){

			@Override
			public void onImageLoaded(final File imageFile, final int position) {
				try {
					
					final TelegramRequest sendRequest = TelegramRequestFactory.createSendPhotoRequest(message.getChat().getId(), imageFile, null, null, null);
					final TelegramResponse response = requestHandler.sendRequest(sendRequest);
					System.out.println(response.toString());
					imageFile.delete();
				} catch (JsonParsingException e) {
					
					e.printStackTrace();
				} catch (TelegramServerException e) {
					
					e.printStackTrace();
				}
				
			}
			
		};
		System.out.println(request);
		
		new Thread(new Runnable(){

			@Override
			public void run() {
			
				final PixivClient client = PixivBotMain.getPixivInstance();
				client.login();
				
				client.searchByKeyword(request.getKeywords(), false, 100,request.getLimit(),request);
				
				
				
			}}){
			
		}.start();;
		
	}

}
