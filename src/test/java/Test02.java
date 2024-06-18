import io.github.e9ae9933.optester.Assembler;
import io.github.e9ae9933.optester.Executor;

import java.util.Arrays;
import java.util.Scanner;

public class Test02
{
	public static void main(String[] args)
	{
		Assembler assembler=new Assembler();
		Scanner cin=new Scanner(ClassLoader.getSystemResourceAsStream("Test02.asm"));
		while(cin.hasNextLine())
		{
			String s=cin.nextLine().trim();
			if(s.isEmpty()) continue;
			System.out.println("parsing "+s);
			assembler.parse(s);
		}
		byte[] b=assembler.output();
		System.out.println(Arrays.toString(b));
		Executor executor=new Executor(512);
		for(int i=0;i<b.length;i++)
			executor.getMemory().writeByte(i,b[i]);
		executor.executeUntilHalt();
		System.out.println(executor.getMemory().toString());
	}
}
