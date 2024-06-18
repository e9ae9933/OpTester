import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class Test6Gen
{
	public static void main(String[] args)
	{
		Clipboard clipboard= Toolkit.getDefaultToolkit().getSystemClipboard();
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<256;i++)
		{
			sb.append("SELECTARRAY &&hex_%02X 0x%02x".formatted(i,i)).append('\n');
		}
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
				new StringSelection(sb.toString()),
				null
		);
	}
}
