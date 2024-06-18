import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Test08V1Killer
{
	public static void main(String[] args) throws IOException
	{
//		Arrays.stream(new File("H:\\workspace\\redirect")
//				.listFiles(f -> f.getName().startsWith("token")))
//				.parallel().forEach(f->
//				{
//					System.out.println(f.getName());
//					f.delete();
//				});
		for(int i=64;i<80;i++)
		{
			File file=new File(("H:\\workspace\\redirect\\calc_optimized_opcode_%d.cmd").formatted(i));
			FileOutputStream fos=new FileOutputStream(file);
			fos.write("""
                        // OPTIMIZED CODE
                        // YOU SHOULDN'T MODIFY THIS.
                        // opcode: %02X
                        
                        
					""".formatted(i).getBytes(StandardCharsets.UTF_8));
			fos.close();
		}
	}
}
