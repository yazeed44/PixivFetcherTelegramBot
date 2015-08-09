import java.io.IOException;

import bean.RankingMode;
import client.PixivClient;
import io.github.nixtabyte.telegram.jtelebot.server.impl.DefaultCommandDispatcher;
import io.github.nixtabyte.telegram.jtelebot.server.impl.DefaultCommandQueue;
import io.github.nixtabyte.telegram.jtelebot.server.impl.DefaultCommandWatcher;



public class PixivBotMain {
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		final DefaultCommandDispatcher commandDispatcher = new DefaultCommandDispatcher(10,100, new DefaultCommandQueue());
        commandDispatcher.startUp();
         
        final DefaultCommandWatcher commandWatcher = new DefaultCommandWatcher(2000,100,PrivateInfo.BOT_TOKEN,commandDispatcher,new BotCommandFactory());
        commandWatcher.startUp();


		

		
		
 
	}
	
	public static PixivClient getPixivInstance(){
		final PixivClient client = PixivClient.create("images");
		client.setUsername(PrivateInfo.PIXIV_USERNAME);
		client.setPassword(PrivateInfo.PIXIV_PASSWORD);
		return client;
	}

}
