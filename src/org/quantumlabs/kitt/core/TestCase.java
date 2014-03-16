package org.quantumlabs.kitt.core;

public class TestCase extends TextBasedTTCNElement implements ITestCase {

	private String id;

	public TestCase(ITTCNElement ancestor, String name) {
		super(ancestor, name);
	}

	@Override
	public int getElementType() {
		return ITTCNElement.TEST_CASE;
	}

	public void setID(String id) {
		this.id = id;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public String getName() {
		return id;
	}
}
