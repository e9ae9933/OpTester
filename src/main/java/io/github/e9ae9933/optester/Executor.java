package io.github.e9ae9933.optester;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.ToIntFunction;

public class Executor
{
	@Deprecated
	public Executor(int memSize)
	{
		this(memSize,null,null);
	}
	public Executor(int memSize, IntSupplier stdin, IntConsumer stdout)
	{
		eax=new AtomicInteger();
		ebx=new AtomicInteger();
		ecx=new AtomicInteger();
		edx=new AtomicInteger();
		esp=new AtomicInteger();
		ebp=new AtomicInteger();
		eip=new AtomicInteger();
		eflags=new AtomicInteger();
		memory=new Memory(memSize);
		this.memSize=memSize;
		this.stdin=stdin;
		this.stdout=stdout;
	}
	private int memSize;
	private IntSupplier stdin;
	private IntConsumer stdout;
	private AtomicInteger eax,ebx,ecx,edx;
	private AtomicInteger esp,ebp;
	private AtomicInteger eip;
	private AtomicInteger eflags;
	private Memory memory;
	public void executeOnce()
	{
		byte opcode=getByte();
//		System.out.println("opcode "+opcode);
		switch (opcode&0xff)
		{
			case 0x00 -> {}
			case 0x01 -> mov();
			case 0x02 -> ldc();
			case 0x03 -> movr();
			case 0x04->ldstk();
			case 0x05->ststk();
			case 0x06->ldreg();
			case 0x07->streg();
			case 0x08->movreg();
			case 0x09->push();
			case 0x0a->pop();
			case 0x0b->popUnary();
			case 0x0c->ldea();
			case 0x0d->stea();
			case 0x10->enter();
			case 0x11->call();
			case 0x12->leave();
			case 0x13->ret();
			case 0x14->jmp();
			case 0x15->jnz();
			case 0x16->jz();
			case 0x20->intFunction(a->~a);
			case 0x40->biFunction((a,b)->a&b);
			case 0x41->biFunction((a,b)->a|b);
			//case 0x42->biFunction((a,b)->a&b);
			//case 0x43->biFunction((a,b)->a&b);
			case 0x44->biFunction((a,b)->a^b);
			//case 0x45->biFunction((a,b)->a&b);
			case 0x46->biFunction((a,b)->a+b);
			case 0x47->biFunction((a,b)->a-b);
			case 0x48->biFunction((a,b)->a*b);
			case 0x49->biFunction((a,b)->{if(b==0){setFlag(TF,true);return 0;}return a/b;});
			case 0x4a->biFunction((a,b)->{if(b==0){setFlag(TF,true);return 0;}return a%b;});
			case 0x4b->biFunction((a,b)->a<<b);
			case 0x4c->biFunction((a,b)->a>>>b);
			//case 0x4d->biFunction((a,b)->a>>b);
			case 0x80->biFunction((a,b)->a==b?1:0);
			case 0x81->biFunction((a,b)->a!=b?1:0);
			case 0x82->biFunction((a,b)->a<b?1:0);
			case 0x83->biFunction((a,b)->a>=b?1:0);
			case 0x84->biFunction((a,b)->a>b?1:0);
			case 0x85->biFunction((a,b)->a<=b?1:0);
			case 0xa0->input();
			case 0xa1->output();
			case 0xff->halt();
			default->halt();
		}
	}
	public void executeUntilHalt()
	{
		while(!getFlag(TF))
		{
			executeOnce();
		}
	}
	private void mov()
	{
		AtomicInteger reg=getRegister();
		int ptr=getInt();
		reg.set(memory.readInt(ptr));
	}
	private void ldc()
	{
		AtomicInteger reg=getRegister();
		int val=getInt();
		reg.set(val);
	}
	private void movr()
	{
		int val=getInt();
		AtomicInteger reg=getRegister();
		memory.writeInt(val,reg.get());
	}
	private void ldstk()
	{
		AtomicInteger reg=getRegister();
		int offset=getInt();
		reg.set(memory.readInt(ebp.get()+offset));
	}
	private void ststk()
	{
		int offset=getInt();
		AtomicInteger reg=getRegister();
		memory.writeInt(ebp.get()+offset,reg.get());
	}
	private void ldreg()
	{
		AtomicInteger reg1=getRegister();
		AtomicInteger reg2=getRegister();
		reg1.set(memory.readInt(reg2.get()));
	}
	private void streg()
	{
		AtomicInteger reg1=getRegister();
		AtomicInteger reg2=getRegister();
		memory.writeInt(reg1.get(),reg2.get());
	}
	private void movreg()
	{
		AtomicInteger reg1=getRegister();
		AtomicInteger reg2=getRegister();
		reg1.set(reg2.get());
	}
	private void push()
	{
		AtomicInteger reg=getRegister();
		memory.writeInt(esp.getAndAdd(4),reg.get());
	}
	private void pop()
	{
		AtomicInteger reg=getRegister();
		reg.set(memory.readInt(esp.addAndGet(-4)));
	}
	private void popUnary()
	{
		esp.addAndGet(-4);
	}
	private void ldea()
	{
		AtomicInteger dest=getRegister();
		AtomicInteger base=getRegister();
		int offset=getInt();
		dest.set(memory.readInt(base.get()+offset));
	}
	private void stea()
	{
		AtomicInteger base=getRegister();
		int offset=getInt();
		AtomicInteger src=getRegister();
		memory.writeInt(base.get()+offset,src.get());
	}
	private void enter()
	{
		memory.writeInt(esp.getAndAdd(4),-1);
		memory.writeInt(esp.getAndAdd(4),ebp.get());
		ebp.set(esp.get());
	}
	private void call()
	{
		int ptr=getInt();
		memory.writeInt(ebp.get()-8,eip.get());
		eip.set(ptr);
	}
	private void leave()
	{
		esp.set(ebp.get());
		ebp.set(memory.readInt(esp.addAndGet(-4)));
		eip.set(memory.readInt(esp.addAndGet(-4)));
	}
	private void ret()
	{
		AtomicInteger reg=getRegister();
		int val=reg.get();
		esp.set(ebp.get());
		ebp.set(memory.readInt(esp.addAndGet(-4)));
		int ptr=memory.readInt(esp.addAndGet(-4));
		memory.writeInt(esp.getAndAdd(4),val);
		eip.set(ptr);
	}
	private void jmp()
	{
		int ptr=getInt();
		eip.set(ptr);
	}
	private void jnz()
	{
		int ptr=getInt();
		AtomicInteger reg=getRegister();
		if(reg.get()!=0) eip.set(ptr);
	}
	private void jz()
	{
		int ptr=getInt();
		AtomicInteger reg=getRegister();
		if(reg.get()==0) eip.set(ptr);
	}
	private void intFunction(ToIntFunction<Integer> op)
	{
		AtomicInteger dest=getRegister();
		AtomicInteger arg=getRegister();
		dest.set(op.applyAsInt(arg.get()));
	}
	private void biFunction(IntBinaryOperator op)
	{
		AtomicInteger dest=getRegister();
		AtomicInteger arg1=getRegister();
		AtomicInteger arg2=getRegister();
		dest.set(op.applyAsInt(arg1.get(),arg2.get()));
	}
	private void input()
	{
		AtomicInteger dest=getRegister();
		int val=stdin.getAsInt();
		//cin.close();
		dest.set(val);
	}
	private void output()
	{
		AtomicInteger reg=getRegister();
		int val=reg.get();
		stdout.accept(val);
	}
	private void halt()
	{
		setFlag(TF,true);
	}
	private boolean getFlag(int flag)
	{
		return (eflags.get()&flag)!=0;
	}
	private void setFlag(int flag,boolean enabled)
	{
		if(enabled)
			eflags.set(eflags.get()|flag);
		else
			eflags.set(eflags.get()&(~flag));
	}
	private AtomicInteger getRegister()
	{
		byte reg=getByte();
		return switch (reg)
		{
			case 1->eax;
			case 2->ebx;
			case 3->ecx;
			case 4->edx;
			case 5->esp;
			case 6->ebp;
			case 7->eip;
			case -128->eflags;
			default -> error("illegal register: "+reg);
		};
	}
	private <T> T error(String arg)
	{
		throw new RuntimeException(arg);
	}
	private byte getByte()
	{
		return memory.readByte(eip.getAndIncrement());
	}
	private short getShort()
	{
		return memory.readShort(eip.getAndAdd(2));
	}
	private int getInt()
	{
		return memory.readInt(eip.getAndAdd(4));
	}
	public Memory getMemory()
	{
		return memory;
	}

	public AtomicInteger getEax()
	{
		return eax;
	}

	public AtomicInteger getEbx()
	{
		return ebx;
	}

	public AtomicInteger getEcx()
	{
		return ecx;
	}

	public AtomicInteger getEdx()
	{
		return edx;
	}

	public AtomicInteger getEsp()
	{
		return esp;
	}

	public AtomicInteger getEbp()
	{
		return ebp;
	}

	public AtomicInteger getEip()
	{
		return eip;
	}

	public AtomicInteger getEflags()
	{
		return eflags;
	}

	public final static int TF=1<<8;

	@Override
	public String toString()
	{
		StringBuilder sb=new StringBuilder("Executor dump\n");
		sb.append("EAX %d\t".formatted(eax.get()));
		sb.append("EBX %d\t".formatted(ebx.get()));
		sb.append("ECX %d\t".formatted(ecx.get()));
		sb.append("EDX %d\t".formatted(edx.get()));
		sb.append("ESP %d\t".formatted(esp.get()));
		sb.append("EBP %d\t".formatted(ebp.get()));
		sb.append("EIP %d\t".formatted(eip.get()));
		sb.append("EFLAGS %d".formatted(eflags.get()));
		sb.append('\n');
		sb.append(memory.toString());
		return sb.toString();
	}
}
