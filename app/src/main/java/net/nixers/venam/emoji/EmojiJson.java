package net.nixers.venam.emoji;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class EmojiJson {
	
	@SerializedName("emojis")
	public List<String> emotions;
	
	@SerializedName("id")
	public String name;
}
