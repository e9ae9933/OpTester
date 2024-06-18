import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Test07MemoryV2
{
	static String v2(int i)
	{
		return """
    IFSTR $op IS 'read' {
        val=$mem_{i}
        op!
    }
    IFSTR $op IS 'write' {
        mem_{i}=$val
        val!
        op!
    }
				""".replace("{i}", Integer.toString(i,10));
	}
	public static void main(String[] args) throws Exception
	{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ZipOutputStream zos=new ZipOutputStream(baos);
		IntStream.range(0,1<<16)
//				.parallel()
				.forEach(i->{
					try
					{
						String s = v2(i);
						String name="memory_v2_%d.cmd".formatted(i);
						zos.putNextEntry(new ZipEntry(name));
						zos.write(s.getBytes(StandardCharsets.UTF_8));
						zos.closeEntry();
//						FileOutputStream fos = new FileOutputStream("H:\\workspace\\redirect\\memory_v2_%d.cmd".formatted(i));
//						fos.write(s.getBytes(StandardCharsets.UTF_8));
//						fos.close();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				});
		zos.close();
		new FileOutputStream("64KiB.zip").write(baos.toByteArray());
	}
}
