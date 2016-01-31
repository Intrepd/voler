package com.devineemail.util;

import java.io.ByteArrayOutputStream;

public class HexUtils
{
	static final byte[] HEX_CHAR_TABLE =
	{ (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B', (byte) 'C',
			(byte) 'D', (byte) 'E', (byte) 'F' };

	public static String toHexString(final byte[] raw)
	{
		return toHexString(raw, 0, raw.length);
	}

	public static String toHexString(final byte[] raw, final int offset, final int length)
	{
		final byte[] hex = new byte[2 * length];
		int index = 0;
		for (int i = 0; i < length; i++)
		{
			final int v = raw[i + offset] & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}
		return new String(hex);
	}

	public static String toHexString(final int value, final int minDigits)
	{
		String hexString = Integer.toHexString(value);
		for (int i = minDigits - hexString.length(); i > 0; i--)
		{
			hexString = "0" + hexString;
		}

		return hexString.toUpperCase();
	}

	public static byte[] fromHexString(String hexString)
	{
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		if (hexString != null)
		{
			if (hexString.startsWith("0x"))
			{
				hexString = hexString.substring(2);
			}
			if (hexString.length() > 0)
			{
				if (hexString.length() % 2 != 0)
				{
					// Make sure we have an even number of bytes
					hexString = "0" + hexString;
				}
				for (int i = 0; i < hexString.length(); i += 2)
				{
					final String hex = hexString.substring(i, i + 2);
					baos.write(Integer.valueOf(hex, 16));
				}
			}
		}

		return baos.toByteArray();
	}
}
