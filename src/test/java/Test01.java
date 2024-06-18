import io.github.e9ae9933.optester.Executor;
import io.github.e9ae9933.optester.Memory;

import java.util.Objects;
import java.util.Scanner;

public class Test01
{
	public static void main(String[] args)
	{
		Executor executor=new Executor(256);
		Memory memory=executor.getMemory();
		Scanner cin=new Scanner(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("Test01.asm")));
		int ptr=0;
		while(cin.hasNextInt())
		{
			int i=cin.nextInt();
			memory.writeInt(ptr++,i);
		}
		System.out.println("all ptr "+ptr);
		try
		{
			executor.executeUntilHalt();
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.print("  EAX "+executor.getEax());
		System.out.print("  EBX "+executor.getEbx());
		System.out.print("  ECX "+executor.getEcx());
		System.out.print("  EDX "+executor.getEdx());
		System.out.print("  ESP "+executor.getEsp());
		System.out.print("  EBP "+executor.getEbp());
		System.out.print("  EIP "+executor.getEip());
		System.out.println();
	}
}
