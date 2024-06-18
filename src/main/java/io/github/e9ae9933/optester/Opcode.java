package io.github.e9ae9933.optester;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.github.e9ae9933.optester.Opcode.VarType.*;
public enum Opcode
{
	NOP(0x00),
	MOV(0x01,REGISTER,ADDRESS),
	LDC(0x02,REGISTER,INT),
	MOVR(0x03,POINTER,REGISTER),
	LDSTK(0x04,REGISTER,OFFSET),
	STSTK(0x05,OFFSET,REGISTER),
	LDREG(0x06,REGISTER,REGISTER),
	STREG(0x07,REGISTER,REGISTER),
	MOVREG(0x08,REGISTER,REGISTER),
	PUSH(0x09,REGISTER),
	POP(0x0a,REGISTER),
	POPUNARY(0x0b),
	LDEA(0x0c,REGISTER,REGISTER,OFFSET),
	STEA(0x0d,REGISTER,OFFSET,REGISTER),
	ENTER(0x10),
	CALL(0x11,LABEL),
	LEAVE(0x12),
	RET(0x13,REGISTER),
	JMP(0x14,LABEL),
	JNZ(0x15,LABEL,REGISTER),
	JZ(0x16,LABEL,REGISTER),
	NOT(0x20,REGISTER,REGISTER),
	AND(0x40,REGISTER,REGISTER,REGISTER),
	OR(0x41,REGISTER,REGISTER,REGISTER),
	XOR(0x44,REGISTER,REGISTER,REGISTER),
	ADD(0x46,REGISTER,REGISTER,REGISTER),
	SUB(0x47,REGISTER,REGISTER,REGISTER),
	MUL(0x48,REGISTER,REGISTER,REGISTER),
	DIV(0x49,REGISTER,REGISTER,REGISTER),
	MOD(0x4a,REGISTER,REGISTER,REGISTER),
	SHL(0x4b,REGISTER,REGISTER,REGISTER),
	SHR(0x4c,REGISTER,REGISTER,REGISTER),
	EQU(0x80,REGISTER,REGISTER,REGISTER),
	NEQ(0x81,REGISTER,REGISTER,REGISTER),
	LSS(0x82,REGISTER,REGISTER,REGISTER),
	GEQ(0x83,REGISTER,REGISTER,REGISTER),
	GTR(0x84,REGISTER,REGISTER,REGISTER),
	LEQ(0x85,REGISTER,REGISTER,REGISTER),
	INPUT(0xa0,REGISTER),
	OUTPUT(0xa1,REGISTER),
	HALT(0xff);
	;
	public final byte op;
	public final VarType[] vars;
	Opcode(int op,VarType... vars)
	{
		this.op=(byte)(op&0xff);
		this.vars=vars;
	}
	public static Opcode getOp(int op)
	{
		return Arrays.stream(Opcode.values()).filter(o->(o.op&0xff)==op).findAny().orElse(HALT);
	}
	public static enum VarType
	{
		INT,
		POINTER,
		ADDRESS,
		OFFSET,
		REGISTER,
		LABEL,
	}
	public static enum RegisterType
	{
		EAX(1),
		EBX(2),
		ECX(3),
		EDX(4),
		ESP(5),
		EBP(6),
		ESI(7),
		EFLAGS(-128),
		;
		public final byte op;
		RegisterType(int op)
		{
			this.op=(byte)(op&0xff);
		}
		public static byte getRegister(String name)
		{
			RegisterType type=valueOf(name.toUpperCase());
			return type.op;
		}
	}
}
