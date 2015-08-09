import io.github.nixtabyte.telegram.jtelebot.client.RequestHandler;
import io.github.nixtabyte.telegram.jtelebot.response.json.Message;
import io.github.nixtabyte.telegram.jtelebot.server.Command;
import io.github.nixtabyte.telegram.jtelebot.server.CommandFactory;

public class BotCommandFactory implements CommandFactory {

	
	
	@Override
	public Command createCommand(Message message, RequestHandler requestHandler) {
		if (message.getText() == null||message.getText().isEmpty()){
			return null;
		}

		
		if (commandedToFetchImages(message)){
			return new GetImagesCommand(message,requestHandler);
		}
		
		
		return null;
	}
	
	private boolean commandedToFetchImages(final Message message){
		return message.getText().startsWith(Constants.COMMAND_FETCH_IMAGES);
	}

}
