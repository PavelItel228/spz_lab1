import java.util.Optional;


public class PhysicalPage
{
	private boolean p;
	private boolean isFree;
	private Integer lastUseTick;



	private Optional<VirtualPage> virtualPage;

	public PhysicalPage(Optional<VirtualPage> virtualPage)
	{
		this.p = false;
		this.isFree = true;
		lastUseTick = Controller.tick;
		this.virtualPage = virtualPage;
	}

	public boolean isFree()
	{
		return isFree;
	}

	public void setFree(boolean free)
	{
		isFree = free;
	}

	public Integer getLastUseTick()
	{
		return lastUseTick;
	}

	public void setLastUseTick(Integer lastUseTick)
	{
		this.lastUseTick = lastUseTick;
	}

	public boolean isP()
	{
		return p;
	}

	public void setP(boolean p)
	{
		this.p = p;
	}

	public Optional<VirtualPage> getVirtualPage()
	{
		return virtualPage;
	}

	public void setVirtualPage(Optional<VirtualPage> virtualPage)
	{
		this.virtualPage = virtualPage;
	}
}
