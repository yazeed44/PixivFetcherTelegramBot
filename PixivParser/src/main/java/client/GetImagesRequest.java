package client;
import java.io.File;

public abstract class GetImagesRequest {

	
	private final String keywords;
	private final int limit;
	public GetImagesRequest(final String command ){
		keywords = parseKeywords(command);
		limit = parseLimit(command);
	}
	
	private int parseLimit(final String command) {
		return Integer.parseInt(command.split(" ")[2]);
		
	}
	private String parseKeywords(final String command) {
		return command.split(" ")[1];
	}
	
	
	public String getKeywords(){
		return keywords;
	}
	
	public int getLimit(){
		return limit;
	}
	@Override
	public String toString() {
		return "GetImagesRequest [keywords=" + keywords + ", limit=" + limit + "]";
	}
	
	public abstract void onImageLoaded(final File imageFile,final int position);
	
}
