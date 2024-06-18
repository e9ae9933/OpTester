package io.github.e9ae9933.optester;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Consumer;

import static com.mojang.brigadier.arguments.IntegerArgumentType.*;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.*;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.*;

public class Assembler
{
	private final CommandDispatcher<Object> dispatcher = new CommandDispatcher<>();
	private final List<Byte> out = new ArrayList<>();
	private final List<Pair<String, Integer>> pendingLabels = new ArrayList<>();
	private final Map<String, Integer> knownLabels = new HashMap<>();

	public Assembler()
	{
		for (Opcode opcode : Opcode.values())
		{
			String name = opcode.name().toLowerCase();
			LiteralArgumentBuilder<Object> original = literal(name);
			List<RequiredArgumentBuilder<Object, ?>> arguments = new ArrayList<>();
			List<Consumer<CommandContext<Object>>> decomposers = new ArrayList<>();
			int i = 0;
			for (Opcode.VarType var : opcode.vars)
			{
				String type = var.name() + (i++);
				switch (var)
				{
					case INT, POINTER, ADDRESS, OFFSET ->
					{
						RequiredArgumentBuilder<Object, Integer> next = argument(type, integer());
						arguments.add(next);
						decomposers.add(c -> appendInt(getInteger(c, type)));
					}
					case REGISTER ->
					{
						RequiredArgumentBuilder<Object, String> next = argument(type, string());
						arguments.add(next);
						decomposers.add(c -> appendByte(Opcode.RegisterType.getRegister(getString(c, type))));
					}
					case LABEL ->
					{
						RequiredArgumentBuilder<Object, String> next = argument(type, string());
						arguments.add(next);
						decomposers.add(c ->
						{
							pendingLabels.add(new ImmutablePair<>(getString(c, type), out.size()));
							appendInt(0xff114514);
						});
					}
				}
			}
			System.out.println(name + " has " + i + " arguments");
			if (!arguments.isEmpty())
			{
				RequiredArgumentBuilder<Object,?> builder=arguments.get(arguments.size()-1).executes(c->{

					appendByte(opcode.op);
					for(Consumer<CommandContext<Object>> o:decomposers)
						o.accept(c);
					return 1;
				});
				for(int j=arguments.size()-2;j>=0;j--)
				{
					builder=arguments.get(j).then(builder);
				}
				dispatcher.register(original.then(builder));
			} else
				dispatcher.register(original.executes(c ->
				{
					System.out.println("appending1");
					appendByte(opcode.op);
					return 1;
				}));
		}
		dispatcher.register(literal("#").then(argument("label", string()).executes(c ->
				{
					String s = getString(c, "label");
					System.out.println("known label " + s + " as " + out.size());
					knownLabels.put(s, out.size());
					return 1;
				}
		)));
		System.out.println(Arrays.toString(dispatcher.getAllUsage(dispatcher.getRoot(), new Object(), false)));
	}

	public void parse(String original)
	{
		try
		{
			String s=original;
			if(original.contains("//"))
				s=original.substring(0,original.indexOf("//"));
			s=s.trim();
			if(s.isEmpty()) return;
			ParseResults<Object> parse = dispatcher.parse(s, new Object());
			dispatcher.execute(parse);
		} catch (Exception e)
		{
			throw new RuntimeException("Failed to parse "+original,e);
		}
	}

	public byte[] output()
	{
		Byte[] bb = out.toArray(new Byte[0]);
		int n = bb.length;
		byte[] d = new byte[n];
		for (int i = 0; i < n; i++) d[i] = bb[i];
		for (Pair<String, Integer> p : pendingLabels)
		{
			String label = p.getLeft();
			int i = p.getRight();
			if (!knownLabels.containsKey(label)) throw new IllegalArgumentException("Label " + label + " unknown");
			int data = knownLabels.get(label);
			d[i + 3] = (byte) (data >>> 24 & 0xff);
			d[i + 2] = (byte) (data >>> 16 & 0xff);
			d[i + 1] = (byte) (data >>> 8 & 0xff);
			d[i] = (byte) (data & 0xff);
		}
		return d;
	}

	private Assembler appendByte(int b)
	{
		out.add((byte) (b & 0xff));
		return this;
	}

	private Assembler appendInt(int i)
	{
		appendByte((byte) (i & 0xff));
		appendByte((byte) (i >>> 8 & 0xff));
		appendByte((byte) (i >>> 16 & 0xff));
		appendByte((byte) (i >>> 24 & 0xff));
		return this;
	}
}
