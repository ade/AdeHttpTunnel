package se.ade.httptunnel.fakehttp;

public class Protocol {
	public enum FrameType {
		UNDEFINED(0),
		JUNK(1),
		DATA(2),
		END(3);

		private int value;

		private FrameType(int value) {
			this.value = value;
		}

		public static FrameType parse(int value) {
			for(FrameType frameType : values()) {
				if(frameType.value == value) {
					return frameType;
				}
			}

			return UNDEFINED;
		}

		public int getValue() {
			return value;
		}
	}

	public static final int JUNK_FRAME_SIZE = 128;
	public static final int JUNK_FRAME_DELAY = 10000;
	public static final int MAX_FRAME_SIZE = 1024 * 1024; //1 mb.
}
