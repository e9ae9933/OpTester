package io.github.e9ae9933.optester;

public class Memory {
	byte[] data;
	Memory(int limit)
	{
		data=new byte[limit];
	}
	public byte readByte(int ptr)
	{
		return data[ptr];
	}
	public void writeByte(int ptr,byte val)
	{
		data[ptr]=val;
	}
	public short readShort(int ptr)
	{
		return ((short) ((data[ptr + 1]&0xff) << 8 | (data[ptr]&0xff)));
	}
	public void writeShort(int ptr,short val)
	{
		data[ptr+1]=(byte)(val>>>8&0xff);
		data[ptr]=(byte)(val&0xff);
	}
	public int readInt(int ptr)
	{
		return (data[ptr+3]&0xff)<<24|(data[ptr+2]&0xff)<<16|(data[ptr+1]&0xff)<<8|(data[ptr]&0xff);
	}
	public void writeInt(int ptr, int val)
	{
		data[ptr+3]=(byte)(val>>>24&0xff);
		data[ptr+2]=(byte)(val>>>16&0xff);
		data[ptr+1]=(byte)(val>>>8&0xff);
		data[ptr]=(byte)(val&0xff);
	}

	@Override
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<(data.length+15)/16;i++)
		{
			sb.append("%04X    ".formatted(i*16));
			for(int j=0;j<16;j++)
			{
				int k=i*16+j;
				if(k>=data.length) continue;
				sb.append("%02X ".formatted(data[k]));
				if(j==7) sb.append(' ');
			}
			sb.append('\n');
		}
		return sb.toString();
	}
}
