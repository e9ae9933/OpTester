import io.github.e9ae9933.optester.Assembler;
import io.github.e9ae9933.optester.Executor;

import java.io.FileWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringJoiner;

public class Test05Aic
{
	public static void main(String[] args) throws Exception
	{
		byte[] b=calc("Tree2.asm");
		FileWriter fw=new FileWriter("H:\\workspace\\redirect\\init_memory.cmd");
		for(int i=0;i<b.length;i++)
		{
			int val=b[i]&0xff;
			fw.write("mem_%d=%d\n".formatted(i,val));
		}
		fw.flush();
		Scanner cin=new Scanner(System.in);
		Executor executor=new Executor(65536,
				()->{
			System.out.print("Require integer: ");
			return cin.nextInt();
				},
				i->{
			System.out.println("Outputed integer: "+i);
				});
		System.out.println("opcode length "+b.length);
		for(int i=0;i<b.length;i++)
			executor.getMemory().writeByte(i,b[i]);
		StringJoiner sj=new StringJoiner(", ","[","]");
		for(int i=0;i<b.length;i++)
		{
			int d=b[i]&0xff;
			sj.add("%d/%02X".formatted(d,d));
		}
		System.out.println(sj);
		executor.executeUntilHalt();
		System.out.println(executor);
	}
	static byte[] calc(String name)
	{
		Assembler assembler=new Assembler();
		Scanner cin=new Scanner(ClassLoader.getSystemResourceAsStream(name));
		while(cin.hasNextLine())
		{
			String s=cin.nextLine().trim();
			if(s.isEmpty()) continue;
			assembler.parse(s);
		}
		byte[] b=assembler.output();
		System.out.println(Arrays.toString(b));
		return b;
	}
}
