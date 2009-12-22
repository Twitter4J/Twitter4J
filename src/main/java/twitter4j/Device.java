package twitter4j;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Device
	implements Serializable
{
	private static final long serialVersionUID = -258215809702057490L;

	private static final Map<String, Device> instances=new HashMap<String, Device>();

	public static final Device IM = new Device("im");
	public static final Device SMS = new Device("sms");
	public static final Device NONE = new Device("none");

	private final String name;

	public static Device getInstance(String name)
	{
		return instances.get(name);
	}

	private Device(String name) {
		this.name = name;
		instances.put(name, this);
	}

	public String getName()
	{
		return name;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Device device = (Device) o;

		if (!name.equals(device.name)) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}

	@Override
	public String toString()
	{
		return name;
	}

	private Object readResolve()
    		throws ObjectStreamException
	{
		return getInstance(name);
	}

}
