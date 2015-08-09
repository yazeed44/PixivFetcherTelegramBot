import java.io.IOException;

import bean.RankingMode;
import client.PixivClient;



public class PixivBotMain {
	
	public static final PixivClient CLIENT = PixivClient.create("images");

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CLIENT.setUsername(PrivateInfo.PIXIV_USERNAME);
		CLIENT.setPassword(PrivateInfo.PIXIV_PASSWORD);
		
		if (CLIENT.login()){
			CLIENT.searchByKeyword("Kancolle", false,100,2);
			//client.close();
			//client.downloadAllRank(RankingMode.all, false);
		}

		

		
		
 
	}

}
