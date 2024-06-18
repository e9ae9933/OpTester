import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Test03Gen
{
	static void dfs(int l,int r)
	{
		if(l==r)
		{
			int i=l;
			StringBuilder sb=new StringBuilder();
			sb.append("IFSTR $op IS 'read' {\n");
			sb.append("\tval=$mem_%d\n".formatted(i));
			sb.append("}\n");
			sb.append("IFSTR $op IS 'write' {\n");
			sb.append("\tmem_%d=$val\n".formatted(i));
			sb.append("}\n");
			write(token(l,r),sb.toString());
			return;
		}
		StringBuilder sb=new StringBuilder();
		int mid=(l+r)/2;
		sb.append("IF $ptr<=%d {\n".formatted(mid));
		sb.append("\tCHANGE_EVENT2 %s\n".formatted(token(l,mid)));
		sb.append("} ELSE {\n");
		sb.append("\tCHANGE_EVENT2 %s\n".formatted(token(mid+1,r)));
		sb.append("}\n");
		write(token(l,r),sb.toString());
		dfs(l,mid);
		dfs(mid+1,r);
	}
	static void writeReal(String token,String s)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(new File("H:\\workspace\\redirect", token + ".cmd"));
			fos.write(s.getBytes(StandardCharsets.UTF_8));
			fos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	static Map<String,String> map=new HashMap<>();
	static void write(String token,String s)
	{
		map.put(token,s);
	}
	static String token(int l,int r)
	{
		return "token_%d_%d".formatted(l,r);
	}
	static void flush()
	{
		map.entrySet()
				.stream()
				.parallel()
				.forEach(e->{
					String key=e.getKey();
					String val=e.getValue();
					writeReal(key,val);
				});
	}
	public static void main(String[] args) throws Exception
	{
		StringBuilder sb=new StringBuilder();
		dfs(0,(1<<16)-1);
		flush();
	}
}
