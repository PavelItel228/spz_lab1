import java.util.Optional;


public class VirtualPage {
	private boolean p;
	private boolean r;
	private boolean m;
	private Optional<PhysicalPage> physicalPage;

	public VirtualPage()
	{
		this.p = false;
		this.r = false;
		this.m = false;
		this.physicalPage = Optional.empty();
	}

	public boolean isP()
	{
		return p;
	}

	public void setP(boolean p)
	{
		this.p = p;
	}

	public boolean isR()
	{
		return r;
	}

	public void setR(boolean r)
	{
		this.r = r;
	}

	public boolean isM()
	{
		return m;
	}

	public void setM(boolean m)
	{
		this.m = m;
	}

	public Optional<PhysicalPage> getPhysicalPage()
	{
		return physicalPage;
	}

	public void setPhysicalPage(Optional<PhysicalPage> physicalPage)
	{
		this.physicalPage = physicalPage;
	}
}
