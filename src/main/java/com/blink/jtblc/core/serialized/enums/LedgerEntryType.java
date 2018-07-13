package com.blink.jtblc.core.serialized.enums;

import java.util.TreeMap;

import com.blink.jtblc.core.fields.Type;
import com.blink.jtblc.core.serialized.BinaryParser;
import com.blink.jtblc.core.serialized.BytesSink;
import com.blink.jtblc.core.serialized.SerializedType;
import com.blink.jtblc.core.serialized.TypeTranslator;
import com.blink.jtblc.encoding.common.B16;

public enum LedgerEntryType implements SerializedType {
	Invalid(-1), AccountRoot('a'), DirectoryNode('d'), GeneratorMap('g'), RippleState('r'), SuspendedPayment('u'),
    // Nickname ('n'), // deprecated
	Offer('o'), Contract('c'), LedgerHashes('h'), Amendments('f'), FeeSettings('s'), Ticket('T'), SignerList('S');
	final int ord;
	
	LedgerEntryType(int i) {
		ord = i;
	}
	
	static private TreeMap<Integer, LedgerEntryType> byCode = new TreeMap<Integer, LedgerEntryType>();
	static {
		for (Object a : LedgerEntryType.values()) {
			LedgerEntryType f = (LedgerEntryType) a;
			byCode.put(f.ord, f);
		}
	}
	
	@Override
	public Type type() {
		return Type.UInt16;
	}
	
	public static LedgerEntryType fromNumber(Number i) {
		return byCode.get(i.intValue());
	}
	
	public Integer asInteger() {
		return ord;
	}
	
	// SeralizedType interface
	@Override
	public byte[] toBytes() {
		return new byte[] { (byte) ((ord >>> 8) & 0xFF), (byte) (ord & 0xFF) };
	}
	
	@Override
	public Object toJSON() {
		return toString();
	}
	
	@Override
	public String toHex() {
		return B16.toString(toBytes());
	}
	
	@Override
	public void toBytesSink(BytesSink to) {
		to.add(toBytes());
	}
	
	public static class Translator extends TypeTranslator<LedgerEntryType> {
		@Override
		public LedgerEntryType fromParser(BinaryParser parser, Integer hint) {
			return fromNumber(parser.readOneInt() << 8 | parser.readOneInt());
		}
		
		@Override
		public LedgerEntryType fromInteger(int integer) {
			return fromNumber(integer);
		}
		
		@Override
		public LedgerEntryType fromString(String value) {
			return LedgerEntryType.valueOf(value);
		}
	}
	
	public static Translator translate = new Translator();
}